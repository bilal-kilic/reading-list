package bilalkilic.com.application.collector

import bilalkilic.com.domain.Article
import bilalkilic.com.domain.RssFeedCollection
import bilalkilic.com.infastructure.persistance.get
import bilalkilic.com.infastructure.persistance.save
import com.couchbase.lite.Database
import com.github.siyoon210.ogparser4j.OgParser
import com.github.siyoon210.ogparser4j.OpenGraph
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RssFeedCollector(
    private val feedDatabase: Database,
    private val articleDatabase: Database,
    private val httpClient: HttpClient,
    private val ogParser: OgParser,
    private val rssReader: SyndFeedInput,
) : ICollector {
    override val job: Job
        get() = SupervisorJob()
    override val executor: ExecutorService
        get() = Executors.newFixedThreadPool(16 * 2 - 1)

    private val rssItemChannel = Channel<SyndEntry>(100)

    init {
        launch {
            repeatUntilCancelled {
                collect()
            }
        }

    }

    override suspend fun collect() {
        val feeds = feedDatabase.get<RssFeedCollection>(RssFeedCollection.type)?.getFeeds()

        feeds?.map { feed ->
            async {
                rssReader.build(XmlReader(URL(feed.url))).entries.toList()
            }
        }?.awaitAll()?.flatten()?.forEach {
            consumeRssItem(it)
        }
    }

    private fun consumeRssItem(item: SyndEntry) {
        val openGraph = runCatching { ogParser.getOpenGraphOf(item.link) }.getOrNull()

        val article = if (openGraph != null) {
            Article.create(
                item.link,
                openGraph.get("title") ?: item.title,
                openGraph.get("description") ?: item.description?.value,
                openGraph.get("image"),
                openGraph.get("site_name"),
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

        articleDatabase.save(article.id, article)
    }
}

fun OpenGraph.get(property: String): String? {
    return runCatching { this.getContentOf(property).value }.getOrNull()
}