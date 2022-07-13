package bilalkilic.com.domain

import kotlinx.serialization.Serializable

@Serializable
enum class ArticleType {
    ARTICLE,
    REDDIT;

    companion object {
        fun fromString(string: String): ArticleType? {
            return values().firstOrNull { it.name.equals(string, true) }
        }
    }
}