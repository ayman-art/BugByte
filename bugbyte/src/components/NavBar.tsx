import React, { useEffect, useState } from "react";
import logoPath from "../assets/bugbyteLogo.svg";
import PostModal from "./PostModal";
import searchIconPath from "../assets/search.png";
import profilePath from "../assets/user-profile.svg";
import { Link, useNavigate } from "react-router-dom";
import notificationsIconPath from '../assets/notifications.png';
import { Notification } from '../pages/TestPage';
import WebSocketService from '../API/socketService';
import { API_URLS } from "../API/ApiUrls";
import { fetchNotifications } from '../API/NotificationAPI';
import validatePostDetails from '../utils/validateQuestion';
import { postQuestion } from '../API/PostAPI';

interface NavbarProps {
  onLogout: () => void;
}

const Navbar: React.FC<NavbarProps> = ({ onLogout }) => {
  const navigate = useNavigate();
  const [showModal, setShowModal] = useState(false);
  const [showNotifications, setShowNotifications] = useState(false);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const webSocketService = new WebSocketService(API_URLS.SOCKET_CONNECTION);

  useEffect(() => {
    const initNotifications = async () => {
      try {
        const fetchedNotifications: Notification[] = await fetchNotifications();
        setNotifications(fetchedNotifications.reverse());
        setUnreadCount(fetchedNotifications.length); // Set initial unread count
        const id = parseInt(localStorage.getItem("id")!);
        webSocketService.connect(onConnect, onError, id);
      } catch (e) {
        console.error(e);
      }
    };

    const onConnect = () => {
      console.log('Connected to WebSocket');
      const id = parseInt(localStorage.getItem("id")!);
      webSocketService.subscribe(`/topic/notifications/${id}`, (message) => {
        const newNotification = JSON.parse(message.body);
        setNotifications((prev) => [newNotification, ...prev]);
        setUnreadCount((prev) => prev + 1); // Increment unread count
      });
    };

    const onError = (error: string) => {
      console.error('WebSocket error:', error);
    };

    initNotifications();
    return () => {
      webSocketService.disconnect();
    };
  }, []);

  const toggleNotifications = () => {
    setShowNotifications((prev) => !prev);
    setUnreadCount(0); // Reset unread count when dropdown is opened
  };

  const visitProfile = () => {
    const username = localStorage.getItem('name');
    navigate(`/Profile/${username}`);
  };

  const goToSearch = () => {
    navigate('/Search');
  };

  const formatDate = (date: string) => {
    return new Intl.DateTimeFormat('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric',
    }).format(new Date(date));
  };
  interface PostDetails {
    title?: string; 
    content: string; 
    community?: string; 
    tags?: string[];
    communityId?: number;
  }
  
  
  const handleSavePost = async (postDetails: PostDetails) => {
    try {
      const token = localStorage.getItem("authToken");
      if (!token) {
        alert("No auth token found. Please log in.");
        return;
      }

      const validation = validatePostDetails(postDetails);
      if (!validation.isValid) {
        alert(validation.errors.join("\n"));
        return;
      }

      const id = await postQuestion(
        postDetails.content,
        postDetails.title!,
        postDetails.tags || [],
        postDetails.communityId!,
        token
      );

      navigate(`/Posts/${id}`);
    } catch (error) {
      console.error("Error saving post:", error);
      alert("An error occurred while saving the post. Please try again.");
    } finally {
      setShowModal(false);
    }
  };
  

  return (
    <nav style={styles.navbar}>
      {/* Logo and Brand Name */}
      <div style={styles.logoContainer} onClick={() => navigate("/")}>
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
          <div style={styles.notificationBadgeContainer}>
            <img
              src={notificationsIconPath}
              alt="Notifications"
              style={styles.notificationsIcon}
              onClick={toggleNotifications}
            />
            {unreadCount > 0 && (
              <span style={styles.notificationBadge}>{unreadCount}</span>
            )}
          </div>
          {showNotifications && (
            <div style={styles.notificationsDropdown}>
              {notifications.length > 0 ? (
                notifications.map((notification, index) => (
                  <div key={index} style={styles.notificationItem}>
                    <a href={notification.link} style={styles.notificationLink}>
                      <p style={styles.notificationMessage}>{notification.message}</p>
                    </a>
                    <span style={styles.notificationDatetime}>
                      {formatDate(notification.date)}
                    </span>
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
      <PostModal
        isOpen={showModal}
        onClose={() => setShowModal(false)}
        onSave={handleSavePost}
      />
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
    display: "flex",
    alignItems: "center",
    cursor: "pointer",
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
  notificationBadgeContainer: {
    position: 'relative',
  },
  notificationsIcon: {
    height: '40px',
    width: '40px',
    cursor: 'pointer',
  },
  notificationBadge: {
    position: 'absolute',
    top: '0',
    right: '-5px',
    backgroundColor: '#ff4757',
    color: 'white',
    borderRadius: '50%',
    width: '20px',
    height: '20px',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: '0.8rem',
    fontWeight: 'bold',
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
