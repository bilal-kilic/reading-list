package bilalkilic.com.application.command.handler

import bilalkilic.com.application.command.MarkArticleAsReadCommand
import bilalkilic.com.domain.BaseArticle
import bilalkilic.com.infastructure.persistance.ArticleRepository
import bilalkilic.com.infastructure.persistance.get
import bilalkilic.com.infastructure.persistance.save
import com.couchbase.lite.Database
import com.trendyol.kediatr.AsyncCommandHandler

class MarkArticleAsReadCommandHandler(
    private val articleRepository: ArticleRepository,
) : AsyncCommandHandler<MarkArticleAsReadCommand> {
    override suspend fun handleAsync(command: MarkArticleAsReadCommand) {
        val article = articleRepository.get(command.id) ?: throw Exception("Article not found")
        article.markAsRead()
        articleRepository.save(article)
    }
}