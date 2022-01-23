package bilalkilic.com.application.query

import bilalkilic.com.domain.RssFeedCollection
import bilalkilic.com.infastructure.persistance.get
import com.couchbase.lite.Database
import com.trendyol.kediatr.AsyncQueryHandler

class GetAllRssFeedsAsyncHandler(
    private val feedDatabase: Database,
) : AsyncQueryHandler<GetAllRssFeeds, Collection<RssFeedCollection.RssFeed>> {

    override suspend fun handleAsync(query: GetAllRssFeeds): Collection<RssFeedCollection.RssFeed> {
        val rssFeed = feedDatabase.get<RssFeedCollection>(RssFeedCollection.type) ?: throw Exception()
        return rssFeed.getFeeds()
    }
}
