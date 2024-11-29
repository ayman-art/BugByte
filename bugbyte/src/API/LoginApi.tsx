import { API_URLS } from './ApiUrls';

export const logIn = async (username: string, password: string): Promise<any> => {
  try {
    const response = await fetch(API_URLS.LOGIN, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
      throw new Error(`Login failed: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error in logIn:', error);
    throw error;
  }
};

export const resetPassword = async (username: string): Promise<any> => {
  try {
    const response = await fetch(
      `${API_URLS.RESET_PASSWORD}?username=${encodeURIComponent(username)}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      }
    );

    if (!response.ok) {
      throw new Error(`Password reset failed: ${response.statusText}`);
    }

    return await response.json();
  } catch (error) {
    console.error('Error in resetPassword:', error);
    throw error;
  }
};