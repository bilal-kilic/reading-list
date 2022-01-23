package bilalkilic.com.infastructure.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.*


class ErrorHandling {

    class Configuration {
        lateinit var handleException: (context: PipelineContext<Unit, ApplicationCall>, ex: Exception)-> ErrorDTO

        fun exceptionHandler(handleException : (context: PipelineContext<Unit, ApplicationCall>, ex: Exception)-> ErrorDTO) {
            this.handleException = handleException
        }
    }

    /**
     * Installable feature to handle exception with given exception handler config [MDCContext].
     */
    companion object Feature : ApplicationPlugin<Application, Configuration, ErrorHandling> {
        override val key: AttributeKey<ErrorHandling> = AttributeKey("Error Handling Key")
        override fun install(pipeline: Application, configure: Configuration.() -> Unit): ErrorHandling {
            val errorHandlingPhase = PipelinePhase("Error Handling")
            val configuration = Configuration().apply(configure)

            pipeline.insertPhaseBefore(ApplicationCallPipeline.Call, errorHandlingPhase)

            pipeline.intercept(errorHandlingPhase) {
                try {
                    proceed()
                } catch (ex: Exception) {
                    val errorDto = configuration.handleException(this, ex)
                    this.call.respond(errorDto.httpStatusCode, errorDto)
                    this.finish()
                }
            }

            return ErrorHandling()
        }
    }

    @kotlinx.serialization.Serializable
    data class ErrorDTO(
        @kotlinx.serialization.Transient
        val httpStatusCode: HttpStatusCode = HttpStatusCode.InternalServerError,
        val detail: String,
        val requestUri: String,
        val requestMethod: String?,
        val instant: String,
        val message: String,
        val title: String = httpStatusCode.description,
        val status: Int = httpStatusCode.value,
    )
}