package bilalkilic.com.infastructure.plugins

import io.ktor.server.application.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.withContext
import kotlinx.coroutines.slf4j.MDCContext

class HeaderIntercepting {
    class Configuration {
        val defaultHeaders = mutableMapOf<String, String>()

        fun addDefaultHeaderIfAbsent(key: String, value: String) {
            defaultHeaders[key] = value
        }
    }

    /**
     * Installable feature for intercepting http headers and putting them into [MDCContext].
     */
    companion object Feature : ApplicationPlugin<Application, Configuration, HeaderIntercepting> {
        override val key: AttributeKey<HeaderIntercepting> = AttributeKey("Header Intercepting")
        override fun install(pipeline: Application, configure: Configuration.() -> Unit): HeaderIntercepting {
            val headerPhase = PipelinePhase("Header Intercepting")
            val configuration = Configuration().apply(configure)

            pipeline.insertPhaseBefore(ApplicationCallPipeline.Call, headerPhase)

            pipeline.intercept(headerPhase) {
                if (this.call.response.status() != null) proceed()

                val headers = mutableMapOf<String, String>()
                this.context.request.headers.forEach { s, list ->
                    headers[s] = list.first()
                }

                configuration.defaultHeaders.forEach {
                    headers.putIfAbsent(it.key, it.value)
                }

                withContext(MDCContext(headers)) {
                    proceed()
                }
            }

            return HeaderIntercepting()
        }
    }
}
