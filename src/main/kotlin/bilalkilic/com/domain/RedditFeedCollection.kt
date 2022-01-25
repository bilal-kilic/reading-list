package bilalkilic.com.domain

import kotlinx.serialization.Serializable
import org.apache.commons.codec.digest.DigestUtils

@Serializable
class RedditFeedCollection(
    private val feeds: MutableSet<RedditFeed> = mutableSetOf(),
) {
    val documentType: String = type

    companion object {
        const val type = "reddit_feed_collection"
    }

    fun add(subredditName: String) {
        feeds.add(RedditFeed(subredditName))
    }

    fun getFeeds() = feeds.toList()

    @Serializable
    data class RedditFeed(
        val subredditName: String,
        val sorting: SortType = SortType.HOT,
        val id: String = DigestUtils.md5Hex(subredditName),
    ) {
        fun getRedditUrl(): String = "https://www.reddit.com/r/$subredditName/${sorting.name.lowercase()}/.json"
    }
}

