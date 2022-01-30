package bilalkilic.com.infastructure.plugins

import com.typesafe.config.ConfigFactory
import org.koin.dsl.module

val configurationModule = module {
    single {
        val env = System.getenv("KTOR_PROFILES_ACTIVE") ?: "dev"
        val envConfig = ConfigFactory.parseResources("application-${env}.conf")
        val defaultConfig = ConfigFactory.parseResources("application.conf")
        ConfigFactory.systemProperties()
            .withFallback(envConfig)
            .withFallback(defaultConfig)
            .resolve()
    }
}