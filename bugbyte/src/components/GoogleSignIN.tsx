import React from 'react';
import { GoogleLogin } from 'react-google-login';


const GoogleSignIN: React.FC = () => {
    const clientId = "YOUR_GOOGLE_CLIENT_ID";

    return (
        <GoogleLogin
            clientId={clientId}
            buttonText="Login with Google"
            cookiePolicy={'single_host_origin'}
        />
    );
};

export default GoogleSignIN;
