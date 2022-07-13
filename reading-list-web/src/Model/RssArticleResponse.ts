export interface ArticleDto {
    type: string;
    documentType: string;
    isRead: boolean;
    collectionDate: number;
    url: string;
    title: string;
    description: string;
    imageUrl: string;
    siteName: string;
    articleType: ArticleType;
    id: string;
}

export type Article = ArticleDto | RedditArticleDto

export interface RedditArticleDto {
    documentType: string;
    isRead: boolean;
    collectionDate: number;
    url: string;
    title: string;
    description: string;
    imageUrl: string;
    siteName: string;
    articleType: ArticleType;
    author: string;
    commentCount: number;
    downVotes: number;
    innerHtml?: any;
    redditUrl: string;
    subreddit: string;
    upVotes: number;
    clicked: boolean;
    id: string;
}


export interface PageResponse<T> {
    page: number;
    pageSize: number;
    totalElementCount: number;
    data: T[];
    totalPageCount: number;
}

export interface RssFeed {
    id: string;
    url: string
}

export interface RedditFeed {
    subredditName: string
    sorting: string
    id: string
}

export class AddRssFeedRequest {
    constructor(
        readonly url: string
    ) {
    }
}

export class AddRedditFeedRequest {
    constructor(readonly subredditName: string, readonly sortType: string) {
    }
}

export const ArticleTypes = ['ARTICLE', 'REDDIT'] as const

export type ArticleType = (typeof ArticleTypes)[number]

export const RedditSortingTypes = ['HOT', 'NEW', 'TOP'] as const

export type RedditSortingType = (typeof RedditSortingTypes)[number]


export function instanceOfRedditArticle(object: any): object is RedditArticleDto {
    return object.articleType === 'REDDIT'
}