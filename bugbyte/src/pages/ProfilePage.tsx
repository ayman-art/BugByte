import React from 'react';
import { useParams } from 'react-router-dom';
import NavBar from '../components/NavBar';

interface ProfileProps {
  isCurrentUser: boolean;
  darkMode: boolean;
  user: {
    name: string;
    avatarUrl: string;
    reputation: number;
    communities: Array<{ name: string}>;
  };
}

const ProfilePage: React.FC<ProfileProps> = ({ isCurrentUser, darkMode, user }) => {
  const { userId } = useParams();
  
  console.log("User ID from route:", userId); // Access userId from the URL

  return (
    <div>
      <NavBar/>
      <div
        className={`profile-page ${darkMode ? 'dark' : ''}`}
        style={{
          backgroundColor: darkMode ? '#1a1a1a' : '#fff',
          color: darkMode ? '#fff' : '#000',
          padding: '1rem',
          borderRadius: '10px',
        }}
      >
        
        <div className="header" style={{ display: 'flex', alignItems: 'center' }}>
          <img
            src={user.avatarUrl}
            alt={`${user.name}'s avatar`}
            style={{
              width: '100px',
              height: '100px',
              borderRadius: '50%',
              marginRight: '1rem',
            }}
          />
          <div>
            <h1>{user.name}</h1>
          </div>
        </div>

        <div className="stats">
          <h2>Stats</h2>
          <p>Reputation: {user.reputation}</p>
        </div>
      </div>
    </div>
  );
};

export default ProfilePage;
