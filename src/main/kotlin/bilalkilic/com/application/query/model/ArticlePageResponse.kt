package bilalkilic.com.application.query.model

import bilalkilic.com.application.query.divRountUp
import bilalkilic.com.domain.Article
import bilalkilic.com.domain.BaseArticle

data class ArticlePageResponse(
    val page: Int,
    val pageSize: Int,
    val totalElementCount: Int,
    val data: List<BaseArticle>,
    val totalPageCount: Int = divRountUp(totalElementCount, pageSize)
)