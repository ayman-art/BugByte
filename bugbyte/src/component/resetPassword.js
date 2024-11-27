import React, { useState, useEffect } from 'react';
import '../styles/resetPassword.css';

const ResetPasswordPopup = ({ setShowPopup }) => {
  const [step, setStep] = useState(1);
  const [verificationCode, setVerificationCode] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [timer, setTimer] = useState(60);
  const [canResend, setCanResend] = useState(false);

  useEffect(() => {
    if (timer > 0) {
      const countdown = setTimeout(() => setTimer(timer - 1), 1000);
      return () => clearTimeout(countdown);
    } else {
      setCanResend(true);
    }
  }, [timer]);

  const handleVerificationCodeChange = (e) => {
    setError('');
    setVerificationCode(e.target.value);
  };

  const handleNewPasswordChange = (e) => {
    setError('');
    setNewPassword(e.target.value);
  };

  const handleConfirmPasswordChange = (e) => {
    setError('');
    setConfirmPassword(e.target.value);
  };

  const handleVerificationSubmit = async (e) => {
    e.preventDefault();
     try {
       const response = await fetch('https://users/validate-email-code', {
         method: 'POST',
         headers: {
           'Content-Type': 'application/json',
         },
         body: JSON.stringify({
           userId: localStorage.getItem('userId'),
           verificationCode: verificationCode,
         }),
       });

       if (response.ok) {
         const data = await response.json();
         console.log('Code is correct', data);
         setStep(2);
       } else {
         setError('Invalid verification code!');
       }
     } catch (error) {
       console.error('Error:', error);
       setError('An error occurred. Please try again.');
     }

  };

  const handleResetPasswordSubmit = async (e) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      setError('Passwords do not match!');
    } else {
      const response = await fetch('https://users/change-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userId: localStorage.getItem('userId'),
          newPassword: newPassword,
        }),
      });

      if (response.ok) {
        const data = await response.json();
        console.log('Reset success', data);
      } else {
        console.error('Reset failed');
      }
    }
  };

  const handleResendCode = async () => {
    setCanResend(false);
    setTimer(60);
     try {
       const response = await fetch('https://users/resendCode', {
         method: 'POST',
         headers: {
           'Content-Type': 'application/json',
         },
         body: JSON.stringify({
           userId: localStorage.getItem('userId'),
         }),
       });

       if (response.ok) {
         console.log('Code resent successfully');
       } else {
         console.error('Failed to resend code');
         setError('Failed to resend code. Please try again.');
       }
     } catch (error) {
       console.error('Error:', error);
       setError('An error occurred. Please try again.');
     }
    console.log('Code resent successfully');
  };

  return (
    <div className="popup">
      <div className="popupContent">
        <span className="closePopup" onClick={() => setShowPopup(false)}>&times;</span>
        <h2 className="title">{step === 1 ? 'Verification' : 'Reset Password'}</h2>

        {step === 1 ? (
          <form onSubmit={handleVerificationSubmit}>
            <div className="inputGroup2">
              <label htmlFor="verificationCode" className="label2">Check your email for verification code</label>
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
            <button type="submit" className="resetPasswordButton">Verify Code</button>
            <button className="resetPasswordButton"
              type="button"
           //   className="resendCodeButton"
              onClick={handleResendCode}
              disabled={!canResend}
            >
              {canResend ? 'Resend Code' : `Resend in ${timer}s`}
            </button>
          </form>
        ) : (
          <form onSubmit={handleResetPasswordSubmit}>
            <div className="inputGroup2">
              <label htmlFor="newPassword" className="label">New Password</label>
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
              <label htmlFor="confirmPassword" className="label">Confirm Password</label>
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
            <button type="submit" className="resetPasswordButton">Reset Password</button>
          </form>
        )}
      </div>
    </div>
  );
};

export default ResetPasswordPopup;
