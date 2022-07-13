package bilalkilic.com.application.collector

import kotlinx.coroutines.*
import org.slf4j.Logger
import java.time.Duration
import java.util.*
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

interface ICollector : CoroutineScope {
    val logger: Logger

    val job: CompletableJob
        get() = SupervisorJob()

    val dispatcher: ExecutorCoroutineDispatcher
        get() = Executors.newFixedThreadPool((Runtime.getRuntime().availableProcessors()) - 1).asCoroutineDispatcher()

    override val coroutineContext: CoroutineContext
        get() = job + dispatcher

    val delayDuration: Duration
        get() = Duration.ofMinutes(100)

    suspend fun collect()

    suspend fun repeatUntilCancelled(block: suspend () -> Unit) {
        while (isActive) {
            try {
                block()
            } catch (ex: CancellationException) {
                logger.error("Worker loop cancelled", ex)
            } catch (ex: Exception) {
                logger.error("Exception occurred in worker loop", ex)
            } finally {
                delay(delayDuration.toMillis())
            }
        }
    }
}

fun <T> Optional<T>.unwrap(): T? = this.orElse(null)