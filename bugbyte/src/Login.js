import React, { useState } from 'react';
import bugbyteLogo from './bugbyteLogo.png';
import Googlelogo from './googlelogo.png';
import { useNavigate } from 'react-router-dom';
import './Login.css';


const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate =useNavigate();
  const handleRegisterClick = () => {
    navigate.('./SignUp')
    console.log("go to sign up");
  };

  const handleUsernameChange = (e) => {
    setUsername(e.target.value);
    setError('');
  };

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
    setError('');
  };

  const handleForgotPasswordClick = () => {
    console.log("Redirect to password reset page");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (username === "" && !password) {
      setError('Both username and password are required.');
      return;
    }
    if (!password) {
      setError('Please enter your password');
      return;
    }
    if (username === "") {
      setError('Please enter your username');
      return;
    }
    //just to test
    setPassword('');
    setError('Wrong username or password!');

    const response = await fetch('https://users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ username, password }),
    });

    if (response.ok) {
      const data = await response.json();
      localStorage.setItem('authToken', data.token);
      console.log('Login successful:', data);
      navigate.('/home');
    } else {
      console.error('Login failed');
      setPassword('');
      setError('Wrong username or password!');
    }
  };

  return (
    <div className="container">
      <div className="formContainer">
        <img src={bugbyteLogo} alt="BugByte" className="logo" />
        <h1 className="title">BugByte</h1>
        <form onSubmit={handleSubmit} className="form">
          <div className="inputGroup">
            <label htmlFor="username" className="label">Username or email</label>
            <input
              id="username"
              type="text"
              placeholder="Enter username or email"
              value={username}
              onChange={handleUsernameChange}
              className="input"
            />
          </div>
          <div className="inputGroup">
            <label htmlFor="password" className="label">
                      <div style={{ transform: 'translateX(25%)' }}>Password</div>
            </label>
            <input
              id="password"
              type="password"
              placeholder="Enter password"
              value={password}
              onChange={handlePasswordChange}
              className="input"
            />
          </div>
          {error && <div className="errorMessage">{error}</div>}
          <button type="submit" className="loginButton">Login</button>
        </form>
        <div className="orSection">
          <span>OR</span>
          <button className="googleButton">
            <img src={Googlelogo} alt="Google logo" className="googleIcon" />
            Log in with Google
          </button>
        </div>
        <div className="registerSection">
          <div>Don’t have an account? </div>
          <span className="registerLink" onClick={handleRegisterClick}>Register now</span>
        </div>
        <div className="forgotPasswordSection">
           <span className="forgotPasswordLink" onClick={handleForgotPasswordClick}>Forgot Password?</span>
        </div>
      </div>
    </div>
  );
};

export default Login;
