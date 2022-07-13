package bilalkilic.com.infastructure.plugins

import io.ktor.server.routing.*
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

inline fun <reified T : Any> Route.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    lazy { get<T>(qualifier, parameters) }

inline fun <reified T : Any> Route.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
) =
    KoinJavaComponent.getKoin().get<T>(qualifier, parameters)
