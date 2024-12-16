import React, { useState } from 'react';
import SearchAndTagFields from '../components/SearchAndTagFields';
import CommunityPost from '../layouts/questionLayout';
import {sendRequest} from '../components/SearchAndTagFields';
import { Container } from 'lucide-react';
const HomePage: React.FC = () => {
    const [searchValue, setSearchValue] = useState('');
    const [tagValue, setTagValue] = useState('');

    const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setSearchValue(event.target.value);
    };

    const handleTagChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setTagValue(event.target.value);
    };

    const handleSearchClick = () => {
           sendRequest(searchValue,tagValue,"home");
           setTagValue("");
           setSearchValue("");
        };


    return (
        <div style={styles.container}>
            {/* Add the SearchAndTagFields component here */}
            <div style={styles.searchSection}>
                <SearchAndTagFields
                    searchValue={searchValue}
                    tagValue={tagValue}
                    onSearchChange={handleSearchChange}
                    onTagChange={handleTagChange}
                    onSearchClick={handleSearchClick}  // Pass the search handler
                />
            </div>

            {/* Display Community Posts */}
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
};

export default HomePage;
