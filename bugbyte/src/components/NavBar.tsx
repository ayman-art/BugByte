// Navbar.tsx
import React, { useEffect, useState } from 'react';
import logoPath from '../assets/bugbyteLogo.svg';
import profilePath from '../assets/user-profile.svg';
import { Link, useNavigate } from 'react-router-dom';
import { Cursor } from 'react-simple-typewriter';
import { updateProfilePicture } from '../API/ProfileAPI';
import { API_URLS } from '../API/ApiUrls';
interface NavbarProps {
  onLogout: () => void;
}
const Navbar: React.FC<NavbarProps> = ({onLogout}) => {
  const [profilePicFetched, setProfilePic] = useState<string | null>(null)
  const visitProfile = ()=>{
    const username = localStorage.getItem("name");
    navigate(`/Profile/${username}`)
  }
  const navigate = useNavigate()
  // useEffect(()=>{
  //   async function fetchPicture(): Promise<void>{
      
  //     const token = localStorage.getItem('authToken')
  //     const response2 = await fetch(`${API_URLS.UPDATE_PROFILE_PICTURE}`, {
  //             method: 'POST',
  //             headers: {
  //               'Authorization': `Bearer ${token}`,
  //               'Content-Type': 'application/json'
  //             },
  //             body:JSON.stringify({
  //               'url': url
  //             })
  //           });
  //           if(!response2.ok){
  //             throw new Error(`Updating Profile Picture failed`)
  //           }
  //   }
  //   fetchPicture();
  // })
  return (
    <nav style={styles.navbar}>
      {/* Logo and Brand Name */}
      <div style={styles.logoContainer} onClick={()=>navigate('/')}>
        <img src={logoPath} alt="Logo" style={styles.logo} />
        <span style={styles.brandName}>BugByte</span>
      </div>

      {/* Profile and Logout */}
      <div style={styles.rightContainer}>
      <img 
        src={profilePicFetched || profilePath} alt="Profile" 
        style={styles.profileIcon}
        onClick={visitProfile} 
       />
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
    cursor:'pointer'
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
    userSelect:'none' as const
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
    cursor: 'pointer'
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