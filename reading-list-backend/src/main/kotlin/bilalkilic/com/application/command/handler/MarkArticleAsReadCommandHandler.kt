package bilalkilic.com.application.command.handler

import bilalkilic.com.application.command.MarkArticleAsReadCommand
import bilalkilic.com.infastructure.persistance.ArticleRepository
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
