package bilalkilic.com.application.query

import com.trendyol.kediatr.Query
import kotlinx.serialization.Serializable
import kotlin.math.ceil

@Serializable
class PageResponse<T>(
    val page: Int,
    val pageSize: Int,
    val totalElementCount: Int,
    val data: List<T>,
    val totalPageCount: Int = divRountUp(totalElementCount, pageSize),
)

interface PageableQuery<T>: Query<PageResponse<T>> {
    val page: Int
    val pageSize: Int
}

fun divRountUp(divident: Int, divisor: Int) = ceil(divident.toDouble() / divisor).toInt()
