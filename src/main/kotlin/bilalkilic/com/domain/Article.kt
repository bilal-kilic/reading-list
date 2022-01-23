package bilalkilic.com.domain

import kotlinx.serialization.Serializable

@Serializable
class Article private constructor(
    override val url: String,
    override val title: String?,
    override val description: String?,
    override val imageUrl: String?,
    override val siteName: String?,
    override val articleType: ArticleType? = ArticleType.ARTICLE,
) : BaseArticle() {


    companion object {
        fun create(
            url: String,
            title: String,
            description: String,
            imageUrl: String?,
            siteName: String?,
        ): Article {
            return Article(
                url,
                title,
                description,
                imageUrl,
                siteName,
                ArticleType.ARTICLE
            )
        }
    }
}


