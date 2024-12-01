// Navbar.tsx
import React from 'react';
import logoPath from '../assets/bugbyteLogo.svg';
import profilePath from '../assets/user-profile.svg';
import { Link } from 'react-router-dom';


const Navbar: React.FC = () => {
  const onLogout = ()=>{

  }

  return (
    <nav style={styles.navbar}>
      {/* Logo and Brand Name */}
      <div style={styles.logoContainer}>
        <img src={logoPath} alt="Logo" style={styles.logo} />
        <span style={styles.brandName}>BugByte</span>
      </div>

      {/* Profile and Logout */}
      <div style={styles.rightContainer}>
      <img src={profilePath} alt="Profile" style={styles.profileIcon} />
      <button style={styles.logoutButton} onClick={onLogout}>
          Logout
        </button>
      </div>
    </nav>
  );
};

const styles = {
  navbar: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '10px 10%',
    backgroundColor: '#099154',
    color: 'white',
    height: '80px',
  },
  logoContainer: {
    display: 'flex',
    alignItems: 'center',
  },
  logo: {
    color: '#ffffff',
    height: '80px',
    width: '80px',
    marginRight: '10px',
  },
  brandName: {
    fontSize: '1.5rem',
    fontWeight: 'bold',
  },
  rightContainer: {
    display: 'flex',
    alignItems: 'center',
  },
  profileIcon: {
    height: '50px',
    width: '50px',
    borderRadius: '50%',
    marginRight: '10px',
  },
  logoutButton: {
    backgroundColor: '#ff4757',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '8px 16px',
    fontSize: '1rem',
    cursor: 'pointer',
  },
};

export default Navbar;