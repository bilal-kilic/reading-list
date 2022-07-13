package bilalkilic.com.application.command.handler

import bilalkilic.com.application.command.CreateRssFeedCommand
import bilalkilic.com.domain.collection.RssFeedCollection
import bilalkilic.com.infastructure.persistance.get
import bilalkilic.com.infastructure.persistance.save
import com.couchbase.lite.Database
import com.trendyol.kediatr.AsyncCommandHandler

class CreateRssFeedCommandAsyncHandler(
    private val articleDatabase: Database,
) : AsyncCommandHandler<CreateRssFeedCommand> {

    override suspend fun handleAsync(command: CreateRssFeedCommand) {
        val rssFeed = articleDatabase.get<RssFeedCollection>(RssFeedCollection.type) ?: RssFeedCollection()
        rssFeed.add(command.url)
        articleDatabase.save(rssFeed.documentType, rssFeed)
    }
}