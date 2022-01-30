package bilalkilic.com.application.collector

import bilalkilic.com.domain.Article
import bilalkilic.com.domain.RssFeedCollection
import bilalkilic.com.infastructure.persistance.ArticleRepository
import bilalkilic.com.infastructure.persistance.get
import com.couchbase.lite.Database
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import io.umehara.ogmapper.OgMapper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.URL

@DelicateCoroutinesApi
class RssFeedCollector(
    private val feedDatabase: Database,
    private val articleRepository: ArticleRepository,
    private val ogMapper: OgMapper,
    private val rssReader: SyndFeedInput,
) : ICollector {

    override val logger: Logger = LoggerFactory.getLogger(RssFeedCollector::class.java)

    init {
        GlobalScope.launch {
            repeatUntilCancelled {
                collect()
            }
        }

    }

    override suspend fun collect() {
        val feeds = feedDatabase.get<RssFeedCollection>(RssFeedCollection.type)?.getFeeds() ?: return

        channelFlow {
            feeds.map { feed ->
                launch {
                    val rssItems = rssReader.build(XmlReader(URL(feed.url))).entries.toList()
                    rssItems.map { item ->
                        launch {
                            val article = parseArticle(item)
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

    private fun parseArticle(item: SyndEntry): Article {
        val ogTags = runCatching { ogMapper.process(URL(item.link)) }.getOrNull()

        return if (ogTags != null) {
            Article.create(
                item.link,
                ogTags.title ?: item.title,
                ogTags.description ?: item.description?.value,
                ogTags.image?.toExternalForm(),
                ogTags.siteName,
            )
        } else {
            Article.create(
                item.link ?: "",
                item.title,
                item.description?.value,
                "",
                ""
            )
        }
    }
}