import { API_URLS } from './ApiUrls';
export const validateEmailCode = async (userId: string, verificationCode: string) => {
  try {
    const response = await fetch(API_URLS.VALIDATE_CODE, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userId,
        verificationCode,
      }),
    });

    return response;
  } catch (error) {
    console.error('Error during validateEmailCode API call:', error);
    throw new Error('An error occurred while validating the email code.');
  }
};

export const resetPassword = async (userId: string, newPassword: string) => {
  try {
    const response = await fetch(API_URLS.CHANGE_PASSWORD, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userId,
        newPassword,
      }),
    });

    return response;
  } catch (error) {
    console.error('Error during resetPassword API call:', error);
    throw new Error('An error occurred while resetting the password.');
  }
};

export const resendVerificationCode = async (userId: string) => {
  try {
    const response = await fetch(API_URLS.RESEND_CODE, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userId,
      }),
    });

    return response;
  } catch (error) {
    console.error('Error during resendVerificationCode API call:', error);
    throw new Error('An error occurred while resending the verification code.');
  }
};
