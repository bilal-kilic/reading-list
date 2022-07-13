package bilalkilic.com.infastructure.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun Application.handleExceptions() {
    val logger: Logger = LoggerFactory.getLogger(ErrorHandling::class.java)
    install(ErrorHandling) {
        exceptionHandler { context, ex ->
            val request = context.context.request
            when (ex) {
                else -> {
                    logger.error("Throwable: ", ex)
                    val message = "default.exception.message"
                    createError(HttpStatusCode.InternalServerError, request.uri, request.httpMethod.value, ex.stackTraceToString(), message)
                }
            }
        }
    }
}

fun createError(httpStatus: HttpStatusCode, message: String, uri: String, methodName: String?, detail: String): ErrorHandling.ErrorDTO {
    return ErrorHandling.ErrorDTO(
        httpStatus,
        detail,
        uri,
        methodName,
        ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT),
        message
    )
}
