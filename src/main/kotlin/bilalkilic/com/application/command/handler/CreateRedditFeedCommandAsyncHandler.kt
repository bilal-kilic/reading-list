package bilalkilic.com.application.command.handler

import bilalkilic.com.application.command.CreateRedditFeedCommand
import bilalkilic.com.domain.RedditFeedCollection
import bilalkilic.com.infastructure.persistance.get
import bilalkilic.com.infastructure.persistance.save
import com.couchbase.lite.Database
import com.trendyol.kediatr.AsyncCommandHandler

class CreateRedditFeedCommandAsyncHandler(
    private val articleDatabase: Database,
) : AsyncCommandHandler<CreateRedditFeedCommand> {

    override suspend fun handleAsync(command: CreateRedditFeedCommand) {
        val redditFeedCollection = articleDatabase.get<RedditFeedCollection>(RedditFeedCollection.type) ?: RedditFeedCollection()
        redditFeedCollection.add(command.subredditName)
        articleDatabase.save(redditFeedCollection.documentType, redditFeedCollection)
    }
}