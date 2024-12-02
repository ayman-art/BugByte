import React from 'react';
import { API_URLS } from '../API/ApiUrls';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';

const GoogleLoginButton: React.FC = () => {
  const clientId = '40038768890-7h07ab156ebvn2aubqjdiup7ss8l7e05.apps.googleusercontent.com'; // Replace with your Google Client ID

  const handleLoginSuccess = (credentialResponse: any) => {
    const token = credentialResponse.credential;
    console.log('Login Success! Token:', token);

    fetch(API_URLS.LOGIN_BY_GOOGLE, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ token }),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log('User Info from Backend:', data);
      })
      .catch((error) => {
        console.error('Error during token verification:', error);
      });
  };

  const handleLoginError = () => {
    console.error('Google Login Failed');
  };

  return (
    <GoogleOAuthProvider clientId={clientId}>
      <div>
        <GoogleLogin onSuccess={handleLoginSuccess} onError={handleLoginError} />
      </div>
    </GoogleOAuthProvider>
  );
};

export default GoogleLoginButton;



