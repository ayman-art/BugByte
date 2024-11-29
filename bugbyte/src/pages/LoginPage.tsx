import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import bugbyteLogo from '../assets/bugbyteLogo.png';
import GoogleLogo from '../assets/googlelogo.png';
import ResetPasswordPopup from './PasswordReset';
import '../styles/Login.css';
 import { logIn ,resetPassword} from '../API/LoginApi';

const Login: React.FC = () => {
  const [username, setUsername] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [showPopup, setShowPopup] = useState<boolean>(false);
  const navigate = useNavigate();

  const handleRegisterClick = () => {
    navigate('/SignUp');
  };

  const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setUsername(e.target.value);
    setError('');
  };

  const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPassword(e.target.value);
    setError('');
  };

  const handleForgotPasswordClick = async () => {
    if (!username) {
      setError('Email required to reset password.');
      return;
    }

    try {
      const data = await resetPassword(username);
      localStorage.setItem('userId', data.userId);
      setShowPopup(true);
    } catch (error) {
      console.error('Error during password reset:', error);
      if (error.message.includes('Password reset failed')) {
        setPassword('');
        setError('Wrong username or email!');
      } else {
        setError('Something went wrong. Please try again later.');
      }
    }
  };



 const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
   e.preventDefault();

   if (!username && !password) {
     setError('Both username and password are required.');
     return;
   }

   if (!password) {
     setError('Please enter your password');
     return;
   }

   if (!username) {
     setError('Please enter your username');
     return;
   }

   try {
     const data = await logIn(username, password);

     localStorage.setItem('authToken', data.token);
     console.log('Login successful:', data);
     // navigate('/home');
   } catch (error) {
     console.error('Error during login:', error);

     if (error.message.includes('Login failed')) {
       setPassword('');
       setError('Wrong username or password!');
     } else {
       setError('Something went wrong. Please try again later.');
     }
   }
 };


  return (
    <div className="container">
      <div className="formContainer">
        <img src={bugbyteLogo} alt="BugByte" className="logo" />
        <h1 className="title">BugByte</h1>
        <form onSubmit={handleSubmit} className="form">
          <div className="inputGroup">
            <label htmlFor="username" className="label">
              Username or email
            </label>
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
              Password
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
          <button type="submit" className="loginButton">
            Login
          </button>
        </form>
        <div className="orSection">
          <span>OR</span>
          <button className="googleButton">
            <img src={GoogleLogo} alt="Google logo" className="googleIcon" />
                    <div>
Log in with Google
        </div>

          </button>
        </div>
        <div className="registerSection">
          <div>Don’t have an account? </div>
          <span className="registerLink" onClick={handleRegisterClick}>
            Register now
          </span>
        </div>
        <div className="forgotPasswordSection">
          <span
            className="forgotPasswordLink"
            onClick={handleForgotPasswordClick}
          >
            Forgot Password?
          </span>
        </div>
      </div>

      {/* Reset Password Popup */}
      {showPopup && <ResetPasswordPopup setShowPopup={setShowPopup} />}
    </div>
  );
};

export default Login;
