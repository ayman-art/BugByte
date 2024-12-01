import { API_URLS } from './ApiUrls';
export const logIn = async (email: string, password: string): Promise<any> => {
  try {
    const response = await fetch(API_URLS.LOGIN, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
    });

    console.log('Request to API:', API_URLS.LOGIN);
    console.log('Request Body:', { email, password });

    if (!response.ok) {
      throw new Error(`Login failed: ${response.statusText}`);
    }

    const data = await response.json();


    console.log('Login Response:', data);

    return data;
  } catch (error) {
    console.error('Error in logIn:', error);
    throw error;
  }
};


export const resetPassword = async (email: string): Promise<any> => {
  try {
    const response = await fetch(API_URLS.FORGOT_PASSWORD, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email }),
    });

    if (!response.ok) {
      throw new Error(`Password reset failed: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error in resetPassword:', error);
    throw error;
  }
};