import React from 'react';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
import { fetchGoogleUserInfo } from '../API/SignUpApi';


const GoogleSignIN: React.FC = () => {
    const clientId = "40038768890-7h07ab156ebvn2aubqjdiup7ss8l7e05.apps.googleusercontent.com";
    const handleLoginSuccess = async (credentialResponse: any) => {
      console.log('Login Success:', credentialResponse);
      const token = credentialResponse.credential; 
  
      try {
        
        const userInfo = await fetchGoogleUserInfo(token);
  

        console.log('Google User Info:', userInfo);
  

      } catch (error) {
        console.error('Error fetching Google user info:', error);
      }
    };
  
    const handleLoginError = () => {
      console.error('Login Failed');
    };
  
    return (
      <GoogleOAuthProvider clientId={clientId}>
        <GoogleLogin
          onSuccess={handleLoginSuccess}
          onError={handleLoginError}
        />
      </GoogleOAuthProvider>
    );
  };


export default GoogleSignIN;


