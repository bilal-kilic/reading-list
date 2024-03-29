package bilalkilic.com.infastructure.routes

import bilalkilic.com.application.command.MarkArticleAsReadCommand
import bilalkilic.com.application.query.GetArticlesByPage
import bilalkilic.com.application.query.GetRedditArticlesByPage
import bilalkilic.com.domain.ArticleType
import bilalkilic.com.infastructure.plugins.inject
import bilalkilic.com.infastructure.routes.documentation.get
import bilalkilic.com.infastructure.routes.documentation.getArticlesByPageParams
import bilalkilic.com.infastructure.routes.documentation.getRedditArticlesByPageParams
import bilalkilic.com.infastructure.routes.documentation.markPostAsReadParams
import bilalkilic.com.infastructure.routes.documentation.put
import com.trendyol.kediatr.CommandBus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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

        route("reddit") {
            get("", getRedditArticlesByPageParams) {
                val page = call.parameters["page"]?.toInt() ?: 0
                val pageSize = call.parameters["pageSize"]?.toInt() ?: 20
                val isRead = call.parameters["isRead"]?.toBoolean()
                val subReddit = call.parameters["subreddit"]
                val articles = commandBus.executeQueryAsync(GetRedditArticlesByPage(page, pageSize, isRead, subReddit))
                call.respond(HttpStatusCode.OK, articles)
            }
        }
    }
}
