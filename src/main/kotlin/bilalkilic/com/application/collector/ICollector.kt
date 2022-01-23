package bilalkilic.com.application.collector

import kotlinx.coroutines.*
import java.util.*
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutorService
import kotlin.coroutines.CoroutineContext

interface ICollector : CoroutineScope {
    val job: Job
    val executor: ExecutorService

    override val coroutineContext: CoroutineContext
        get() = job + executor.asCoroutineDispatcher()

    suspend fun collect()

    suspend fun repeatUntilCancelled(block: suspend () -> Unit) {
        while (isActive) {
            try {
                block()
                yield()
            } catch (ex: CancellationException) {
                println("test")
            } catch (ex: Exception) {
                println(ex.printStackTrace())
            }
        }
    }
}


fun <T> Optional<T>.unwrap(): T? = this.orElse(null)