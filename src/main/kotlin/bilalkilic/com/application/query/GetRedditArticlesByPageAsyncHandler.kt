package bilalkilic.com.application.query

import bilalkilic.com.domain.Article
import bilalkilic.com.domain.BaseArticle
import bilalkilic.com.domain.RedditArticle
import bilalkilic.com.infastructure.plugins.jsonSerializer
import com.couchbase.lite.*
import com.couchbase.lite.Function
import com.trendyol.kediatr.AsyncQueryHandler
import kotlinx.serialization.decodeFromString

class GetRedditArticlesByPageAsyncHandler(
    private val articleDatabase: Database,
) : AsyncQueryHandler<GetRedditArticlesByPage, PageResponse<RedditArticle>> {

    override suspend fun handleAsync(query: GetRedditArticlesByPage): PageResponse<RedditArticle> {
        val dataSource = DataSource.database(articleDatabase)
        var where = Expression.property("documentType").equalTo(Expression.string(BaseArticle.type))

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
            .map { jsonSerializer.decodeFromString<RedditArticle>(it) }

        return PageResponse(query.page, query.pageSize, totalCount, data)
    }
}