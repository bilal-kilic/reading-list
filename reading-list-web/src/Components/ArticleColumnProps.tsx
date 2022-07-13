import {ArticleColumnDto} from "../Model/ArticleColumnDto";

export interface ArticleColumnProps {
    articleColumn: ArticleColumnDto
    removeColumn: (id: string) => void
}