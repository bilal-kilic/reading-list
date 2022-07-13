import {
    Button,
    FormControl, FormLabel,
    IconButton, Input,
    Modal, ModalBody,
    ModalCloseButton,
    ModalContent, ModalFooter,
    ModalHeader,
    ModalOverlay, Select,
    useDisclosure
} from "@chakra-ui/react";
import * as React from "react";
import {ArticleType, ArticleTypes} from "../Model/RssArticleResponse";
import {AddIcon} from "@chakra-ui/icons";
import {v4 as uuid} from 'uuid';
import {ArticleColumnDto} from "../Model/ArticleColumnDto";

export const ArticleColumnCreator = (props: ArticleRowCreatorProps) => {
    return (
        <InitialFocus/>
    )

    function InitialFocus() {
        const {isOpen, onOpen, onClose} = useDisclosure()
        const [articleTypeValue, setArticleTypeValue] = React.useState("ARTICLE" as ArticleType);
        const [columnName, setColumnName] = React.useState("")
        const [subreddit, setSubreddit] = React.useState("")
        const [isRead, setIsRead] = React.useState("false")

        const save = () => {
            props.createArticleColumn({
                type: articleTypeValue,
                columnName,
                subreddit,
                isRead: isRead === 'true',
                id: uuid()
            })
        };

        return (
            <>
                <IconButton
                    variant="ghost"
                    color="current"
                    marginLeft="2"
                    onClick={onOpen}
                    icon={<AddIcon/>}
                    justifySelf="flex-start"
                    aria-label={"Add new article column."}
                />

                <Modal
                    isOpen={isOpen}
                    onClose={onClose}
                >
                    <ModalOverlay/>
                    <ModalContent>
                        <ModalHeader>Add new article row</ModalHeader>
                        <ModalCloseButton/>
                        <ModalBody pb={6}>
                            <FormControl mt={4} isRequired={true}>
                                <FormLabel>Article Type</FormLabel>
                                <Select
                                    onChange={event => setArticleTypeValue(event.currentTarget.value as ArticleType)}
                                    value={articleTypeValue}
                                >
                                    {ArticleTypes.map((articleType) => {
                                        return <option value={articleType}>
                                            {articleType}
                                        </option>;
                                    })}
                                </Select>
                            </FormControl>

                            <FormControl mt={4} isRequired={true}>
                                <FormLabel>IsRead</FormLabel>
                                <Select onChange={event => setIsRead(event.currentTarget.value)}>
                                    <option value='false'>false</option>
                                    <option value='true'>true</option>
                                </Select>
                            </FormControl>

                            <FormControl mt={4} isRequired={true}>
                                <FormLabel>Column Name</FormLabel>
                                <Input onChange={event => setColumnName(event.currentTarget.value)}/>
                            </FormControl>

                            {articleTypeValue === "REDDIT" && <FormControl mt={4} isRequired={true}>
                                <FormLabel>Subreddit</FormLabel>
                                <Input onChange={event => setSubreddit(event.currentTarget.value)}/>
                            </FormControl>}
                        </ModalBody>

                        <ModalFooter>
                            <Button
                                colorScheme='blue'
                                mr={3}
                                onClick={save}
                            >
                                Save
                            </Button>
                            <Button onClick={onClose}>Cancel</Button>
                        </ModalFooter>
                    </ModalContent>
                </Modal>
            </>
        )
    }
}


interface ArticleRowCreatorProps {
    createArticleColumn(props: ArticleColumnDto): void;
}