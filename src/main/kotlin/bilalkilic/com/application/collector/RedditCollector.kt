package bilalkilic.com.application.collector

import bilalkilic.com.application.collector.model.RedditResponse
import bilalkilic.com.domain.RedditArticle
import bilalkilic.com.domain.RedditFeedCollection
import bilalkilic.com.infastructure.persistance.ArticleRepository
import bilalkilic.com.infastructure.persistance.get
import com.couchbase.lite.Database
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.umehara.ogmapper.OgMapper
import io.umehara.ogmapper.domain.OgTags
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

@DelicateCoroutinesApi
class RedditCollector(
    private val feedDatabase: Database,
    private val articleRepository: ArticleRepository,
    private val httpClient: HttpClient,
    private val ogMapper: OgMapper,
) : ICollector {
    override val logger: Logger = LoggerFactory.getLogger(RedditCollector::class.java)

    init {
        GlobalScope.launch {
            repeatUntilCancelled {
                collect()
            }
        }
    }

    override suspend fun collect() {
        val feeds = feedDatabase.get<RedditFeedCollection>(RedditFeedCollection.type)?.getFeeds() ?: return

        channelFlow {
            feeds.map { feed ->
                launch {
                    val redditData = httpClient
                        .get(feed.getRedditUrl())
                        .body<RedditResponse>()
                        .data
                        .children

                    redditData.map {
                        launch {
                            val article = parseArticle(it.data)
                            send(article)
                        }
                    }.joinAll()
                }
            }.joinAll()
        }.collect { article ->
            val existingArticle = articleRepository.get(article.id)

            if (existingArticle == null) {
                articleRepository.save(article)
            }
        }
    }

    private fun parseArticle(item: RedditResponse.Data.Children.RedditPostData): RedditArticle {
        val ogTags: OgTags? = runCatching {
            ogMapper.process(URL(item.url))
        }.getOrNull()

        val imageUrl = ogTags?.image?.toExternalForm() ?: item.media?.thumbnail_url ?: item.getImageInUrl()

        return if (item.is_self || ogTags == null) {
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
                title = ogTags.title ?: item.title,
                description = ogTags.description ?: item.selftext,
                innerHtml = item.selftext_html,
                imageUrl = imageUrl,
                siteName = ogTags.siteName ?: "reddit",
                author = item.author,
                commentCount = item.num_comments,
                downVotes = item.downs,
                redditUrl = item.permalink,
                subreddit = item.subreddit,
                upVotes = item.ups,
                clicked = false,
            )
        }
    }
}
