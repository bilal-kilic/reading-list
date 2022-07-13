import * as React from "react"
import {Box, ChakraProvider, Flex, Grid, theme,} from "@chakra-ui/react"
import {ColorModeSwitcher} from "./ColorModeSwitcher"
import {ArticleColumn} from "./Components/ArticleColumn";
import {ArticleColumnCreator} from "./Components/ArticleColumnCreator";
import {ArticleColumnDto} from "./Model/ArticleColumnDto";
import {CollectorSettings} from "./Components/CollectorSettings";

export class App extends React.Component {
    constructor(props: any) {
        super(props);
        this.state = {
            articleColumns: this.getExistingArticleRows()
        }
    }

    getExistingArticleRows() {
        const json = localStorage.getItem('articleColumns')
        return json != null ? JSON.parse(json) as ArticleColumnDto[] : [] as ArticleColumnDto[];
    }

    render() {
        const articles = this.getExistingArticleRows()

        const createColumn = (props: ArticleColumnDto) => {
            const articles = this.getExistingArticleRows()
            articles.push(props)
            localStorage.setItem('articleColumns', JSON.stringify(articles))
            this.setState(articles)
        };

        const removeColumn = (id: string) => {
            const articles = this.getExistingArticleRows()
            const filteredArticles = articles.filter(a => a.id !== id)
            localStorage.setItem('articleColumns', JSON.stringify(filteredArticles))
            this.setState(filteredArticles)
        };

        return <ChakraProvider theme={theme}>
            <Box textAlign="center" fontSize="xl">
                <Grid p={3} templateColumns='repeat(2, 1fr)'>
                    <ArticleColumnCreator createArticleColumn={(props) => createColumn(props)}></ArticleColumnCreator>
                    <Box justifySelf={"flex-end"}>
                        <ColorModeSwitcher justifySelf="flex-end"/>
                        <CollectorSettings></CollectorSettings>
                    </Box>
                </Grid>
                <Flex justifyContent={"space-around"}>
                    {articles.map(article => {
                        return <ArticleColumn articleColumn={article} removeColumn={(id) => removeColumn(id)}/>
                    })}

                </Flex>
            </Box>
        </ChakraProvider>;
    }
}

