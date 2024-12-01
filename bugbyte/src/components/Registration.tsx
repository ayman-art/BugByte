import React, {useEffect, useState} from 'react';
import { User } from '../types';
import { useTypewriter } from 'react-simple-typewriter';
import { useGoogleLogin } from '@react-oauth/google';
import { useNavigate } from 'react-router-dom';
import '../styles/components.css';
import { fetchGoogleUserInfo, Signup } from '../API/SignUpApi';
import GoogleSignIN from './GoogleSignIN';

const RegistrationForm: React.FC = () => {
    const [formData, setFormData] = useState<User>({
        username: '',
        email: '',
        password: ''
    });
    const [error, setError] = useState<string>('');
    const navigate = useNavigate();

    // Handle Google login
    const googleLogin = useGoogleLogin({
      onSuccess: async (response) => {
          try {
              const userInfo = await fetchGoogleUserInfo(response.access_token);
              console.log('Google user info:', userInfo);
  
              const { email, name } = userInfo;
              console.log(email)
              console.log(name)
              try {
                  const signupData = await Signup(name, email, 'Password12#!'); 
  
                  console.log('SignUp successful:', signupData);
  
                  // Store JWT Token
                  const { jwt, isAdmin } = signupData;
                  if (jwt) {
                      localStorage.setItem('authToken', jwt);
                      console.log('JWT Token:', jwt);
                      console.log('Is Admin:', isAdmin);
                  } else {
                      setError('No token received. Please try again.');
                  }
                  navigate('/home');
  
              } catch (signupError) {
                  console.error('Error during signup:', signupError);
                  setError('Error during Google sign-up.');
              }
  
          } catch (error) {
              console.error('Error fetching Google user info:', error);
              setError('Google Sign-In failed.');
          }
      },
      onError: () => {
          console.error('Google Login Failed');
          setError('Google Login Failed');
      },
  });
  

    // const handleSubmit = (e: FormEvent) => {
    //     e.preventDefault();
    //     console.log('Registering user:', formData);
    // };
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
     
        if (!formData.username && !formData.password) {
          setError('Both username and password are required.');
          return;
        }
     
        if (!formData.password) {
          setError('Please enter your password');
          return;
        }
     
        if (!formData.username) {
          setError('Please enter your username');
          return;
        }
        if(formData.password.length < 8){
          setError('week password');
          return;
        }
        if(formData.password.length < 8){
          setError('week password');
          return;
        }
        const specialCharacterRegex = /[!@#$%^&*(),.?":{}|<>]/;
        if (!specialCharacterRegex.test(formData.password)) {
          setError('Password must contain at least one special character.');
          return;
        }
        if(formData.username.length < 5){
          setError('username is too short');
          return;
        }
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        if (!emailRegex.test(formData.email)) {
          setError('Email is not valid');
          return;
        }
        
        if (formData.email.split('@').length - 1 !== 1) {
          setError('Email is not valid');
          return;
        }
     
     
        try {
          const data = await Signup(formData.username,formData.email, formData.password);
     
          console.log('Login response:', data); 
 
          const { jwt, isAdmin } = data;
     
          if (jwt) {
            localStorage.setItem('authToken', jwt);
            console.log('JWT Token:', jwt);
            console.log('Is Admin:', isAdmin);
          } else {
            setError('No token received. Please try again.');
          }
          console.log('SignUp successful:', data);
          navigate('/home');
          
        } catch (error: unknown) {
         console.error('Username or Email Already Exist', error);
     
         if (error instanceof Error) {
             setFormData;
             setError(error.message);
         } else {
           setError('An unexpected error occurred.');
         }
        }
      };

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const [text] = useTypewriter({
        words: [
            "We're excited to have you join our community! This is a space where you can ask questions, share your knowledge, connect with like-minded individuals, and explore content tailored to your interests."],
        loop: true,
        delaySpeed: 50
    });

    return (
        <div className="min-h-screen bg-green-600 flex items-center justify-center"
             style={{backgroundColor: '#299C71'}}>
            <label className='registration-form-Title1'>
                Welcome To BugByte
            </label>

            <img src="src/assets/logo.png" alt="logo" className='registration-form-logo'/>

            <label className='registration-form-desc'>
                {text}
            </label>
            <div className="registration-form">
                <h2 className="text-2xl font-bold mb-6 text-center " style={{paddingTop: 120}}>Create Account</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="username" className="block text-sm font-medium text-gray-700 "
                               style={{paddingLeft: 70, paddingTop: 50}}>
                            User Name
                        </label>
                        <br/>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            value={formData.username}
                            onChange={handleInputChange}
                            className="registration-form-input"
                        />
                    </div>
                    <div>
                        <label htmlFor="email" className="block text-sm font-medium text-gray-700"
                               style={{paddingLeft: 70}}>
                            Email
                        </label>
                        <br/>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleInputChange}
                            className="registration-form-input"
                        />
                    </div>
                    <div>
                        <label htmlFor="password" className="block text-sm font-medium text-gray-700"
                               style={{paddingLeft: 70}}>
                            Password
                        </label>
                        <br/>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            value={formData.password}
                            onChange={handleInputChange}
                            className="registration-form-input"
                        />
                    </div>
                    {error && <div className="errorMessage">{error}</div>}
                    <button
                        type="submit"
                        className="registration-form-button"
                    >
                        Register
                    </button>
                </form>
                <div className="mt-4 text-center text-sm text-gray-600">
                    Already registered? <a href="/LogIn">Login</a>
                </div>

                <div className="registration-form-google-signin">
                    <button
                        onClick={() => {googleLogin}}
                        className="flex items-center justify-center gap-2 bg-white px-4 py-2 rounded-md shadow-md hover:shadow-lg transition-shadow"
                        style={{
                            border: '1px solid #dadce0',
                            width: '80%',
                            marginTop: '20px'
                        }}
                    >   <GoogleSignIN/>
                    </button>
                </div>
            </div>
        </div>
    );
};

export default RegistrationForm;