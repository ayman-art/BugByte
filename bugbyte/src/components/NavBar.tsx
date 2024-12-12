import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import logoPath from '../assets/bugbyteLogo.svg';
import profilePath from '../assets/user-profile.svg';
import PostModal from './PostModal';

interface NavbarProps {
  onLogout: () => void;
}

const Navbar: React.FC<NavbarProps> = ({ onLogout }) => {
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);

  const visitProfile = () => {
    const username = localStorage.getItem('name');
    navigate(`/Profile/${username}`);
  };

  const handleSavePost = (postDetails: { content: string; community: string; tags: string[] }) => {
    console.log('Post saved:', postDetails);
    // Add your post saving logic here
  };

  return (
    <nav style={styles.navbar}>
      {/* Logo and Brand Name */}
      <div style={styles.logoContainer} onClick={() => navigate('/')}>
        <img src={logoPath} alt="Logo" style={styles.logo} />
        <span style={styles.brandName}>BugByte</span>
      </div>

      {/* Profile and Logout */}
      <div style={styles.rightContainer}>
        <button style={styles.plusButton} onClick={() => setShowModal(true)}>
          +
        </button>
        <img
          src={profilePath}
          alt="Profile"
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
