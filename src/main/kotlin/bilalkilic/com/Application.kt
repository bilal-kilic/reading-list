package bilalkilic.com

import bilalkilic.com.application.applicationModule
import bilalkilic.com.infastructure.http.httpModule
import bilalkilic.com.infastructure.persistance.couchbaseModule
import bilalkilic.com.infastructure.plugins.HeaderIntercepting
import bilalkilic.com.infastructure.plugins.configureHTTP
import bilalkilic.com.infastructure.plugins.configureSerialization
import bilalkilic.com.infastructure.plugins.handleExceptions
import bilalkilic.com.infastructure.routes.articleRouting
import bilalkilic.com.infastructure.routes.feedRouting
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
                title = "International Content Api",
                version = "1.33.7",
                contact = OpenApiSpecInfoContact(
                    name = "pim",
                    email = "pim@trendyol.com",
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
