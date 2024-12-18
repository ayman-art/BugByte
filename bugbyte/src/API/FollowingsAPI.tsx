import { API_URLS } from './ApiUrls';

export const getFollowings = async (username: string, token: string): Promise<any[]> => {
  try {
    const response = await fetch(`${API_URLS.GET_FOLLOWINGS}?username=${encodeURIComponent(username)}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
    });

    if (!response.ok) {
      console.error('Error fetching followings:', response.statusText);
      return [];
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Error fetching followings:', error);
    return [];
  }
};
