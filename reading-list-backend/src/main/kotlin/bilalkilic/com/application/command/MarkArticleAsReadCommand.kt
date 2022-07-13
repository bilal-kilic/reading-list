package bilalkilic.com.application.command

import com.trendyol.kediatr.Command

data class MarkArticleAsReadCommand(
    val id: String,
) : Command