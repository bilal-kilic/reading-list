package bilalkilic.com.infastructure.persistance.query

import bilalkilic.com.application.query.GetArticlesByPage
import bilalkilic.com.application.query.PageResponse
import bilalkilic.com.domain.Article
import bilalkilic.com.domain.ArticleType
import bilalkilic.com.domain.BaseArticle
import bilalkilic.com.infastructure.plugins.jsonSerializer
import com.couchbase.lite.*
import com.couchbase.lite.Function
import com.fasterxml.jackson.databind.ser.Serializers.Base
import com.trendyol.kediatr.AsyncQueryHandler
import kotlinx.serialization.decodeFromString

class GetArticlesByPageAsyncHandler(
    private val articleDatabase: Database,
) : AsyncQueryHandler<GetArticlesByPage, PageResponse<BaseArticle>> {

    override suspend fun handleAsync(query: GetArticlesByPage): PageResponse<BaseArticle> {
        val dataSource = DataSource.database(articleDatabase)
        var where = Expression.property("documentType").equalTo(Expression.string(BaseArticle.type))
            .and(Expression.property("articleType").equalTo(Expression.string(ArticleType.ARTICLE.name)))

        if (query.isRead != null) {
            where = where.and(Expression.property("isRead").equalTo(Expression.booleanValue(query.isRead)))
        }

        val totalCount = QueryBuilder.select(SelectResult.expression(Function.count(Expression.string("*"))).`as`("count"))
                             .from(dataSource)
                             .where(where)
                             .execute()
                             .firstOrNull()
                             ?.getInt("count") ?: 0

        val data = QueryBuilder.select(SelectResult.all())
            .from(dataSource)
            .where(where)
            .orderBy(Ordering.property("collectionDate").descending())
            .limit(Expression.intValue(query.pageSize), Expression.intValue(query.page * query.pageSize))
            .execute()
            .toList()
            .map { it.getDictionary("article-database")?.toJSON() ?: "" }
            .map { jsonSerializer.decodeFromString<BaseArticle>(it) }

        return PageResponse(query.page, query.pageSize, totalCount, data)
    }
}