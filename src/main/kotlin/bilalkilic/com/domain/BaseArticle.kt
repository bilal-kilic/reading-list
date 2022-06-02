package bilalkilic.com.domain

import bilalkilic.com.infastructure.plugins.InstantSerializer
import kotlinx.serialization.Serializable
import org.apache.commons.codec.digest.DigestUtils
import java.time.Instant

@Serializable
sealed class BaseArticle {
    abstract val url: String
    abstract val title: String?
    abstract val description: String?
    abstract val imageUrl: String?
    abstract val siteName: String?
    abstract val articleType: ArticleType?
    abstract val id: String
    @Serializable(with = InstantSerializer::class)
    abstract val publishedDate: Instant?

    val documentType: String = type
    var isRead: Boolean = false
    val collectionDate: Long = Instant.now().toEpochMilli()

    fun markAsRead() {
        this.isRead = true
    }

    companion object {
        const val type = "article"
    }
}