package bilalkilic.com.domain.collection

import kotlinx.serialization.Serializable
import org.apache.commons.codec.digest.DigestUtils

@Serializable
class RssFeedCollection(
    private val feeds: MutableSet<RssFeed> = mutableSetOf(),
) {
    val documentType: String = type

    companion object {
        const val type = "rss-feed-collection"
    }

    fun add(url: String) {
        feeds.add(RssFeed(url))
    }

    fun remove(id: String) {
        feeds.removeIf { it.id == id }
    }

    fun getFeeds() = feeds.toList()

    @Serializable
    data class RssFeed(
        val url: String,
        val id: String = DigestUtils.md5Hex(url),
    )
}
