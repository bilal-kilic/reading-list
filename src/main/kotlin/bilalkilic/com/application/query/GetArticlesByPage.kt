package bilalkilic.com.application.query

import bilalkilic.com.domain.Article
import bilalkilic.com.domain.ArticleType
import com.trendyol.kediatr.Query

data class GetArticlesByPage(
    val page: Int = 0,
    val pageSize: Int = 20,
    val articleType: ArticleType?,
    val isRead: Boolean?,
) : Query<PageResponse<Article>>


