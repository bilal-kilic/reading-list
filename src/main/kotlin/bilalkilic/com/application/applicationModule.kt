package bilalkilic.com.application

import bilalkilic.com.application.collector.RedditCollector
import bilalkilic.com.application.collector.RssFeedCollector
import bilalkilic.com.application.command.CreateRedditFeedCommandAsyncHandler
import bilalkilic.com.application.command.CreateRssFeedCommandAsyncHandler
import bilalkilic.com.application.query.GetAllRedditFeedsAsyncHandler
import bilalkilic.com.application.query.GetAllRssFeedsAsyncHandler
import bilalkilic.com.application.query.GetArticlesByPageAsyncHandler
import com.apptastic.rssreader.RssReader
import com.rometools.rome.io.SyndFeedInput
import com.trendyol.kediatr.koin.KediatrKoin.Companion.getCommandBus
import io.umehara.ogmapper.OgMapper
import io.umehara.ogmapper.jsoup.JsoupOgMapperFactory
import org.koin.core.qualifier.named
import org.koin.dsl.module

val applicationModule = module {
    single(createdAtStart = true) { getCommandBus() }
    single { CreateRssFeedCommandAsyncHandler(get(named("feedDatabase"))) }
    single { CreateRedditFeedCommandAsyncHandler(get(named("feedDatabase"))) }
    single { GetAllRedditFeedsAsyncHandler(get(named("feedDatabase"))) }
    single { GetAllRssFeedsAsyncHandler(get(named("feedDatabase"))) }
    single { GetArticlesByPageAsyncHandler(get(named("articleDatabase"))) }

    single { RssReader() }
    single {
        val ogMapper: OgMapper = JsoupOgMapperFactory().build()
        ogMapper
    }
    single(createdAtStart = true) {
        RssFeedCollector(
            get(named("feedDatabase")),
            get(named("articleDatabase")),
            get(),
            SyndFeedInput())
    }
    single(createdAtStart = true) {
        RedditCollector(
            get(named("feedDatabase")),
            get(named("articleDatabase")),
            get(),
            get(),
        )
    }
}