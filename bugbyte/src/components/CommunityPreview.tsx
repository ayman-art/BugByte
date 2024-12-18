import React from "react";
import { Community } from "../Models/Community";
import { Link } from "react-router-dom"; // Import Link for navigation

interface CommunityPreviewProps {
  community: Community;
}

const CommunityPreview: React.FC<CommunityPreviewProps> = ({ community }) => {
  const { id, name, description, creationDate, tags } = community;

  return (
    <Link to={`/communities/${id}`} style={styles.link}> {/* Wrap the entire component with Link */}
      <div style={styles.container}>
        <h2 style={styles.name}>{name}</h2>
        <p style={styles.description}>{description}</p>
        <div style={styles.footer}>
          <span style={styles.creationDate}>
            Created on: {new Date(creationDate).toLocaleDateString()}
          </span>
          {tags && (
            <div style={styles.tags}>
              {tags.map((tag, index) => (
                <span key={index} style={styles.tag}>
                  {tag}
                </span>
              ))}
            </div>
          )}
        </div>
      </div>
    </Link>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  link: {
    textDecoration: "none", // Ensure no underline on the link
    color: "inherit", // Inherit color from the container
  },
  container: {
    border: "1px solid #ccc",
    borderRadius: "8px",
    padding: "16px",
    margin: "16px auto", // Keeps component centered with full width
    boxShadow: "0 2px 4px rgba(0, 0, 0, 0.1)",
    backgroundColor: "#fff",
    width: "800px", // Ensures it takes up full width
    boxSizing: "border-box",
  },
  name: {
    margin: "0 0 8px",
    fontSize: "1.5em",
    color: "#333",
  },
  description: {
    margin: "0 0 16px",
    fontSize: "1em",
    color: "#666",
  },
  footer: {
    display: "flex",
    justifyContent: "space-between",
    alignItems: "center",
    fontSize: "0.9em",
    color: "#999",
  },
  creationDate: {
    fontStyle: "italic",
  },
  tags: {
    display: "flex",
    flexWrap: "wrap",
    gap: "8px",
  },
  tag: {
    background: "#007BFF",
    color: "#fff",
    padding: "4px 8px",
    borderRadius: "12px",
    fontSize: "0.8em",
  },
};

export default CommunityPreview;
