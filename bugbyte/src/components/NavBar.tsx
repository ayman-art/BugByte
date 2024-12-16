import React, { useEffect, useState } from "react";
import logoPath from "../assets/bugbyteLogo.svg";
import profilePath from "../assets/user-profile.svg";
import searchIconPath from "../assets/search.png"; // Import the search icon
import { Link, useNavigate } from "react-router-dom";
import { API_URLS } from "../API/ApiUrls";

interface NavbarProps {
  onLogout: () => void;
}

const Navbar: React.FC<NavbarProps> = ({ onLogout }) => {
  const [profilePicFetched, setProfilePic] = useState<string | null>(null);
  const navigate = useNavigate();

  const visitProfile = () => {
    const username = localStorage.getItem("name");
    navigate(`/Profile/${username}`);
  };

  const goToSearch = () => {
    navigate("/Search"); // Navigate to the search page
  };

  return (
    <nav style={styles.navbar}>
      {/* Logo and Brand Name */}
      <div style={styles.logoContainer} onClick={() => navigate("/")}>
        <img src={logoPath} alt="Logo" style={styles.logo} />
        <span style={styles.brandName}>BugByte</span>
      </div>

      {/* Profile, Search, and Logout */}
      <div style={styles.rightContainer}>
        <img
          src={searchIconPath} // Add search icon
          alt="Search"
          style={styles.searchIcon}
          onClick={goToSearch} // Navigate to search page
        />
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
    </nav>
  );
};

const styles = {
  navbar: {
    display: "flex",
    alignItems: "center",
    justifyContent: "space-between",
    padding: "10px 10%",
    backgroundColor: "#099154",
    color: "white",
    height: "80px",
  },
  logoContainer: {
    display: "flex",
    alignItems: "center",
    cursor: "pointer",
  },
  logo: {
    color: "#ffffff",
    height: "80px",
    width: "80px",
    marginRight: "10px",
  },
  brandName: {
    fontSize: "1.5rem",
    fontWeight: "bold",
    userSelect: "none" as const,
  },
  rightContainer: {
    display: "flex",
    alignItems: "center",
  },
  searchIcon: {
    height: "40px",
    width: "40px",
    marginRight: "10px",
    cursor: "pointer", // Add cursor for clickable effect
  },
  profileIcon: {
    height: "50px",
    width: "50px",
    borderRadius: "50%",
    marginRight: "10px",
    cursor: "pointer",
  },
  logoutButton: {
    backgroundColor: "#ff4757",
    color: "white",
    border: "none",
    borderRadius: "5px",
    padding: "8px 16px",
    fontSize: "1rem",
    cursor: "pointer",
  },
};

export default Navbar;
