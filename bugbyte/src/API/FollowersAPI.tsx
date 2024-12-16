import { API_URLS } from './ApiUrls';

export const getFollowers = async (username: string, token:string): Promise<any[]> => {
  try {
    const response = await fetch(`${API_URLS.GET_FOLLOWERS}?username=${username}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
    });

    if (!response.ok) {
      console.error('Error fetching followers:', response.statusText);
      return [];
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching followers:', error);
    return [];
  }
};
