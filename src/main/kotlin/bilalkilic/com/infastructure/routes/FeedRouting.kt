package bilalkilic.com.infastructure.routes

import bilalkilic.com.application.command.CreateRedditFeedCommand
import bilalkilic.com.application.command.CreateRssFeedCommand
import bilalkilic.com.application.query.GetAllRedditFeeds
import bilalkilic.com.application.query.GetAllRssFeeds
import bilalkilic.com.infastructure.plugins.inject
import bilalkilic.com.infastructure.routes.documentation.*
import com.trendyol.kediatr.CommandBus
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.feedRouting() {
    val commandBus by inject<CommandBus>()

    route("feeds") {
        route("rss") {
            post("", createRssFeed) {
                val command = call.receive<CreateRssFeedCommand>()
                commandBus.executeCommandAsync(command)
                call.respond(HttpStatusCode.OK)
            }

            get("all", getAllRssFeed) {
                val response = commandBus.executeQueryAsync(GetAllRssFeeds())
                call.respond(response)
            }
        }

        route("reddit") {
            post("", createRedditFeed) {
                val command = call.receive<CreateRedditFeedCommand>()
                commandBus.executeCommandAsync(command)
                call.respond(HttpStatusCode.OK)
            }

            get("all", getAllRedditFeed) {
                val response = commandBus.executeQueryAsync(GetAllRedditFeeds())
                call.respond(response)
            }
        }
    }
}
