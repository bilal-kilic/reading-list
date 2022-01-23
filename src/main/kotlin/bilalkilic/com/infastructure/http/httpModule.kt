package bilalkilic.com.infastructure.http

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
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
        }
    }
}