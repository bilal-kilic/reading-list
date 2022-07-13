package bilalkilic.com.application.command.handler

import bilalkilic.com.application.command.DeleteRssFeedCommand
import bilalkilic.com.domain.collection.RssFeedCollection
import bilalkilic.com.infastructure.persistance.get
import bilalkilic.com.infastructure.persistance.save
import com.couchbase.lite.Database
import com.trendyol.kediatr.AsyncCommandHandler

class DeleteRssFeedCommandAsyncHandler(
    private val articleDatabase: Database,
) : AsyncCommandHandler<DeleteRssFeedCommand> {

    override suspend fun handleAsync(command: DeleteRssFeedCommand) {
        val rssFeedCollection = articleDatabase.get<RssFeedCollection>(RssFeedCollection.type) ?: RssFeedCollection()
        rssFeedCollection.remove(command.id)
        articleDatabase.save(rssFeedCollection.documentType, rssFeedCollection)
    }
}
