package bilalkilic.com.infastructure.http

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.dsl.module

val httpModule = module {
    single {
        HttpClient(OkHttp) {
            expectSuccess = false
            engine {
                config {
                    retryOnConnectionFailure(true)
                }
            }
            install(DefaultRequest) {
                this.accept(ContentType.Application.Json)
                this.contentType(ContentType.Application.Json)
            }
        }
    }
}
