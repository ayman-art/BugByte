// Profile.tsx
import React, { useState, useEffect } from 'react';
import '../styles/ProfilePage.css'
import {ReactComponent as ProfilePic} from '../assets/user-profile.svg'
interface UserData {
  name: string;
  reputation: number;
  about: string;
}

const Profile: React.FC = () => {
  const [user, setUser] = useState<UserData | null>({
    name: "Abdullah",
    reputation: 1000,
    about: "this is an engineer"
  });
  //useState<UserData | null>(null);

  useEffect(() => {
    // Fetch user data from the backend
    const fetchUserData = async () => {
      const response = await fetch('/api/user');
      const userData = await response.json();
      setUser(userData);
    };
    fetchUserData();
  }, []);

  if (!user) {
    return <div>Loading...</div>;
  }

  return (
    <div className="p-4">
      <div className="flex items-center mb-4">
        <span className="text-xl font-bold">{user.name}</span>
      </div>
      <p>Reputation: {user.reputation}</p>
      <p>{user.about}</p>
    </div>
  );
};

export default Profile;