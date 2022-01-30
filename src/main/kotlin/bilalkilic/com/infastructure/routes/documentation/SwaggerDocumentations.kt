package bilalkilic.com.infastructure.routes.documentation

import bilalkilic.com.application.command.CreateRedditFeedCommand
import bilalkilic.com.application.command.CreateRssFeedCommand
import bilalkilic.com.application.query.model.ArticlePageResponse
import bilalkilic.com.domain.*
import io.bkbn.kompendium.annotations.KompendiumParam
import io.bkbn.kompendium.annotations.ParamType
import io.bkbn.kompendium.models.meta.MethodInfo
import io.bkbn.kompendium.models.meta.RequestInfo
import io.bkbn.kompendium.models.meta.ResponseInfo
import io.ktor.http.*

val createRssFeed = MethodInfo.PostInfo<Unit, CreateRssFeedCommand, Unit>(
    summary = "Feeds",
    tags = setOf("feeds"),
    responseInfo = ResponseInfo(
        status = HttpStatusCode.Accepted,
        ""
    ),
    requestInfo = RequestInfo(
        description = "",
        examples = mapOf(
            "Example" to CreateRssFeedCommand(
                url = "test",
            )
        )
    ),
)

val createRedditFeed = MethodInfo.PostInfo<Unit, CreateRedditFeedCommand, Unit>(
    summary = "Feeds",
    tags = setOf("feeds"),
    responseInfo = ResponseInfo(
        status = HttpStatusCode.Accepted,
        ""
    ),
    requestInfo = RequestInfo(
        description = "",
        examples = mapOf(
            "Example" to CreateRedditFeedCommand(
                subredditName = "programming",
                sortType = SortType.HOT
            )
        )
    ),
)

val markPostAsReadParams = MethodInfo.PutInfo<MarkArticleAsReadParams, Unit, Unit>(
    summary = "Mark article as read",
    tags = setOf("articles"),
    responseInfo = ResponseInfo(
        status = HttpStatusCode.Accepted,
        ""
    )
)

val getArticlesByPageParams = MethodInfo.GetInfo<GetArticlesByPageParams, ArticlePageResponse>(
    summary = "Get articles by page",
    tags = setOf("articles"),
    responseInfo = ResponseInfo(
        status = HttpStatusCode.OK,
        "",
        examples = mapOf("Example" to ArticlePageResponse(0, 0, 0, listOf()))
    )
)

val getAllRssFeed = MethodInfo.GetInfo<Unit, Collection<RssFeedCollection.RssFeed>>(
    summary = "Get all rss feeds",
    tags = setOf("feeds"),
    responseInfo = ResponseInfo(
        status = HttpStatusCode.OK,
        description = "",
        examples = mapOf("Example" to listOf(RssFeedCollection.RssFeed("test")))
    ),
)

val getAllRedditFeed = MethodInfo.GetInfo<Unit, Collection<RedditFeedCollection.RedditFeed>>(
    summary = "Get all reddit feeds",
    tags = setOf("feeds"),
    responseInfo = ResponseInfo(
        status = HttpStatusCode.OK,
        description = "",
        examples = mapOf("Example" to listOf(RedditFeedCollection.RedditFeed("test")))
    ),
)

data class GetArticlesByPageParams(
    @KompendiumParam(ParamType.QUERY) val page: Int,
    @KompendiumParam(ParamType.QUERY) val pageSize: Int,
    @KompendiumParam(ParamType.QUERY) val isRead: Boolean,
    @KompendiumParam(ParamType.QUERY) val articleType: ArticleType,
)

data class MarkArticleAsReadParams(
    @KompendiumParam(ParamType.PATH) val id: String,
)