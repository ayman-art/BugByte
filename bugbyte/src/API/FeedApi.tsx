import { API_URLS } from './ApiUrls';

export const getFeed = async (token:string, page:number=0, size:number=10) => {
  try {
    const response = await fetch(`${API_URLS.GET_FEED}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
    });

    if (!response.ok) {
      console.error('Error while getting feed', response.statusText);
      return [];
    }

    const data = await response.json();
    return data['feed'];
  } catch (error) {
    console.error('Error fetching feed:', error);
    return [];
  }
};
