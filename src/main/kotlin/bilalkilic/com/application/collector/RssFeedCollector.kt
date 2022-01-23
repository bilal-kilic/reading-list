package bilalkilic.com.application.collector

import bilalkilic.com.domain.Article
import bilalkilic.com.domain.RssFeedCollection
import bilalkilic.com.infastructure.persistance.get
import bilalkilic.com.infastructure.persistance.save
import com.apptastic.rssreader.Item
import com.apptastic.rssreader.RssReader
import com.couchbase.lite.Database
import com.github.siyoon210.ogparser4j.OgParser
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
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

    private val rssItemChannel = Channel<Item>(100)

    init {
        launch {
            repeatUntilCancelled {
                collect()
            }
        }

        launch {
            consumeRssItem()
        }
    }

    override suspend fun collect() {
        val feeds = feedDatabase.get<RssFeedCollection>(RssFeedCollection.type)?.getFeeds()

        feeds?.map { feed ->
            async {
                val a =rssReader.read(feed.url).toList()
                rssReader.build(XmlReader(URL(feed.url))).entries.toList()
            }
        }?.awaitAll()?.flatten()?.forEach {
            rssItemChannel.send(it)
        }
    }

    private suspend fun consumeRssItem() {
        rssItemChannel.consumeEach { item ->
            val openGraph = runCatching { ogParser.getOpenGraphOf(item.link.unwrap()) }.getOrNull()

            val article = if (openGraph != null) {
                Article.create(
                    item.link.unwrap() ?: "",
                    item.title.unwrap() ?: "",
                    item.description.unwrap() ?: "",
                    "",
                    ""
                )
            } else {
                Article.create(
                    item.link.unwrap() ?: "",
                    item.title.unwrap() ?: "",
                    item.description.unwrap() ?: "",
                    "",
                    ""
                )
            }

            articleDatabase.save(article.id, article)
        }
    }
}