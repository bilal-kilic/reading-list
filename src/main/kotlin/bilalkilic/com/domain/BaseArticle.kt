package bilalkilic.com.domain

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