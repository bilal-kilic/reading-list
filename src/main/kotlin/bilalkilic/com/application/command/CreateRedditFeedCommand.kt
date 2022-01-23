package bilalkilic.com.application.command

import bilalkilic.com.domain.SortType
import com.trendyol.kediatr.Command

data class CreateRedditFeedCommand(
    val subredditName: String,
    val sortType: SortType
) : Command
