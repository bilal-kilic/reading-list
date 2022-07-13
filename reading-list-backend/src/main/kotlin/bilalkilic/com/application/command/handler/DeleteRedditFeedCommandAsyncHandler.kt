package bilalkilic.com.application.command.handler

import bilalkilic.com.application.command.DeleteRedditFeedCommand
import bilalkilic.com.domain.collection.RedditFeedCollection
import bilalkilic.com.infastructure.persistance.get
import bilalkilic.com.infastructure.persistance.save
import com.couchbase.lite.Database
import com.trendyol.kediatr.AsyncCommandHandler

class DeleteRedditFeedCommandAsyncHandler(
    private val articleDatabase: Database,
) : AsyncCommandHandler<DeleteRedditFeedCommand> {

    override suspend fun handleAsync(command: DeleteRedditFeedCommand) {
        val redditFeedCollection = articleDatabase.get<RedditFeedCollection>(RedditFeedCollection.type) ?: RedditFeedCollection()
        redditFeedCollection.remove(command.id)
        articleDatabase.save(redditFeedCollection.documentType, redditFeedCollection)
    }
}