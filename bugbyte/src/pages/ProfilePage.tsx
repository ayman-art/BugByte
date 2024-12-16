import React, { useEffect, useState } from 'react';
import profilePath from '../assets/user-profile.svg';
import Layout from '../layouts/MainLayout';
import TextPopUp from '../components/BioPopup'
import { followUser, getProfile, makeAdmin, unfollowUser, updateBio } from '../API/ProfileAPI';
import { useNavigate, useParams } from 'react-router-dom';
import { useNavbar } from '@nextui-org/navbar';
interface UserProfile {
  username: string;
  reputation: number;
  followers: number;
  following: number;
  bio: string;
  is_following: boolean;
  is_admin: boolean; // Add this to match the fetched user profile structure
}
interface Post {
  id: number;
  title: string;
  content: string;
}

const Profile: React.FC = () => {
  const navigate = useNavigate()
  const [userProfile, setUserProfile] = useState<UserProfile | null>(null);
  const {userName} = useParams<{ userName: string }>();
  const [topPosts, setTopPosts] = useState<Post[]>([
    {
      id: 1,
      title: "string",
      content: "string"
    },
    {
      id: 2,
      title: "string",
      content: "string"
    },
    {
      id: 3,
      title: "string",
      content: "string"
    }
  ]);
  const [profileLoading, setProfileLoading] = useState<boolean>(true);
  const [postsLoading, setPostsLoading] = useState<boolean>(false);
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [error, setError] = useState<string>('');

  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = localStorage.getItem('is_admin') === 'true';

  const handleButtonClick = () => {
    setIsPopupOpen(true);
  };

  const handlePopupSubmit = (newText: string) => {
    // PUT request HERE
    let newUserProfile = userProfile;
    try{
      const token = localStorage.getItem('authToken')
      updateBio(newText, token!)
      newUserProfile!.bio = newText;
      setUserProfile(newUserProfile);
    }catch ( e){
      setError("Failed to update bio");
    }
  };

  const handlePopupClose = () => {
    setIsPopupOpen(false);
  };
  
  // Fetch profile data
  useEffect(() => {
    async function fetchProfile(): Promise<void> {
      try {
        const token = localStorage.getItem('authToken')
        const data = await getProfile(userName!, token!)
        setUserProfile({
          username: data['userName'],
          reputation: data['reputation'],
          followers: data['followersCount'],
          following: data['followingsCount'],
          bio: data['bio'],
          is_admin: data['isAdmin'],
          is_following: data['is_following']
        });
        
      } catch (error) {
        console.error('Error fetching user profile:', error);
      } finally {
        setProfileLoading(false);
      }
    }

    fetchProfile();
  }, [userName]);
  const handleFollow= async () => {
    try {
      const token = localStorage.getItem('authToken')
      await followUser(userName!, token!)
      setUserProfile((prevProfile) => 
        prevProfile ? { 
          ...prevProfile, 
          is_following: true, 
          followers: prevProfile.followers + 1 
        } : null
      );
    } catch (error) {
      console.error('Error following user:', error);
    }
  }
  const handleUnfollow= async() =>{
    try {
      const token = localStorage.getItem('authToken')
      await unfollowUser(userName!, token!)
      setUserProfile((prevProfile) => 
        prevProfile ? { 
          ...prevProfile, 
          is_following: false, 
          followers: prevProfile.followers - 1 
        } : null
      );
    } catch (error) {
      console.error('Error following user:', error);
    }
  }
  const handleAdminize= async () => {
    try {
      const token = localStorage.getItem('authToken')
      await makeAdmin(userName!, token!)
      setUserProfile((prevProfile) => 
        prevProfile ? { 
          ...prevProfile, 
          is_admin: true, 
        } : null
      );
      localStorage.setItem("is_admin",'true')
    } catch (error) {
      console.error('Error following user:', error);
    }
  }

  if (profileLoading) {
    return <div style={{ textAlign: 'center', marginTop: '20px' }}>Loading profile...</div>;
  }

  if (!userProfile) {
    return <div style={{ textAlign: 'center', marginTop: '20px', color: 'red' }}>Error loading profile. Please try again later.</div>;
  }

  // Inline style definitions
  const styles = {
    errorMessge: {
      color: 'red',
      fontSize: '12px', 
      marginTop: '10px', 
      textAlign: 'center' as 'center', 
    },
    container: {
      margin: '0 auto',
      padding: '20px',
      backgroundColor: '#f5f5f5',
      borderRadius: '10px',
      boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
      fontFamily: 'Arial, sans-serif',
    },
    header: {
      display: 'flex',
      alignItems: 'center',
      gap: '20px',
      marginBottom: '20px',
      justifyContent: 'space-between', // Add space between the profile info and the buttons
    },
    profilePicture: {
      width: '100px',
      height: '100px',
      borderRadius: '50%',
      border: '2px solid #ddd',
    },
    profileInfo: {
      margin: 0,
    },
    followStats: {
      display: 'flex',
      gap: '20px',
      fontSize: '14px',
      color: '#555',
    },
    bio: {
      marginBottom: '20px',
      padding: '10px',
      backgroundColor: '#eaeaea',
      borderRadius: '8px',
    },
    posts: {
      marginTop: '20px',
    },
    post: {
      backgroundColor: '#fff',
      padding: '10px',
      marginBottom: '10px',
      border: '1px solid #ddd',
      borderRadius: '8px',
    },
    openButton: {
      padding: '8px 12px',
      fontSize: '14px',
      borderRadius: '5px',
      border: 'none',
      cursor: 'pointer',
    },
    editButton: {
      backgroundColor: '#ADD8E6', // Light blue
      color: '#333',
      display: 'flex',
      alignItems: 'center',
      fontSize: '14px',
      fontWeight: 'bold',
    },
    postTitle: {
      margin: '0 0 5px 0',
      fontSize: '16px',
    },
    postContent: {
      margin: 0,
      fontSize: '14px',
      color: '#555',
    },
    button: {
      padding: '8px 12px',
      border: 'none',
      borderRadius: '5px',
      cursor: 'pointer',
    },
    followButton: {
      backgroundColor: '#4caf50',
      color: 'white',
    },
    adminButton: {
      backgroundColor: '#f44336',
      color: 'white',
    },
  };

  return (
      <div style={styles.container}>
        {/* Profile Header */}
        <div style={styles.header}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
          {}
          <img
            src={profilePath}
            alt="Profile"
            style={styles.profilePicture}
          />
          <div>
            <h1>{userProfile.username}</h1>
            <p>Reputation: {userProfile.reputation}</p>
            <div style={styles.followStats}>
              <p>Followers: {userProfile.followers}</p>
              <p>Following: {userProfile.following}</p>
            </div>
          </div>
        </div>
        {/* Buttons */}
        <div style={{ display: 'flex', gap: '10px' }}>
        {userProfile.username !== loggedInUsername && (
              userProfile.is_following ? (
                <button style={{ ...styles.button, ...styles.followButton }} onClick={handleUnfollow}>
                  Unfollow
                </button>
              ) : (
                <button style={{ ...styles.button, ...styles.followButton }} onClick={handleFollow}>
                  Follow
                </button>
              )
            )}
            {isAdmin && !userProfile.is_admin && (
              <button style={{ ...styles.button, ...styles.adminButton }} onClick={handleAdminize}>Make Admin</button>
            )}
          </div>
        </div>

        {/* Bio Section */}
          <div style={styles.bio}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <h3>About:</h3>
              {userProfile.username === loggedInUsername && (<button onClick={handleButtonClick} style={{ ...styles.openButton, ...styles.editButton }}>
                <span style={{ marginRight: '8px' }}>✏️</span> Edit
              </button>)}
          </div>
          <p>{userProfile.bio || 'No bio available.'}</p>
          {isPopupOpen && (
            <TextPopUp 
              initialText={userProfile.bio}
              onSubmit={handlePopupSubmit}
              onClose={handlePopupClose}
            />
          )}
          {error && <div style={styles.errorMessge }>{error}</div>}
        </div>

        {/* Top Posts Section */}
        <div style={styles.posts}>
          <h3>Top Posts:</h3>
          {postsLoading ? (
            <p>Loading top posts...</p>
          ) : (
            <ul>
              {topPosts.map((post) => (
                <li key={post.id} style={styles.post}>
                  <h4 style={styles.postTitle}>{post.title}</h4>
                  <p style={styles.postContent}>{post.content}</p>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
  );
};

export default Profile;

