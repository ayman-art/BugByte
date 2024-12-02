import React, { useEffect, useState } from 'react';
import profilePath from '../assets/user-profile.svg';
import Layout from '../layouts/MainLayout';
import TextPopUp from '../components/BioPopup'
import { getProfile, updateBio } from '../API/ProfileAPI';
import { useParams } from 'react-router-dom';
interface UserProfile {
  username: string;
  reputation: number;
  followers: number;
  following: number;
  bio: string;
  is_admin: boolean; // Add this to match the fetched user profile structure
}
interface Post {
  id: number;
  title: string;
  content: string;
}

const Profile: React.FC = () => {
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
        });
      } catch (error) {
        console.error('Error fetching user profile:', error);
      } finally {
        setProfileLoading(false);
      }
    }

    fetchProfile();
  }, []);
  const handleFollow= (): React.MouseEventHandler<HTMLButtonElement> | undefined => {
    throw new Error('Function not implemented.');
  }
  const handleUnfollow= (): React.MouseEventHandler<HTMLButtonElement> | undefined =>{
    throw new Error('Function not implemented.');
  }
  const handleAdminize= (): React.MouseEventHandler<HTMLButtonElement> | undefined=> {
    throw new Error('Function not implemented.');
  }
  // Fetch top posts data
  // useEffect(() => {
  //   async function fetchTopPosts(): Promise<void> {
  //     try {
  //       const response = await fetch('/api/top-posts');
  //       if (!response.ok) throw new Error('Failed to fetch top posts');
  //       const data: Post[] = await response.json();
  //       setTopPosts(data);
  //     } catch (error) {
  //       console.error('Error fetching top posts:', error);
  //     } finally {
  //       setPostsLoading(false);
  //     }
  //   }

  //   fetchTopPosts();
  // }, []);

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
          {/*<img
            src={userProfile.profilePicture}
            alt="Profile"
            style={styles.profilePicture}
          />*/}
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
            {userProfile.username !== loggedInUsername } (
              <button style={{ ...styles.button, ...styles.followButton }} onClick={handleFollow}>Follow</button>
            ) :(
              <button style={{ ...styles.button, ...styles.followButton }} onClick={handleUnfollow}>Unfollow</button>
            )
            {isAdmin && !userProfile.is_admin && (
              <button style={{ ...styles.button, ...styles.adminButton }} onClick={handleAdminize}>Make Admin</button>
            )}
          </div>
        </div>

        {/* Bio Section */}
          <div style={styles.bio}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <h3>About:</h3>
              <button onClick={handleButtonClick} style={{ ...styles.openButton, ...styles.editButton }}>
                <span style={{ marginRight: '8px' }}>✏️</span> Edit
              </button>
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

