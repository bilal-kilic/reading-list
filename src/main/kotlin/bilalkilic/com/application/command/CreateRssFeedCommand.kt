package bilalkilic.com.application.command

import com.trendyol.kediatr.Command
import kotlinx.serialization.Serializable

@Serializable
data class CreateRssFeedCommand(
    val url: String
) : Command