package bilalkilic.com.infastructure.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import kotlinx.serialization.json.Json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(jsonSerializer)
    }
}


val jsonSerializer = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    coerceInputValues  = true
}