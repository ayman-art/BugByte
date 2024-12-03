import React from 'react';
import { API_URLS } from '../API/ApiUrls';
import { GoogleOAuthProvider, GoogleLogin } from '@react-oauth/google';
import { useNavigate } from 'react-router-dom';
import { saveData } from '../API/HomeAPI';
interface googleButtonProps{
    onLogin: ()=>void
}
const GoogleLoginButton: React.FC<googleButtonProps> = ({onLogin}) => {
  const navigate = useNavigate();
  const clientId = '40038768890-7h07ab156ebvn2aubqjdiup7ss8l7e05.apps.googleusercontent.com';

  const  handleLoginSuccess = async (credentialResponse: any) => {
    const token = credentialResponse.credential;

    try {
      const response = await fetch(API_URLS.LOGIN_BY_GOOGLE, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ token }),
      });
      if (!response.ok) {
        throw new Error(`Login failed: ${response.statusText}`);
      }
  
      const data = await response.json();
  
      const { jwt, isAdmin } = data;
      if (jwt) {
        localStorage.setItem('authToken', jwt);
        saveData(jwt)
        onLogin()
        navigate('/');
      }
    } catch (error) {
      console.error('Error in logIn:', error);
      throw error;
    }
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



