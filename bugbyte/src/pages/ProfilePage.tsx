import React, { useEffect, useState } from 'react';
import profilePath from '../assets/user-profile.svg';
import TextPopUp from '../components/BioPopup'
import { followUser, getProfile, makeAdmin, unfollowUser, updateBio, updateProfilePicture } from '../API/ProfileAPI';
import { useNavigate, useParams } from 'react-router-dom';
interface UserProfile {
  username: string;
  reputation: number;
  followers: number;
  following: number;
  bio: string;
  is_following: boolean;
  is_admin: boolean; // Add this to match the fetched user profile structure
  profile_picture?: string; // New optional field for profile picture
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
  const [isProfilePicPopupOpen, setIsProfilePicPopupOpen] = useState(false);
  const [error, setError] = useState<string>('');
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

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
  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files && event.target.files[0]) {
      setSelectedFile(event.target.files[0]);
      setIsProfilePicPopupOpen(true);
    }
  };
  
  // Upload profile picture
  const handleProfilePictureUpload = async () => {
    if (!selectedFile) return;

    try {
      const token = localStorage.getItem('authToken');
      const formData = new FormData();
      formData.append('file', selectedFile);

      const updatedProfilePic = await updateProfilePicture(formData, token!);
      
      setUserProfile((prevProfile) => 
        prevProfile ? { 
          ...prevProfile, 
          profile_picture: updatedProfilePic 
        } : null
      );
      
      setIsProfilePicPopupOpen(false);
      setSelectedFile(null);
    } catch (error) {
      console.error('Error uploading profile picture:', error);
      setError("Failed to upload profile picture");
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
          is_following: data['is_following'],
          profile_picture: data['picture']
        });
        
      } catch (error) {
        console.error('Error fetching user profile:', error);
      } finally {
        setProfileLoading(false);
      }
    }

    fetchProfile();
  }, []);
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

  

  return (
      <div style={styles.container}>
        {/* Profile Header */}
        <div style={styles.header}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }} >
          {/* Profile Picture with Click to Upload */}
          <div style={{ position: 'relative', cursor: 'pointer' }}>
          {loggedInUsername === userName && (
            <input 
              type="file" 
              accept="image/*" 
              onChange={handleFileSelect}
              style={{ display: 'none' }} 
              id="profile-pic-upload"
            />)}
            <label htmlFor="profile-pic-upload">
              <img
                src={userProfile?.profile_picture || profilePath}
                alt="Profile"
                style={styles.profilePicture}
              />
              {loggedInUsername === userName && (
                <div style={{
                  position: 'absolute', 
                  bottom: 0, 
                  right: 0, 
                  backgroundColor: 'rgba(0,0,0,0.5)', 
                  color: 'white', 
                  padding: '2px 5px', 
                  borderRadius: '50%'
                }}>
                  📷
                </div>
              )}
            </label>
          </div>

          {/* Profile Picture Upload Popup */}
          {isProfilePicPopupOpen && (
            <div style={{
              position: 'fixed',
              top: 0,
              left: 0,
              width: '100%',
              height: '100%',
              backgroundColor: 'rgba(0,0,0,0.5)',
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              zIndex: 1000
            }}>
              <div style={{
                backgroundColor: 'white',
                padding: '20px',
                borderRadius: '10px',
                textAlign: 'center'
              }}>
                <h3>Upload Profile Picture</h3>
                {selectedFile && (
                  <img 
                    src={URL.createObjectURL(selectedFile)} 
                    alt="Selected" 
                    style={{ 
                      maxWidth: '200px', 
                      maxHeight: '200px', 
                      marginBottom: '10px' 
                    }} 
                  />
                )}
                <div>
                  <button onClick={handleProfilePictureUpload}>
                    Upload
                  </button>
                  <button onClick={() => {
                    setIsProfilePicPopupOpen(false);
                    setSelectedFile(null);
                  }}>
                    Cancel
                  </button>
                </div>
              </div>
            </div>
          )}
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
export default Profile;