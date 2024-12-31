import React, { ReactNode, useState } from "react";
import Navbar from "../components/NavBar";
import { useNavigate } from "react-router-dom";
import CommunityModal from "../components/CommunityModal";

interface LayoutProps {
  children: ReactNode;
  onLogout: () => void;
}

interface CommunityDetails {
  name: string;
  description?: string;
  tags?: string[];
}

const Layout: React.FC<LayoutProps> = ({ children, onLogout }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const handleCreateCommunity = async (communityDetails: CommunityDetails) => {
    console.log("Community Details:", communityDetails);
  };

  const navigate = useNavigate();
  const visitProfile = () => {
    const username = localStorage.getItem("name");
    navigate(`/Profile/${username}`);
  };

  const handleLogout = () => {
    onLogout();
    navigate("/");
  };

  const isAdmin = localStorage.getItem("is_admin") === "true";

  return (
    <div style={styles.container}>
      {/* Navbar */}
      <Navbar onLogout={handleLogout} />

      {/* Main Content */}
      <div style={styles.main}>
        {/* Sidebar Links on Left */}
        <aside style={styles.sidebar}>
          <center>
            <ul style={styles.links}>
              <li onClick={() => navigate("/")}>Home</li>
              <li onClick={visitProfile}>Profile</li>
              <li onClick={() => navigate("/communities")}>Communities</li>
              <li onClick={() => navigate("/joined-communities")}>
                Joined Communities
              </li>
              {isAdmin && (
                <li
                  onClick={() => setIsModalOpen(true)}
                  style={styles.createCommunityButton}
                >
                  Create Community
                </li>
              )}
            </ul>
          </center>
        </aside>

        {/* Page Content in Center */}
        <main style={styles.content}>{children}</main>

        {/* Modal for Creating Community */}
        <CommunityModal
          isOpen={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          onSave={handleCreateCommunity}
        />
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    display: "flex",
    flexDirection: "column",
    backgroundColor: "#f5f5f5",
  },
  main: {
    display: "flex",
    flex: 1,
    padding: "0 1%",
    backgroundColor: "#f5f5f5",
  },
  sidebar: { width: "200px", padding: "10px", backgroundColor: "#f5f5f5" },
  links: {
    listStyle: "none",
    padding: 0,
    display: "flex",
    flexDirection: "column",
    gap: "10px",
    cursor: "pointer",
  },
  createCommunityButton: {
    color: "#4caf50",
    fontWeight: "bold",
    cursor: "pointer",
  },
  content: {
    flex: 1,
    backgroundColor: "white",
    padding: "20px",
    marginLeft: "20px",
    borderRadius: "5px",
    boxShadow: "0 2px 5px rgba(0, 0, 0, 0.1)",
  },
  modalOverlay: {
    position: "fixed",
    top: 0,
    left: 0,
    width: "100%",
    height: "100%",
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    zIndex: 1000,
  },
  modal: {
    backgroundColor: "white",
    borderRadius: "10px",
    padding: "20px",
    width: "400px",
    boxShadow: "0 4px 10px rgba(0, 0, 0, 0.2)",
  },
  formGroup: { marginBottom: "15px" },
  input: {
    width: "100%",
    padding: "10px",
    borderRadius: "5px",
    border: "1px solid #ccc",
  },
  textarea: {
    width: "100%",
    padding: "10px",
    borderRadius: "5px",
    border: "1px solid #ccc",
  },
  modalActions: { display: "flex", justifyContent: "space-between" },
  createButton: {
    backgroundColor: "#4caf50",
    color: "white",
    border: "none",
    borderRadius: "5px",
    padding: "10px 15px",
    cursor: "pointer",
  },
  cancelButton: {
    backgroundColor: "#f44336",
    color: "white",
    border: "none",
    borderRadius: "5px",
    padding: "10px 15px",
    cursor: "pointer",
  },
};

export default Layout;
