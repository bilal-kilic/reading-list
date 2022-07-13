package bilalkilic.com.domain

import bilalkilic.com.infastructure.plugins.InstantSerializer
import kotlinx.serialization.Serializable
import org.apache.commons.codec.digest.DigestUtils
import java.time.Instant

@Serializable
class RedditArticle private constructor(
    override val url: String,
    override val title: String,
    override val description: String,
    override val imageUrl: String?,
    override val siteName: String?,
    override val articleType: ArticleType?,
    @Serializable(with = InstantSerializer::class)
    override val publishedDate: Instant? = null,
    val author: String,
    val commentCount: Int,
    val downVotes: Int,
    val innerHtml: String?,
    val redditUrl: String,
    val subreddit: String,
    val upVotes: Int,
    val clicked: Boolean,
    override val id: String = DigestUtils.md5Hex(url),
) : BaseArticle() {

    companion object {
        fun create(
            url: String,
            title: String,
            description: String,
            imageUrl: String?,
            siteName: String?,
            author: String,
            commentCount: Int,
            downVotes: Int,
            innerHtml: String?,
            redditUrl: String,
            subreddit: String,
            upVotes: Int,
            clicked: Boolean,
            publishedDate: Instant?,
        ): RedditArticle {
            return RedditArticle(
                url = url,
                title = title,
                description = description,
                imageUrl = imageUrl,
                siteName = siteName,
                author = author,
                commentCount = commentCount,
                downVotes = downVotes,
                innerHtml = innerHtml,
                redditUrl = redditUrl,
                subreddit = subreddit,
                upVotes = upVotes,
                clicked = clicked,
                publishedDate = publishedDate,
                articleType = ArticleType.REDDIT
            )
        }
    }
}