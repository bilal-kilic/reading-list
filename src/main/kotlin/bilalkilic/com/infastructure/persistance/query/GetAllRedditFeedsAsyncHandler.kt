package bilalkilic.com.infastructure.persistance.query

import bilalkilic.com.application.query.GetAllRedditFeeds
import bilalkilic.com.domain.RedditFeedCollection
import bilalkilic.com.infastructure.persistance.get
import com.couchbase.lite.Database
import com.trendyol.kediatr.AsyncQueryHandler

class GetAllRedditFeedsAsyncHandler(
    private val feedDatabase: Database
) :
    AsyncQueryHandler<GetAllRedditFeeds, Collection<RedditFeedCollection.RedditFeed>> {

    override suspend fun handleAsync(query: GetAllRedditFeeds): Collection<RedditFeedCollection.RedditFeed> {
        val rssFeed = feedDatabase.get<RedditFeedCollection>(RedditFeedCollection.type) ?: throw Exception()
        return rssFeed.getFeeds()
    }
}