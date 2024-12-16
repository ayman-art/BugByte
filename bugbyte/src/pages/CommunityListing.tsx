import React, { useState } from 'react';

import MDEditor from '../components/MDEditor';
import MDViewer from '../components/MDViewer';
import { Community } from '../Models/Community';

const CommunityListing: React.FC = () => {
        const [searchValue, setSearchValue] = useState('');
        const [tagValue, setTagValue] = useState('');
        const [communities, setCommunities] = useState<Community[]>([]);
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
                const fetchedQuestions = await sendRequest(searchValue, tagValue, "community",page,size);
                setCommunities(fetchedQuestions);
                //setTagValue("");
                //setSearchValue("");
                }
            } catch (error) {
                console.error("Error fetching communities:", error);
            }
        };

    return (
        <div>
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
        </div>
      );
};

export default CommunityListing;