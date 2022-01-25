package bilalkilic.com.domain

import kotlinx.serialization.Serializable
import org.apache.commons.codec.digest.DigestUtils

@Serializable
class Article private constructor(
    override val url: String,
    override val title: String?,
    override val description: String?,
    override val imageUrl: String?,
    override val siteName: String?,
    override val articleType: ArticleType? = ArticleType.ARTICLE,
    override val id: String = DigestUtils.md5Hex(url)
) : BaseArticle() {


    companion object {
        fun create(
            url: String,
            title: String?,
            description: String?,
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


