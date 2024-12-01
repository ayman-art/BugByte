import React, { useState, useEffect } from 'react';
import '../styles/resetPassword.css';
import { validateEmailCode, resetPassword, resendVerificationCode } from '../API/ResetPasswordApi';

interface ResetPasswordPopupProps {
  setShowPopup: React.Dispatch<React.SetStateAction<boolean>>;
}

const ResetPasswordPopup: React.FC<ResetPasswordPopupProps> = ({ setShowPopup }) => {
  const [step, setStep] = useState<number>(1);
  const [verificationCode, setVerificationCode] = useState<string>('');
  const [newPassword, setNewPassword] = useState<string>('');
  const [confirmPassword, setConfirmPassword] = useState<string>('');
  const [error, setError] = useState<string>('');
  const [timer, setTimer] = useState<number>(60);
  const [canResend, setCanResend] = useState<boolean>(false);

  useEffect(() => {
    if (timer > 0) {
      const countdown = setTimeout(() => setTimer(timer - 1), 1000);
      return () => clearTimeout(countdown);
    } else {
      setCanResend(true);
    }
  }, [timer]);

  const handleVerificationCodeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setError('');
    setVerificationCode(e.target.value);
  };

  const handleNewPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setError('');
    setNewPassword(e.target.value);
  };

  const handleConfirmPasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setError('');
    setConfirmPassword(e.target.value);
  };

  const handleVerificationSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!verificationCode) {
      setError('Verification code is required.');
      return;
    }

    try {
      const email = localStorage.getItem('email');
      console.log(email)
      if (!email) {
        console.error('Email not found in localStorage');
        setError('Email not found. Please check your session.');
        return;
      }
      console.log(verificationCode)
      const response = await validateEmailCode(email, verificationCode);

      if (response.ok) {
        const data = await response.json();
        const { jwt } = data;
 
        if (jwt) {
          localStorage.setItem('authToken', jwt);
          console.log('JWT Token:', jwt);
        } else {
          setError('No token received. Please try again.');
        }
        console.log('Code is correct', data);
        setStep(2);
      } else {
        setError('Invalid verification code!');
      }
    } catch (error: unknown) {
      console.error('Error:', error);
      setError(error instanceof Error ? error.message : 'An error occurred. Please try again.');
    }
  };

  const handleResetPasswordSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (newPassword !== confirmPassword) {
      setError('Passwords do not match!');
      return;
    }

    try {
      const jwt = localStorage.getItem('authToken');
      if (!jwt) {
        console.error('Email not found in localStorage');
        setError('Email not found. Please check your session.');
        return;
      }

      const response = await resetPassword(jwt, newPassword);

      if (response.ok) {
        const data = await response.json();
        console.log('Password reset successful', data);
        setShowPopup(false);  // Close popup after success
      } else {
        setError('Failed to reset password.');
      }
    } catch (error: unknown) {
      console.error('Error:', error);
      setError(error instanceof Error ? error.message : 'An error occurred. Please try again.');
    }
  };

  const handleResendCode = async () => {
    setCanResend(false);
    setTimer(60);

    try {
      const email = localStorage.getItem('email');
      if (!email) {
        console.error('Email not found in localStorage');
        setError('Email not found. Please check your session.');
        return;
      }

      const response = await resendVerificationCode(email);

      if (response.ok) {
        console.log('Code resent successfully');
      } else {
        setError('Failed to resend code. Please try again.');
      }
    } catch (error: unknown) {
      console.error('Error:', error);
      setError(error instanceof Error ? error.message : 'An error occurred. Please try again.');
    }
  };

  return (
    <div className="popup">
      <div className="popupContent">
        <span className="closePopup" onClick={() => setShowPopup(false)}>
          &times;
        </span>
        <h2 className="title">{step === 1 ? 'Verification' : 'Reset Password'}</h2>

        {step === 1 ? (
          <form onSubmit={handleVerificationSubmit}>
            <div className="inputGroup2">
              <label htmlFor="verificationCode" className="label2">
                Check your email for verification code
              </label>
              <input
                id="verificationCode"
                type="text"
                placeholder="Enter verification code"
                value={verificationCode}
                onChange={handleVerificationCodeChange}
                className="input"
              />
            </div>
            {error && <div className="errorMessage">{error}</div>}
            <button type="submit" className="resetPasswordButton">
              Verify Code
            </button>
            <button
              type="button"
              className="resetPasswordButton"
              onClick={handleResendCode}
              disabled={!canResend}
            >
              {canResend ? 'Resend Code' : `Resend in ${timer}s`}
            </button>
          </form>
        ) : (
          <form onSubmit={handleResetPasswordSubmit}>
            <div className="inputGroup2">
              <label htmlFor="newPassword" className="label">
                New Password
              </label>
              <input
                id="newPassword"
                type="password"
                placeholder="Enter new password"
                value={newPassword}
                onChange={handleNewPasswordChange}
                className="input"
              />
            </div>
            <div className="inputGroup2">
              <label htmlFor="confirmPassword" className="label">
                Confirm Password
              </label>
              <input
                id="confirmPassword"
                type="password"
                placeholder="Confirm new password"
                value={confirmPassword}
                onChange={handleConfirmPasswordChange}
                className="input"
              />
            </div>
            {error && <div className="errorMessage">{error}</div>}
            <button type="submit" className="resetPasswordButton">
              Reset Password
            </button>
          </form>
        )}
      </div>
    </div>
  );
};

export default ResetPasswordPopup;

