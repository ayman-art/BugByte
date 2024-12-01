import { API_URLS } from './ApiUrls';
export const fetchGoogleUserInfo = async (accessToken: string) => {
  try {
    console.log('Access Token:', accessToken);

    const response = await fetch('https://www.googleapis.com/oauth2/v3/userinfo', {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      // Log the status and response to help diagnose the issue
      const errorResponse = await response.json();
      console.error('Error Response:', errorResponse);
      throw new Error(`Failed to fetch user info from Google. Status: ${response.status}`);
    }

    const userInfo = await response.json();
    console.log('Fetched User Info:', userInfo);
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

    if (response.status != 201) {
      throw new Error(`Sign up failed: ${response.statusText}`);
    }
    return await response.json();
  } catch (error) {
    console.error('Error in SignUp:', error);
    throw error;
  }
};
