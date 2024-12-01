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

export const Signup = async (userName: string,email: string, password: string): Promise<any> => {
  try {
    const response = await fetch(API_URLS.SIGNUP, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ userName,email, password }),
    });

    if (!response.ok) {
      throw new Error(`Login failed: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error in SignUp:', error);
    throw error;
  }
};
