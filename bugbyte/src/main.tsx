import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import 'bootstrap/dist/css/bootstrap.css'
import RegistrationForm from "./components/Registration.tsx";
import './styles/global.css'
import './styles/components.css'
import { GoogleOAuthProvider } from "@react-oauth/google";

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <GoogleOAuthProvider clientId="812505714822-hrnuq5v5os2k40atsk3bslf195l3fn8b.apps.googleusercontent.com">
            <RegistrationForm />
        </GoogleOAuthProvider>
    </StrictMode>
);
