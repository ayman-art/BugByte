import React from 'react';
import RegistrationForm from '../components/Registration';
import { GoogleOAuthProvider } from '@react-oauth/google';
const SignUpPage: React.FC = () => {
    return (
        <GoogleOAuthProvider clientId="812505714822-hrnuq5v5os2k40atsk3bslf195l3fn8b.apps.googleusercontent.com">
            <RegistrationForm />
        </GoogleOAuthProvider>
      );
};

export default SignUpPage;
