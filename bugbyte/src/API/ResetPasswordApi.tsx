import { API_URLS } from './ApiUrls';
export const validateEmailCode = async (email: string, code: string) : Promise<any>  => {
  try {
    const response = await fetch(API_URLS.VALIDATE_CODE, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email,
        code,
      }),
    });

    return response;
  } catch (error) {
    console.error('Error during validateEmailCode API call:', error);
    throw new Error('An error occurred while validating the email code.');
  }
};

export const resetPassword = async (jwt: string, password: string) => {
  try {
    const response = await fetch(API_URLS.CHANGE_PASSWORD, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        jwt,
        password,
      }),
    });

    return response;
  } catch (error) {
    console.error('Error during resetPassword API call:', error);
    throw new Error('An error occurred while resetting the password.');
  }
};

export const resendVerificationCode = async (email: string) => {
  try {
    const response = await fetch(API_URLS.FORGOT_PASSWORD, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        email,
      }),
    });

    return response;
  } catch (error) {
    console.error('Error during resendVerificationCode API call:', error);
    throw new Error('An error occurred while resending the verification code.');
  }
};
