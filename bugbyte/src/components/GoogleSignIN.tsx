import React from 'react';
import { API_URLS } from '../API/ApiUrls';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
import { useNavigate } from 'react-router-dom';

const GoogleLoginButton: React.FC = () => {
  const navigate = useNavigate();
  const clientId = '40038768890-7h07ab156ebvn2aubqjdiup7ss8l7e05.apps.googleusercontent.com';

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
        const { jwt, isAdmin } = data;
        if (jwt) {
          localStorage.setItem('authToken', jwt);
          console.log('JWT Token:', jwt);
          console.log('Is Admin:', isAdmin);
          navigate('/home');
          
        }
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



