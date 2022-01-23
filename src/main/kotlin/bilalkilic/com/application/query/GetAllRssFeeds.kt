package bilalkilic.com.application.query

import bilalkilic.com.domain.RssFeedCollection
import com.trendyol.kediatr.Query
import kotlinx.serialization.Serializable

@Serializable
class GetAllRssFeeds : Query<Collection<RssFeedCollection.RssFeed>>