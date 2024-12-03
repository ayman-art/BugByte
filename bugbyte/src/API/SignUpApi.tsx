import { API_URLS } from './ApiUrls';

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
