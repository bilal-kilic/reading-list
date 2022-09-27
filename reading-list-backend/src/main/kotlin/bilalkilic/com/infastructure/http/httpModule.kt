package bilalkilic.com.infastructure.http

import bilalkilic.com.infastructure.plugins.jsonSerializer
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import org.koin.dsl.module

val httpModule = module {
    single {
        HttpClient(CIO) {
            expectSuccess = false
            engine {
                pipelining = true
            }

            install(DefaultRequest) {
                this.accept(ContentType.Application.Json)
                this.contentType(ContentType.Application.Json)
            }

            install(ContentNegotiation) {
                val converter = KotlinxSerializationConverter(jsonSerializer)
                register(ContentType.Application.Json, converter)
            }
        }
    }
}
