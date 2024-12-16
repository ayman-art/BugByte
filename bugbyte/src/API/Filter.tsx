import { API_URLS } from './ApiUrls';
import { Question } from '../Models/Question';
import { Community } from '../Models/Community';

const getAuthToken = (): string => {
  const authToken = localStorage.getItem('authToken');
  if (!authToken) {
    console.error('No auth token found in localStorage.');
    throw new Error('Unauthorized: Missing token');
  }
  return authToken;
};

const parseTags = (tags: string): string[] => {
  return tags
    .split('#')
    .filter((tag) => tag.trim() !== '');
};

export const searchFilteredQuestions = async (
  tags: string,
  page: number,
  size: number
): Promise<Question[]> => {
  try {
    const authToken = getAuthToken();
    const tagsList = parseTags(tags);

    const tagsQuery = tagsList.map((tag) => `tags=${encodeURIComponent(tag)}`).join('&');
    const url = `${API_URLS.SEARCH_FILTERED_QUESTIONS}?${tagsQuery}&page=${page}&size=${size}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${authToken}`,
      },
    });

    if (!response.ok) {
      console.error(`Error fetching questions: ${response.status} - ${response.statusText}`);
      throw new Error(`HTTP Error ${response.status}`);
    }

    const data = await response.json();
    console.log('Fetched questions:', data.questions);
    return data.questions;
  } catch (error) {
    console.error('Error in searchFilteredQuestions:', error);
    return [];
  }
};

export const searchFilteredCommunities = async (
  tags: string,
  page: number,
  size: number
): Promise<Community[]> => {
  try {
    const authToken = getAuthToken();
    const tagsList = parseTags(tags);

    const tagsQuery = tagsList.map((tag) => `tags=${encodeURIComponent(tag)}`).join('&');
    const url = `${API_URLS.SEARCH_FILTERED_COMMUNITIES}?${tagsQuery}&page=${page}&size=${size}`;

    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${authToken}`,
      },
    });

    if (!response.ok) {
      console.error(`Error fetching communities: ${response.status} - ${response.statusText}`);
      throw new Error(`HTTP Error ${response.status}`);
    }

    const data = await response.json();
    console.log('Fetched communities:', data.communities);
    return data.communities;
  } catch (error) {
    console.error('Error in searchFilteredCommunities:', error);
    return [];
  }
};

