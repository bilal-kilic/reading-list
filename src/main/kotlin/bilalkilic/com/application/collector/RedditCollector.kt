package bilalkilic.com.application.collector

import bilalkilic.com.application.collector.model.RedditResponse
import bilalkilic.com.domain.RedditArticle
import bilalkilic.com.domain.RedditFeedCollection
import bilalkilic.com.infastructure.persistance.get
import bilalkilic.com.infastructure.persistance.save
import com.couchbase.lite.Database
import com.github.siyoon210.ogparser4j.OgParser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RedditCollector(
    private val feedDatabase: Database,
    private val articleDatabase: Database,
    private val httpClient: HttpClient,
    private val ogParser: OgParser,
) : ICollector {
    override val job: Job
        get() = SupervisorJob()
    override val executor: ExecutorService
        get() = Executors.newFixedThreadPool(16 * 2 - 1)

    init {
        launch {
            repeatUntilCancelled {
                collect()
            }
        }
    }

    override suspend fun collect() {
        val feeds = feedDatabase.get<RedditFeedCollection>(RedditFeedCollection.type)?.getFeeds()

        feeds?.map { feed ->
            async {
                httpClient
                    .get(feed.getRedditUrl())
                    .body<RedditResponse>()
                    .data
                    .children
            }
        }?.awaitAll()
            ?.flatten()
            ?.forEach {
                consumeRssItem(it.data)
            }
    }

    private fun consumeRssItem(item: RedditResponse.Data.Children.RedditPostData) {
        val openGraph = runCatching { ogParser.getOpenGraphOf(item.url) }.getOrNull()

        val imageUrl = openGraph?.get("image") ?: item.media?.thumbnail_url ?: item.getImageInUrl()

        val article = if (item.is_self || openGraph == null) {
            RedditArticle.create(
                url = item.url ?: item.redditUrl(),
                title = item.title,
                description = item.selftext,
                innerHtml = item.selftext_html,
                imageUrl = imageUrl,
                siteName = "reddit",
                author = item.author,
                commentCount = item.num_comments,
                downVotes = item.downs,
                redditUrl = item.redditUrl(),
                subreddit = item.subreddit,
                upVotes = item.ups,
                clicked = false,
            )
        } else {
            RedditArticle.create(
                url = item.url ?: item.redditUrl(),
                title = openGraph.get("title") ?: item.title,
                description = openGraph.get("description") ?: item.selftext,
                innerHtml = item.selftext_html,
                imageUrl = imageUrl,
                siteName = openGraph.get("site_name") ?: "reddit",
                author = item.author,
                commentCount = item.num_comments,
                downVotes = item.downs,
                redditUrl = item.permalink,
                subreddit = item.subreddit,
                upVotes = item.ups,
                clicked = false,
            )
        }

        articleDatabase.save(article.id, article)
    }

}
