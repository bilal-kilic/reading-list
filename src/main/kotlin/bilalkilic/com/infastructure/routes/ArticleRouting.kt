package bilalkilic.com.infastructure.routes

import bilalkilic.com.application.command.MarkArticleAsReadCommand
import bilalkilic.com.application.query.GetArticlesByPage
import bilalkilic.com.application.query.GetRedditArticlesByPage
import bilalkilic.com.domain.ArticleType
import bilalkilic.com.infastructure.plugins.inject
import bilalkilic.com.infastructure.routes.documentation.*
import com.trendyol.kediatr.CommandBus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.put

fun Route.articleRouting() {
    val commandBus by inject<CommandBus>()

    route("articles") {
        get("", getArticlesByPageParams) {
            val page = call.parameters["page"]?.toInt() ?: 0
            val pageSize = call.parameters["pageSize"]?.toInt() ?: 20
            val isRead = call.parameters["isRead"]?.toBoolean()
            val articleType = ArticleType.fromString(call.parameters["articleType"] ?: "")
            val articles = commandBus.executeQueryAsync(GetArticlesByPage(page, pageSize, articleType, isRead))
            call.respond(HttpStatusCode.OK, articles)
        }

        put("/{id}", markPostAsReadParams) {
            val id = call.parameters["id"] ?: ""
            commandBus.executeCommandAsync(MarkArticleAsReadCommand(id))
            call.respond(HttpStatusCode.OK)
        }
    }
}