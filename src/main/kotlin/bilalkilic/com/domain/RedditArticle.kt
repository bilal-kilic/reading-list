package bilalkilic.com.domain

import kotlinx.serialization.Serializable

@Serializable
class RedditArticle private constructor(
    override val url: String,
    override val title: String,
    override val description: String,
    override val imageUrl: String?,
    override val siteName: String?,
    override val articleType: ArticleType?,
    val author: String,
    val commentCount: Int,
    val downVotes: Int,
    val innerHtml: String,
    val redditUrl: String,
    val subreddit: String,
    val upVotes: Int,
    val clicked: Boolean,
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
            innerHtml: String,
            redditUrl: String,
            subreddit: String,
            upVotes: Int,
            clicked: Boolean,
        ) {
            RedditArticle(
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
                articleType = ArticleType.REDDIT
            )
        }
    }
}