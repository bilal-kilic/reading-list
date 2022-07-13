package bilalkilic.com.application.command

import bilalkilic.com.domain.collection.SortType
import com.trendyol.kediatr.Command
import kotlinx.serialization.Serializable

@Serializable
data class CreateRedditFeedCommand(
    val subredditName: String,
    val sortType: SortType
) : Command
