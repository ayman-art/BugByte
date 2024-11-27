import React from 'react';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './pages/LoginPage';
import SignUpPage from './pages/SignupPage';
const isLoggedIn = false;
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
            </Routes>
        </BrowserRouter>
      );
};

export default App;
