package bilalkilic.com

import bilalkilic.com.application.applicationModule
import bilalkilic.com.infastructure.http.httpModule
import bilalkilic.com.infastructure.persistance.couchbaseModule
import bilalkilic.com.infastructure.plugins.*
import bilalkilic.com.infastructure.routes.articleRouting
import bilalkilic.com.infastructure.routes.feedRouting
import com.typesafe.config.ConfigFactory
import io.bkbn.kompendium.Kompendium
import io.bkbn.kompendium.models.oas.OpenApiSpecInfo
import io.bkbn.kompendium.models.oas.OpenApiSpecInfoContact
import io.bkbn.kompendium.routes.openApi
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import org.koin.core.context.startKoin
import org.slf4j.event.Level

fun main() {
    embeddedServer(CIO, port = 6161, host = "0.0.0.0") {
        val oas = Kompendium.openApiSpec.copy(
            info = OpenApiSpecInfo(
                title = "Reading List",
                version = "1.0.0",
                contact = OpenApiSpecInfoContact(
                    name = "bilal.kilic",
                    email = "bkilic@gmail.com",
                )
            )
        )

        configureHTTP()
        configureSerialization()
        handleExceptions()
        install(HeaderIntercepting)
        install(DataConversion)
        install(Webjars)
        install(CallLogging) {
            level = Level.INFO
        }

        startKoin {
            modules(
                configurationModule,
                couchbaseModule,
                applicationModule,
                httpModule,
            )
        }

        routing {
            openApi(oas)
            swaggerUI()
            get { call.respondRedirect("/swagger-ui") }

            feedRouting()
            articleRouting()
        }

    }.start(wait = true)
}

fun Routing.swaggerUI(openApiJsonUrl: String = "/openapi.json") {
    get("/swagger-ui") {
        call.respondRedirect("/webjars/swagger-ui/index.html?url=$openApiJsonUrl", true)
    }
}
