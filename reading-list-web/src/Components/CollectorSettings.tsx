import {
    Box, FormLabel, HStack,
    IconButton, Input,
    Modal,
    ModalBody,
    ModalCloseButton,
    ModalContent,
    ModalHeader,
    ModalOverlay, Select, Stack, Tab,
    Table,
    TableContainer, TabList, TabPanel, TabPanels, Tabs,
    Tbody, Td,
    Th,
    Thead,
    Tr,
    useDisclosure
} from "@chakra-ui/react";
import * as React from "react";
import {
    AddRedditFeedRequest,
    AddRssFeedRequest,
    ArticleType,
    ArticleTypes,
    RedditFeed, RedditSortingType,
    RedditSortingTypes,
    RssFeed
} from "../Model/RssArticleResponse";
import {CheckIcon, SettingsIcon, SmallCloseIcon} from "@chakra-ui/icons";
import {get, fDelete, post} from "../Extensions/HttpUtils";

export function CollectorSettings() {

    const {isOpen, onOpen, onClose} = useDisclosure()
    const [newRssColumnUrl, setNewRssColumnUrl] = React.useState("")
    const [rssFeeds, setRssFeeds] = React.useState([] as RssFeed[])
    const [redditFeeds, setRedditFeeds] = React.useState([] as RedditFeed[])
    const [redditSortingType, setRedditSortingType] = React.useState("HOT" as RedditSortingType);
    const [newSubredditName, setNewSubredditName] = React.useState("");

    const openModal = () => {
        async function fetchRssFeeds() {
            return await get<RssFeed[]>(`${process.env.BACKEND_URL}/feeds/rss/all`)
        }

        async function fetchRedditFeeds() {
            return await get<RedditFeed[]>(`${process.env.BACKEND_URL}/feeds/reddit/all`)
        }

        fetchRssFeeds().then(response => {
            setRssFeeds(response ?? [])
        }).catch(e => {
            console.error(e)
        })

        fetchRedditFeeds().then(response => {
            setRedditFeeds(response ?? [])
        }).catch(e => {
            console.error(e)
        })

        onOpen()
    }

    const addRssFeed = (newRssColumnUrl: string) => {
        post(`${process.env.BACKEND_URL}/feeds/rss`, new AddRssFeedRequest(newRssColumnUrl), {
            headers: {
                'content-type': 'application/json',
            },
            mode: "cors"
        })
        .then(() => {
                const copy = rssFeeds.slice()
                copy.push({id: "", url: newRssColumnUrl})
                setRssFeeds(copy)
            }
        ).catch(e => {
            console.error(e)
        })
    };

    const deleteRssFeed = (rssFeedId: string) => {
        fDelete(`${process.env.BACKEND_URL}/feeds/rss/${rssFeedId}`)
        .then(() => {
                setRssFeeds(rssFeeds.filter(rf => rf.id !== rssFeedId))
            }
        ).catch(e => {
            console.error(e)
        })
    };

    function addNewRedditFeed() {
        post(`${process.env.BACKEND_URL}/feeds/reddit`, new AddRedditFeedRequest(newSubredditName, redditSortingType), {
            headers: {
                'content-type': 'application/json',
            },
            mode: "cors"
        })
        .then(() => {
                const copy = redditFeeds.slice()
                copy.push({id: "", subredditName: newSubredditName, sorting: setRedditSortingType.name})
                setRedditFeeds(copy)
            }
        ).catch(e => {
            console.error(e)
        })

    }

    function deleteRedditFeed(id: string) {
        fDelete(`${process.env.BACKEND_URL}/feeds/reddit/${id}`)
        .then(() => {
                setRssFeeds(rssFeeds.filter(rf => rf.id !== id))
            }
        ).catch(e => {
            console.error(e)
        })
    }

    return (
        <>
            <IconButton
                variant="ghost"
                color="current"
                marginLeft="2"
                onClick={openModal}
                icon={<SettingsIcon/>}
                justifySelf="flex-start"
                aria-label={"Configure Collectors."}
            />

            <Modal
                isOpen={isOpen}
                onClose={onClose}
                size={"4xl"}
            >
                <ModalOverlay/>
                <ModalContent>
                    <ModalHeader>Configure Collectors</ModalHeader>
                    <ModalCloseButton/>
                    <ModalBody pb={6}>

                        <Tabs isFitted variant='enclosed'>
                            <TabList mb='1em'>
                                <Tab>Rss Feeds</Tab>
                                <Tab>Reddit Feeds</Tab>
                            </TabList>
                            <TabPanels>
                                <TabPanel>
                                    <TableContainer>
                                        <Box marginBottom={4}>
                                            <Input
                                                onChange={event => setNewRssColumnUrl(event.target.value)}
                                                width={"93%"}
                                                id='name'
                                                placeholder='Enter a new rss feed'
                                            />
                                            <IconButton
                                                marginBottom={1}
                                                marginLeft={3}
                                                aria-label={'Save'}
                                                colorScheme='green'
                                                onClick={() => {
                                                    addRssFeed(newRssColumnUrl);
                                                }}
                                                icon={<CheckIcon/>}>
                                            </IconButton>
                                        </Box>
                                        <Table size='sm'>
                                            <Thead>
                                                <Tr>
                                                    <Th>Rss Feed</Th>
                                                    <Th></Th>
                                                </Tr>
                                            </Thead>
                                            <Tbody>
                                                {rssFeeds?.map(rf => {
                                                    return <Tr>
                                                        <Td>
                                                            {rf.url}
                                                        </Td>
                                                        <IconButton
                                                            onClick={() => deleteRssFeed(rf.id)}
                                                            marginTop={3}
                                                            variant={"link"}
                                                            aria-label={'Save'}
                                                            icon={<SmallCloseIcon/>}
                                                        >
                                                        </IconButton>
                                                    </Tr>
                                                })}

                                            </Tbody>
                                        </Table>
                                    </TableContainer>
                                </TabPanel>
                                <TabPanel>
                                    <TableContainer>
                                        <HStack marginBottom={4}>
                                            <Input
                                                onChange={event => setNewSubredditName(event.target.value)}
                                                width={"50%"}
                                                id='name'
                                                placeholder='Enter Subreddit Name'
                                            />
                                            <Select
                                                width={"15%"}
                                                onChange={event => setRedditSortingType(event.currentTarget.value as RedditSortingType)}
                                                value={redditSortingType}
                                            >
                                                {RedditSortingTypes.map((articleType) => {
                                                    return <option value={articleType}>
                                                        {articleType}
                                                    </option>;
                                                })}
                                            </Select>
                                            <Box width={"30%"}></Box>
                                            <IconButton
                                                marginBottom={1}
                                                marginLeft={3}
                                                aria-label={'Save'}
                                                colorScheme='green'
                                                onClick={() => {
                                                    addNewRedditFeed();
                                                }}
                                                icon={<CheckIcon/>}>
                                            </IconButton>
                                        </HStack>
                                        <Table size='sm'>
                                            <Thead>
                                                <Tr>
                                                    <Th>Subreddit Name</Th>
                                                    <Th>Sorting</Th>
                                                    <Th></Th>
                                                </Tr>
                                            </Thead>
                                            <Tbody>
                                                {redditFeeds?.map(rf => {
                                                    return <Tr>
                                                        <Td>
                                                            {rf.subredditName}
                                                        </Td>
                                                        <Td>
                                                            {rf.sorting}
                                                        </Td>
                                                        <IconButton
                                                            onClick={() => deleteRedditFeed(rf.id)}
                                                            marginTop={3}
                                                            variant={"link"}
                                                            aria-label={'Save'}
                                                            icon={<SmallCloseIcon/>}
                                                        >
                                                        </IconButton>
                                                    </Tr>
                                                })}

                                            </Tbody>
                                        </Table>
                                    </TableContainer>
                                </TabPanel>
                            </TabPanels>
                        </Tabs>
                    </ModalBody>
                </ModalContent>
            </Modal>
        </>
    )
}
