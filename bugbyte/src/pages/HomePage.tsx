import React, { useState, useEffect } from 'react';
import SearchAndTagFields from '../components/SearchAndTagFields';
import CommunityPost from '../components/QuestionPreview.tsx';
import { sendRequest } from '../components/SearchAndTagFields';
import { Question } from '../Models/Question';
import { Container } from 'lucide-react';

const HomePage: React.FC = () => {
    const [searchValue, setSearchValue] = useState('');
    const [tagValue, setTagValue] = useState('');
    const [questions, setQuestions] = useState<Question[]>([]);
    const page=0;
    const size =10;

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchValue(event.target.value);
    };

    const handleTagChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setTagValue(event.target.value);
    };

    const handleSearchClick = async () => {
        try {
            if(!(searchValue==="" && tagValue==="")){
            const fetchedQuestions = await sendRequest(searchValue, tagValue, "home",page,size);
            setQuestions(fetchedQuestions);
            console.log(fetchedQuestions);
            //setTagValue("");
            //setSearchValue("");
            }
        } catch (error) {
            console.error("Error fetching questions:", error);
        }
    };

    return (
        <div style={styles.container}>
            {/* Search and Tag Fields */}
            <div style={styles.searchSection}>
                <SearchAndTagFields
                    searchValue={searchValue}
                    tagValue={tagValue}
                    onSearchChange={handleSearchChange}
                    onTagChange={handleTagChange}
                    onSearchClick={handleSearchClick}
                />
            </div>

            {/* Community Posts */}
            <div>
                <CommunityPost
                    postId="123"
                    communityName="depressed programmers"
                    authorName="Ali Al jayyar"
                    content="asking questions"
                    upvotes={150}
                    downvotes={30}
                />
            </div>

            <div>
                <CommunityPost
                    postId="125"
                    communityName="depressed lawyers searching for a career shift"
                    authorName="Saul Goodman"
                    content="asking another question"
                    upvotes={1000}
                    downvotes={0}
                />
            </div>

            <div>
                <CommunityPost
                    postId="126"
                    communityName="new mexico community"
                    authorName="Walter White"
                    content="just ask"
                    upvotes={0}
                    downvotes={0}
                />
            </div>

            {/* Render the list of questions */}
            <div>
                <h2>Questions</h2>
                {questions.length > 0 ? (
                    questions.map((question) => (
                        <div key={question.id} style={styles.questionCard}>
                            <h3>{question.title}</h3>
                            <p>{question.mdContent}</p>
                            <div>
                                <strong>Tags:</strong>
                                <ul>
                                    {question.tags.map((tag, index) => (
                                        <li key={index}>{tag}</li>
                                    ))}
                                </ul>
                            </div>
                            <p>Posted by: {question.creatorUserName}</p>
                            <p>Posted on: {new Date(question.postedOn).toLocaleDateString()}</p>
                            <p>Upvotes: {question.upVotes} | Downvotes: {question.downVotes}</p>
                        </div>
                    ))
                ) : (
                    <p>No questions found.</p>
                )}
            </div>
        </div>
    );
};

// Styles for the page
const styles: { [key: string]: React.CSSProperties } = {
    container: {
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        marginTop: '20px',
    },
    searchSection: {
        marginBottom: '20px',
        display: 'flex',
        justifyContent: 'center',
        width: '100%',
    },
    questionCard: {
        border: '1px solid #ccc',
        padding: '10px',
        margin: '10px',
        borderRadius: '5px',
        width: '80%',
        boxSizing: 'border-box',
    },
};

export default HomePage;
