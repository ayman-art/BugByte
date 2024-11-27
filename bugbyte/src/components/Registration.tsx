import React, { useState, FormEvent } from 'react';
import { User } from '../types';
import { useTypewriter } from 'react-simple-typewriter';
import { useGoogleLogin } from '@react-oauth/google';
import '../styles/components.css';

const RegistrationForm: React.FC = () => {
    const [formData, setFormData] = useState<User>({
        username: '',
        email: '',
        password: ''
    });

    // Handle Google login
    const googleLogin = useGoogleLogin({
        onSuccess: async (response) => {
            try {
                // Get user info from Google
                const userInfoResponse = await fetch(
                    'https://www.googleapis.com/oauth2/v3/userinfo',
                    {
                        headers: {
                            Authorization: `Bearer ${response.access_token}`,
                        },
                    }
                );
                const userInfo = await userInfoResponse.json();
                console.log('Google user info:', userInfo);
            } catch (error) {
                console.error('Error fetching Google user info:', error);
            }
        },
        onError: () => {
            console.error('Google Login Failed');
        },
    });

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        console.log('Registering user:', formData);
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
                    <button
                        type="submit"
                        className="registration-form-button"
                    >
                        Register
                    </button>
                </form>
                <div className="mt-4 text-center text-sm text-gray-600">
                    Already registered? <a href="/login">Login</a>
                </div>

                <div className="registration-form-google-signin">
                    <button
                        onClick={() => googleLogin()}
                        className="flex items-center justify-center gap-2 bg-white px-4 py-2 rounded-md shadow-md hover:shadow-lg transition-shadow"
                        style={{
                            border: '1px solid #dadce0',
                            width: '80%',
                            marginTop: '20px'
                        }}
                    >
                        <img
                            src='src/assets/google_logo.png'
                            alt='Google logo'
                            style={{
                                width: '50px',
                                height: '25px',
                                marginRight: '8px'
                            }}
                        />
                        <span style={{ color: '#3c4043' }}>Sign in with Google</span>
                    </button>
                </div>
            </div>
        </div>
    );
};

export default RegistrationForm;