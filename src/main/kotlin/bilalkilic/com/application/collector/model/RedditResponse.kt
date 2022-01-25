package bilalkilic.com.application.collector.model

import kotlinx.serialization.Serializable

@Serializable
data class RedditResponse(
    val data: Data,
) {
    @Serializable
    data class Data(
        val children: List<Children>,
    ) {
        @Serializable
        data class Children(
            val data: RedditPostData,
        ) {
            @Serializable
            data class RedditPostData(
                val author: String,
                val clicked: Boolean,
                val created_utc: Double,
                val downs: Int,
                val is_meta: Boolean,
                val is_self: Boolean,
                val is_video: Boolean,
                val name: String,
                val num_comments: Int,
                val permalink: String,
                val pinned: Boolean,
                val score: Int,
                val selftext: String,
                val selftext_html: String? = null,
                val stickied: Boolean,
                val subreddit: String,
                val title: String,
                val ups: Int,
                val url: String?,
                val visited: Boolean,
                val media: Media? = null,
            ) {

                fun getImageInUrl(): String? {
                    return if (
                        url?.endsWith(".jpg") == true ||
                        url?.endsWith(".png") == true ||
                        url?.endsWith(".gif") == true
                    ) url else null
                }

                fun redditUrl(): String {
                    return "https://reddit.com$permalink"
                }

                @Serializable
                data class Media(
                    val thumbnail_url: String? = null,
                )
            }
        }
    }
}