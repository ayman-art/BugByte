import React, { useEffect, useState } from 'react';
import logoPath from '../assets/bugbyteLogo.svg';
import profilePath from '../assets/user-profile.svg';
import { Link, useNavigate } from 'react-router-dom';
import { API_URLS } from '../API/ApiUrls';
import PostModal from './PostModal';
import { postQuestion } from '../API/PostAPI';

interface NavbarProps {
  onLogout: () => void;
}

interface PostDetails {
  title?: string; 
  content: string; 
  community?: string; 
  tags?: string[];
  communityId?: string;
}

const Navbar: React.FC<NavbarProps> = ({ onLogout }) => {
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [profilePicFetched, setProfilePicFetched] = useState<string | null>(null);

  // Fetch profile picture on component mount
  useEffect(() => {
    const fetchProfilePicture = async () => {
      try {
        const token = localStorage.getItem('authToken');
        if (!token) {
          console.error('No auth token found');
          return;
        }

        const response = await fetch(API_URLS.GET_PROFILE_PICTURE, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          throw new Error('Failed to fetch profile picture');
        }

        const data = await response.json();
        setProfilePicFetched(data.profilePictureUrl);
      } catch (error) {
        console.error('Error fetching profile picture:', error);
      }
    };

    fetchProfilePicture();
  }, []);

  const visitProfile = () => {
    const username = localStorage.getItem("name");
    navigate(`/Profile/${username}`);
  };

  const handleSavePost = async (postDetails: PostDetails) => {
    try {
      const token = localStorage.getItem('authToken');
      if (!token) {
        console.error('No auth token found');
        return;
      }

      if (!postDetails.communityId) {
        console.error('Community ID is required');
        return;
      }

      const id = await postQuestion(
        postDetails.content,
        postDetails.title || '',
        postDetails.tags || [],
        postDetails.communityId,
        token
      );

      // Close modal and navigate to the newly created post
      setShowModal(false);
      navigate(`/Posts/${id}`);
    } catch (error) {
      console.error('Error saving post:', error);
      // Optionally, show an error message to the user
    }
  };

  const handleUpdateProfilePicture = async (url: string) => {
    try {
      const token = localStorage.getItem('authToken');
      if (!token) {
        console.error('No auth token found');
        return;
      }

      const response = await fetch(API_URLS.UPDATE_PROFILE_PICTURE, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ url })
      });

      if (!response.ok) {
        throw new Error('Updating Profile Picture failed');
      }

      // Update local state with new profile picture
      setProfilePicFetched(url);
    } catch (error) {
      console.error('Error updating profile picture:', error);
    }
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
          src={profilePicFetched || profilePath} 
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
