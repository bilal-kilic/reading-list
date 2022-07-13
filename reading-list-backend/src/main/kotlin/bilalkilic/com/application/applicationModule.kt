package bilalkilic.com.application

import bilalkilic.com.application.collector.RedditCollector
import bilalkilic.com.application.collector.RssFeedCollector
import bilalkilic.com.application.command.handler.CreateRedditFeedCommandAsyncHandler
import bilalkilic.com.application.command.handler.CreateRssFeedCommandAsyncHandler
import bilalkilic.com.application.command.handler.DeleteRedditFeedCommandAsyncHandler
import bilalkilic.com.application.command.handler.DeleteRssFeedCommandAsyncHandler
import bilalkilic.com.application.command.handler.MarkArticleAsReadCommandHandler
import bilalkilic.com.infastructure.persistance.query.GetAllRedditFeedsAsyncHandler
import bilalkilic.com.infastructure.persistance.query.GetAllRssFeedsAsyncHandler
import bilalkilic.com.infastructure.persistance.query.GetArticlesByPageAsyncHandler
import bilalkilic.com.infastructure.persistance.query.GetRedditArticlesByPageAsyncHandler
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
    single { DeleteRssFeedCommandAsyncHandler(get(named("feedDatabase"))) }
    single { DeleteRedditFeedCommandAsyncHandler(get(named("feedDatabase"))) }
    single { GetAllRedditFeedsAsyncHandler(get(named("feedDatabase"))) }
    single { GetAllRssFeedsAsyncHandler(get(named("feedDatabase"))) }
    single { GetArticlesByPageAsyncHandler(get(named("articleDatabase"))) }
    single { GetRedditArticlesByPageAsyncHandler(get(named("articleDatabase"))) }
    single { MarkArticleAsReadCommandHandler(get()) }

    single { RssReader() }
    single {
        val ogMapper: OgMapper = JsoupOgMapperFactory().build()
        ogMapper
    }
    single(createdAtStart = true) {
        RssFeedCollector(
            get(named("feedDatabase")),
            get(),
            get(),
            SyndFeedInput()
        )
    }
    single(createdAtStart = true) {
        RedditCollector(
            get(named("feedDatabase")),
            get(),
            get(),
            get(),
        )
    }
}
