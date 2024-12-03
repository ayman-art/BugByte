import React from 'react';
import RegistrationForm from '../components/Registration';
import { GoogleOAuthProvider } from '@react-oauth/google';
interface signUPProps{
    onLogin: ()=>void
}
const SignUpPage: React.FC<signUPProps> = ({onLogin}) => {

    return (
        <GoogleOAuthProvider clientId="812505714822-hrnuq5v5os2k40atsk3bslf195l3fn8b.apps.googleusercontent.com">
            <RegistrationForm onLogin={onLogin} />
        </GoogleOAuthProvider>
      );
};

export default SignUpPage;
