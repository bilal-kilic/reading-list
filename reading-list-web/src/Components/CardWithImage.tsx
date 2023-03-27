import {
    Box,
    Center, Grid, GridItem,
    Heading, HStack, IconButton,
    LinkBox,
    Stack,
    Text,
    useColorModeValue,
} from '@chakra-ui/react';
import {Article, instanceOfRedditArticle} from "../Model/RssArticleResponse";
import '../Extensions/DateExtensions'
import {ArrowUpIcon, CheckIcon, StarIcon} from "@chakra-ui/icons";
import {put} from "../Extensions/HttpUtils";

export function CardWithImage(props: CardProps) {
    const {article} = props
    const isRedditArticle = instanceOfRedditArticle(article)

    const markArticleAsRead = () => {
        const articleId = props.article.id
        put(`${process.env.REACT_APP_BACKEND_URL}/articles/${articleId}`)
        .then(() => {
                props.onMarkAsRead(articleId)
            }
        ).catch(e => {
            console.error(e)
        })
    };

    return (
        <LinkBox>
            <Center py={2}>
                <Box
                    maxW={'31vw'} // TODO configure this according to column count
                    minW={'31vw'}
                    w={'full'}
                    bg={useColorModeValue('white', 'gray.700')}
                    boxShadow={'2xl'}
                    rounded={'md'}
                    p={6}
                    overflow={'hidden'}
                    _hover={{
                        fontWeight: 'semibold',
                        boxShadow: '3xl',
                        transform: 'scale(1.03)'
                    }}
                >

                    <a href={article.url}>
                        <Stack>
                            {
                                article.imageUrl && <Box
                                    bg={'gray.100'}
                                    mt={-6}
                                    mx={-6}
                                    mb={1}
                                    pos={'relative'}
                                >
                                    <img src={article.imageUrl} alt={''}/>
                                </Box>
                            }
                            <Heading
                                color={useColorModeValue('gray.700', 'white')}
                                fontSize={'1xl'}
                                fontFamily={'body'}>
                                {article.title}
                            </Heading>
                            <Text color={'gray.500'} fontSize={"sm"} noOfLines={5}>
                                {article.description}
                            </Text>
                        </Stack>
                    </a>

                    <Grid templateColumns='repeat(8, 1fr)' marginBottom={-5}>
                        <GridItem>
                            <IconButton
                                marginLeft={'-12'}
                                variant="ghost"
                                color="current"
                                icon={<CheckIcon/>}
                                aria-label={`Mark as read`}
                                onClick={markArticleAsRead}
                            />
                        </GridItem>

                        <GridItem colStart={4} colEnd={7}>
                            <Text
                                marginTop={3}
                                fontSize={"xs"}
                                color={'gray.500'}
                                noOfLines={1}
                            >
                                {article.siteName && article.siteName + ' | '} {article.collectionDate.parseAsDate()}
                            </Text>
                        </GridItem>


                        {
                            isRedditArticle && <GridItem colStart={8}>
                                <a href={`https://www.reddit.com${article.redditUrl}`}>
                                    <HStack marginTop={2} marginRight={-3}>
                                        <Text fontSize={"xs"}>{article.commentCount}</Text>
                                        <StarIcon
                                            color="current"
                                            aria-label={`Number of comments`}
                                        />
                                        <Text fontSize={"xs"} marginRight={-2}>{article.upVotes} </Text>
                                        <ArrowUpIcon
                                            color="current"
                                            aria-label={`Number of upvotes`}
                                        />
                                    </HStack>
                                </a>
                            </GridItem>
                        }
                    </Grid>
                </Box>
            </Center>
        </LinkBox>
    );
}

interface CardProps {
    article: Article
    onMarkAsRead: (id: string) => void;
}