import React from 'react';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './pages/LoginPage';
import SignUpPage from './pages/SignupPage';
import ProfilePage from './pages/ProfilePage';
const isLoggedIn = true;

const App: React.FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                {isLoggedIn ? (
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
