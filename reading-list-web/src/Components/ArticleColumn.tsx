import {Badge, Box, Flex, IconButton, Spacer, Text, VStack} from "@chakra-ui/react";
import {CardWithImage} from "./CardWithImage";
import * as React from "react";
import {get} from "../Extensions/HttpUtils";
import {Article, ArticleDto, ArticleType, PageResponse, RedditArticleDto} from "../Model/RssArticleResponse";
import {CloseIcon} from "@chakra-ui/icons";
import {ArticleColumnProps} from "./ArticleColumnProps";

export class ArticleColumn extends React.Component<ArticleColumnProps, ArticleColumnState> {

    public readonly state: Readonly<ArticleColumnState> = {
        currentPage: 0,
        totalElementCount: 0,
        articles: {} as ArticleDto[] | RedditArticleDto[],
        status: "Loading"
    }

    columnRemoved = () => {
        this.props.removeColumn(this.props.articleColumn.id)
    };

    componentDidMount() {
        async function fetchData(page: number = 0, pageSize: number = 20, isRead: boolean = false, articleType: ArticleType, subreddit: string) {
            switch (articleType) {
                case "ARTICLE":
                    return get<PageResponse<ArticleDto>>(`${process.env.BACKEND_URL}/articles?articleType=ARTICLE&isRead=${isRead}&page=${page}&pageSize=${pageSize}`)
                case "REDDIT":
                    return get<PageResponse<RedditArticleDto>>(`${process.env.BACKEND_URL}/articles/reddit?articleType=REDDIT&isRead=${isRead}&page=${page}&pageSize=${pageSize}&subreddit=${subreddit}`)
            }
        }

        const articleColumn = this.props.articleColumn

        fetchData(0, 30, articleColumn.isRead, articleColumn.type, articleColumn.subreddit).then(response => {
            this.setState({
                articles: response.data ?? [],
                status: 'Success',
                currentPage: response.page ?? 0,
                totalElementCount: response?.totalElementCount ?? 0,
            })
        }).catch(e => {
            console.error(e)
            this.setState({currentPage: 0, totalElementCount: 0, articles: [], status: "Failure"})
        })
    }

    render() {
        const {articles, status} = this.state
        const articleColumn = this.props.articleColumn


        const renderContent = () => {
            switch (status) {
                case "Loading":
                    return <Text>Loading</Text>;
                case "Success":
                    return <Box
                        maxW={'31vw'} // TODO configure this according to column count
                        minW={'31vw'}>
                        <Flex>
                            <Spacer></Spacer>
                            <Spacer></Spacer>
                            <Text fontSize='xl' fontWeight='bold'>
                                {articleColumn.columnName}
                                <Badge ml='1' fontSize='0.8em' colorScheme='green'>
                                    {articleColumn.type}
                                </Badge>
                                <Badge ml='1' fontSize='0.6em' colorScheme='cyan'>
                                    {this.state.totalElementCount}
                                </Badge>
                            </Text>
                            <Spacer></Spacer>
                            <Spacer></Spacer>
                            <IconButton
                                onClick={this.columnRemoved}
                                variant="ghost"
                                color="current"
                                aria-label={"Remove column"}
                                icon={<CloseIcon/>}
                            />
                        </Flex>
                        <VStack direction={"column"}>

                            {articles?.map(articleDto => {
                                return <CardWithImage
                                    article={articleDto}
                                    onMarkAsRead={(id: string) => {
                                        this.articleMarkedAsRead(id)
                                    }}
                                />
                            })}
                        </VStack>
                    </Box>
                case "Failure":
                    return <Text>There has been a problem!</Text>
            }
        }

        return renderContent()

    }

    private articleMarkedAsRead(id: string) {
        this.setState({
            status: "Success",
            articles: this.state.articles?.filter(x => x.id !== id),
            currentPage: this.state.currentPage,
            totalElementCount: this.state.totalElementCount,
        })
    }
}

export type LoadingStatus = 'Loading' | 'Success' | 'Failure'

interface ArticleColumnState {
    articles: Article[]
    status: LoadingStatus
    currentPage: number
    totalElementCount: number
}