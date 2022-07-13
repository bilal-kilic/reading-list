package bilalkilic.com.application.query

import bilalkilic.com.domain.RedditArticle
import com.trendyol.kediatr.Query

data class GetRedditArticlesByPage(
    val page: Int = 0,
    val pageSize: Int = 20,
    val isRead: Boolean?,
    val subreddit: String?,
) : Query<PageResponse<RedditArticle>>