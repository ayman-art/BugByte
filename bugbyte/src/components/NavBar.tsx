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
      <div style={styles.logoContainer} onClick={() => navigate('/')}>
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

      {/* Modal for Adding Post */}
      <PostModal 
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        onSave={handleSavePost}
      />
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
    cursor: 'pointer',
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
    userSelect: 'none' as const,
  },
  rightContainer: {
    display: 'flex',
    alignItems: 'center',
  },
  profileIcon: {
    height: '50px',
    width: '50px',
    borderRadius: '50%',
    marginLeft: '10px', // Space between the plus button and profile icon
    cursor: 'pointer',
  },
  plusButton: {
    backgroundColor: '#28a745', // Green color
    color: '#ffffff',
    border: 'none',
    borderRadius: '50%',
    width: '40px',
    height: '40px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    cursor: 'pointer',
    fontSize: '1.5rem',
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
  modal: {
    position: 'fixed',
    top: 0,
    left: 0,
    width: '100%',
    height: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  modalContent: {
    backgroundColor: 'white',
    padding: '20px',
    borderRadius: '10px',
    width: '300px',
  },
  textarea: {
    width: '100%',
    height: '100px',
    marginBottom: '10px',
    padding: '10px',
    borderRadius: '5px',
    border: '1px solid #ccc',
  },
  tagSection: {
    marginBottom: '10px',
  },
  tagInput: {
    width: '70%',
    padding: '8px',
    borderRadius: '5px',
    border: '1px solid #ccc',
    marginRight: '10px',
  },
  addTagButton: {
    backgroundColor: '#099154',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '8px 16px',
    cursor: 'pointer',
  },
  tagList: {
    display: 'flex',
    flexWrap: 'wrap',
    marginTop: '10px',
  },
  tagItem: {
    backgroundColor: '#f1f1f1',
    padding: '5px 10px',
    margin: '5px',
    borderRadius: '5px',
    display: 'flex',
    alignItems: 'center',
  },
  removeTagButton: {
    backgroundColor: 'transparent',
    color: '#ff4757',
    border: 'none',
    marginLeft: '5px',
    cursor: 'pointer',
  },
  modalButtons: {
    display: 'flex',
    justifyContent: 'space-between',
  },
  saveButton: {
    backgroundColor: '#099154',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '8px 16px',
    cursor: 'pointer',
  },
  closeButton: {
    backgroundColor: '#ff4757',
    color: 'white',
    border: 'none',
    borderRadius: '5px',
    padding: '8px 16px',
    cursor: 'pointer',
  },
  communityDropdown: {
    marginBottom: '10px',
  },
  dropdown: {
    width: '100%',
    padding: '8px',
    borderRadius: '5px',
    border: '1px solid #ccc',
    marginTop: '5px',
  },
};

export default Navbar;
