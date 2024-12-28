import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import logoPath from '../assets/bugbyteLogo.svg';
import profilePath from '../assets/user-profile.svg';
import searchIconPath from '../assets/search.png';
import notificationsIconPath from '../assets/notifications.png';
import PostModal from './PostModal';

interface NavbarProps {
  onLogout: () => void;
}

interface Notification {
  message: string;
  link: string;
  datetime: string;
}

const Navbar: React.FC<NavbarProps> = ({ onLogout }) => {
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications] = useState<Notification[]>([
    { message: 'New comment on your post', link: '/post/1', datetime: '2024-12-29 10:30 AM' },
    { message: 'New follower: John Doe', link: '/profile/john', datetime: '2024-12-28 4:45 PM' },
    { message: 'Your post was liked by Jane', link: '/post/2', datetime: '2024-12-28 3:15 PM' },
  ]);

  const toggleNotifications = () => {
    setShowNotifications((prev) => !prev);
  };

  const visitProfile = () => {
    const username = localStorage.getItem('name');
    navigate(`/Profile/${username}`);
  };

  const goToSearch = () => {
    navigate('/Search');
  };

  return (
    <nav style={styles.navbar}>
      {/* Logo and Brand Name */}
      <div style={styles.logoContainer} onClick={() => navigate('/')}>
        <img src={logoPath} alt="Logo" style={styles.logo} />
        <span style={styles.brandName}>BugByte</span>
      </div>

      {/* Right Container */}
      <div style={styles.rightContainer}>
        <button style={styles.plusButton} onClick={() => setShowModal(true)}>
          +
        </button>
        <img
          src={searchIconPath}
          alt="Search"
          style={styles.searchIcon}
          onClick={goToSearch}
        />

        {/* Notifications Dropdown */}
        <div style={styles.notificationsContainer}>
          <img
            src={notificationsIconPath}
            alt="Notifications"
            style={styles.notificationsIcon}
            onClick={toggleNotifications}
          />
          {showNotifications && (
            <div style={styles.notificationsDropdown}>
              {notifications.length > 0 ? (
                notifications.map((notification, index) => (
                  <div key={index} style={styles.notificationItem}>
                    <a href={notification.link} style={styles.notificationLink}>
                      <p style={styles.notificationMessage}>{notification.message}</p>
                    </a>
                    <span style={styles.notificationDatetime}>{notification.datetime}</span>
                  </div>
                ))
              ) : (
                <div style={styles.noNotifications}>No Notifications</div>
              )}
            </div>
          )}
        </div>

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
      <PostModal isOpen={showModal} onClose={() => setShowModal(false)} onSave={() => setShowModal(false)} />
    </nav>
  );
};

const styles: Record<string, React.CSSProperties> = {
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
    height: '80px',
    width: '80px',
    marginRight: '10px',
  },
  brandName: {
    fontSize: '1.5rem',
    fontWeight: 'bold',
    userSelect: 'none',
  },
  rightContainer: {
    display: 'flex',
    alignItems: 'center',
  },
  searchIcon: {
    height: '40px',
    width: '40px',
    marginRight: '10px',
    cursor: 'pointer',
  },
  notificationsContainer: {
    position: 'relative',
    marginRight: '10px',
  },
  notificationsIcon: {
    height: '40px',
    width: '40px',
    cursor: 'pointer',
  },
  notificationsDropdown: {
    position: 'absolute',
    top: '50px',
    right: '0',
    backgroundColor: '#ffffff',
    border: '1px solid #ccc',
    borderRadius: '5px',
    boxShadow: '0px 4px 6px rgba(0, 0, 0, 0.1)',
    zIndex: 10,
    width: '300px',
    maxHeight: '400px',
    overflowY: 'auto',
    padding: '10px',
  },
  notificationItem: {
    display: 'flex',
    flexDirection: 'column',
    padding: '10px',
    marginBottom: '5px',
    borderRadius: '5px',
    backgroundColor: '#f9f9f9',
    boxShadow: '0px 2px 4px rgba(0, 0, 0, 0.1)',
  },
  notificationLink: {
    textDecoration: 'none',
    color: '#333',
  },
  notificationMessage: {
    fontSize: '1rem',
    marginBottom: '5px',
  },
  notificationDatetime: {
    fontSize: '0.8rem',
    color: '#666',
    textAlign: 'right',
  },
  noNotifications: {
    padding: '10px',
    textAlign: 'center',
    color: '#666',
  },
  profileIcon: {
    height: '50px',
    width: '50px',
    borderRadius: '50%',
    cursor: 'pointer',
  },
  plusButton: {
    backgroundColor: '#28a745',
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
};

export default Navbar;

