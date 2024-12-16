import React from 'react';
import '../styles/SearchAndTagFields.css'; // Import the CSS file
import {searchCommunities , searchQuestions} from '../API/Search.tsx';
import {searchFilteredCommunities , searchFilteredQuestions} from '../API/Filter.tsx';

interface Community {
  id: number;
  name: string;
  description: string;
  adminId: number;
  creationDate: string;
  tags: string[];
}
interface SearchAndTagFieldsProps {
    searchValue: string;
    tagValue: string;
    onSearchChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
    onTagChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
    onSearchClick: () => void;
}
export const sendRequest = (searchValue: string, tagValue: string, source: string) => {
//     if(source=="home" && tagValue=="");
     //let c=  searchQuestions(searchValue,0,3);
//     else if (source=="home" && tagValue!="");
//        let c=  searchFilteredQuestions(tagValue,0,2);
//     else if (source=="community" && tagValue=="");
//         let c = searchCommunities(searchValue,0,3);
 //   else if (source=="community" && tagValue!="");
        let c = searchFilteredCommunities(tagValue,0,2);
};

const SearchAndTagFields: React.FC<SearchAndTagFieldsProps> = ({
    searchValue,
    tagValue,
    onSearchChange,
    onTagChange,
    onSearchClick,
}) => {
    return (
        <div className="">
            <input
                type="text"
                placeholder="Search..."
                value={searchValue}
                onChange={onSearchChange}
                className="searchField"
            />
            <input
                type="text"
                placeholder="Tag..."
                value={tagValue}
                onChange={onTagChange}
                className="tagField"
            />
            <button onClick={onSearchClick} className="searchButton">
                Search
            </button>
        </div>
    );
};

export default SearchAndTagFields;
