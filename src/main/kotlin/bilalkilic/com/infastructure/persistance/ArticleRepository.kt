package bilalkilic.com.infastructure.persistance

import bilalkilic.com.domain.BaseArticle
import com.couchbase.lite.Database

class ArticleRepository(
    private val articleDatabase: Database
) {
    fun save(article: BaseArticle) {
        articleDatabase.save(article.id, article)
    }

    fun get(id: String): BaseArticle? {
        return articleDatabase.get<BaseArticle>(id)
    }
}