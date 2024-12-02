import React, { useEffect, useState } from 'react';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './pages/LoginPage';
import SignUpPage from './pages/SignupPage';
import ProfilePage from './pages/ProfilePage';
import { authorizeToken, saveData } from './API/HomeAPI';

const isLoggedIn = false;

const App: React.FC = () => {
    const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

    useEffect(() => {
        const check = async () => {
          const token = localStorage.getItem('authToken');
          if (token) {
            try {
              const data = await authorizeToken(token);
              const { jwt } = data;
              localStorage.setItem('authToken', jwt);
              saveData(jwt)
              setIsAuthenticated(true); // Set to true when token is valid
            } catch (err) {
              localStorage.removeItem('authToken');
              setIsAuthenticated(false); // Set to false on error
              console.log(err);
            }
          } else {
            setIsAuthenticated(false); // No token, so not authenticated
          }
        };
        check();
      }, []);
      if (isAuthenticated === null) {
        return <div>Loading...</div>; // Loading screen or spinner while checking token
      }
    return (
        <BrowserRouter>
            <Routes>
                {isAuthenticated ? (
                <Route path="/" element={<HomePage />} />
                ) : (
                <Route path="/" element={<Navigate to="/SignUp" />} />
                )}
                <Route path="/SignUp" element={<SignUpPage />} />
                <Route path="/LogIn" element={<Login/>}/>
                <Route path= "/Profile/:userName" element={<ProfilePage/>}/>
                <Route path="/Home" element={<HomePage/>}/>

            </Routes>
        </BrowserRouter>
      );
};

export default App;
