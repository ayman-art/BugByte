import { API_URLS } from './ApiUrls';
export const fetchGoogleUserInfo = async (accessToken: string) => {
  try {
    const response = await fetch(API_URLS.LOGIN_BY_GOOGLE, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch user info from Google');
    }

    const userInfo = await response.json();
    return userInfo;
  } catch (error) {
    console.error('Error fetching Google user info:', error);
    throw new Error('Error fetching Google user info');
  }
};
