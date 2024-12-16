import React from 'react';
import '../styles/SearchAndTagFields.css';
import {searchCommunities , searchQuestions} from '../API/Search.tsx';
import {searchFilteredCommunities , searchFilteredQuestions} from '../API/Filter.tsx';
import { Question } from '../Models/Question';
import { Community } from '../Models/Community';

interface SearchAndTagFieldsProps {
    searchValue: string;
    tagValue: string;
    onSearchChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
    onTagChange: (event: React.ChangeEvent<HTMLInputElement>) => void;
    onSearchClick: () => void;
}
export const sendRequest = (searchValue: string, tagValue: string, source: string,page:number,size:number) => {
    if (source === "home" && tagValue === "") {
        return searchQuestions(searchValue, page,size);
    } else if (source === "home" && tagValue !== "") {
        return searchFilteredQuestions(tagValue, page, size);
    } else if (source === "community" && tagValue === "") {
        return searchCommunities(searchValue, page, size);
    } else if (source === "community" && tagValue !== "") {
        return searchFilteredCommunities(tagValue, page, size);
    }
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
