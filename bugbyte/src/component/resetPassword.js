import React, { useState } from 'react';
import '../styles/resetPassword.css';

const ResetPasswordPopup = ({ setShowPopup }) => {
  const [step, setStep] = useState(1);
  const [verificationCode, setVerificationCode] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');

  const handleVerificationCodeChange = (e) => {
    setError("")
    setVerificationCode(e.target.value);

  };

  const handleNewPasswordChange = (e) => {
    setError("")
    setNewPassword(e.target.value);
  };

  const handleConfirmPasswordChange = (e) => {
      setError("")
      setConfirmPassword(e.target.value);
  };

  const handleVerificationSubmit =async (e) => {
    e.preventDefault();
  try {
    const response = await fetch('https://users/verifyCode', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userId: localStorage.getItem("userId"),
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

  const handleResetPasswordSubmit = async(e) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      setError('Passwords do not match!');
      }
    else
    {
      const response = await fetch('https://users/reset-password', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          userId: localStorage.getItem('userId'), // Retrieve userId from localStorage
          newPassword: newPassword // Pass newPassword
        }),
      });

      if (response.ok) {
        const data = await response.json();
        console.log('Reset success', data);
      } else {
        console.error('Reset failed');
      }
    }

//    console.log('Password reset successful');
//    setShowPopup(false); // Close the popup after submission
  };

  return (
    <div className="popup">
      <div className="popupContent">
        <span className="closePopup" onClick={() => setShowPopup(false)}>&times;</span>
        <h2 className="title">{step === 1 ? 'Verfication' : 'Reset Password'}</h2>

        {step === 1 ? (
          <form onSubmit={handleVerificationSubmit}>
            <div className="inputGroup2">
              <label htmlFor="verificationCode" className ="label2">check your email for verfication code</label>
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
            <button type="submit" className="resetPasswordButton ">Reset Password</button>
          </form>
        )}
      </div>
    </div>
  );
};

export default ResetPasswordPopup;
