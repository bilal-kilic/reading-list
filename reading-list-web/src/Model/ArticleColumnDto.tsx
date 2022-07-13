import {ArticleType} from "./RssArticleResponse";

export interface ArticleColumnDto {
    id: string
    type: ArticleType
    columnName: string
    subreddit: string
    isRead: boolean
}

