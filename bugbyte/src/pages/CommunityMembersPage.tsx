import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { fetchCommunityMembers } from "../API/CommunityAPI.tsx"; // Replace with actual API call to fetch members
import { Link } from "react-router-dom"; // Import Link for navigation

interface UserProfile {
  userName: string;
  reputation: number;
  followers: number;
  following: number;
  bio: string;
  is_following: boolean;
  is_admin: boolean; // Add this to match the fetched user profile structure
  profile_picture?: string; // New optional field for profile picture
}

const CommunityMembersPage: React.FC = () => {
  const { communityId } = useParams(); // Get the communityId from the URL
  const [members, setMembers] = useState<UserProfile[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  // Fetch community members based on the communityId
  const fetchMembers = async () => {
    if (!communityId) return; // Handle case where communityId might not exist

    try {
      const response = await fetchCommunityMembers(communityId);
      setMembers(response);
      console.log(members);
    } catch (error) {
      console.error("Error fetching community members:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMembers();
  }, [communityId]); // Re-fetch if communityId changes

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div style={styles.container}>
      <h1 style={styles.title}>Community Members</h1>
      <div style={styles.membersList}>
        {members.map((member) => (
          <div key={member.userName} style={styles.memberCard}>
            {/* Profile Picture */}
            {member.profile_picture && (
              <img
                src={member.profile_picture}
                alt={member.userName}
                style={styles.profilePicture}
              />
            )}
            <div style={styles.memberInfo}>
              {/* Clickable Username */}
              <Link to={`/Profile/${member.userName}`} style={styles.username}>
                {member.userName}
              </Link>
              <p style={styles.bio}>{member.bio}</p>
              <div style={styles.stats}>
                <span>Reputation: {member.reputation}</span>
              </div>
              {/* Follow/Unfollow button */}
              {/* <button style={styles.followButton}>
                {member.is_following ? "Unfollow" : "Follow"}
              </button> */}
              {/* Admin Badge */}
              {member.is_admin && <span style={styles.adminBadge}>Admin</span>}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    marginTop: "20px",
    width: "100%",
    padding: "0 10px",
  },
  title: {
    fontSize: "32px",
    fontWeight: "bold",
    marginBottom: "30px",
    color: "#333",
    textAlign: "center",
  },
  membersList: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    width: "100%",
  },
  memberCard: {
    display: "flex",
    flexDirection: "row",
    margin: "15px 0",
    padding: "15px",
    border: "1px solid #ddd",
    borderRadius: "8px",
    width: "80%",
    maxWidth: "650px",
    justifyContent: "space-between",
    alignItems: "center",
    backgroundColor: "#fafafa",
    transition: "transform 0.3s, box-shadow 0.3s",
  },
  profilePicture: {
    width: "60px",
    height: "60px",
    borderRadius: "50%",
    marginRight: "20px",
    objectFit: "cover",
  },
  memberInfo: {
    flex: 1,
    textAlign: "left",
    padding: "5px",
  },
  username: {
    fontSize: "22px",
    fontWeight: "bold",
    color: "#007BFF",
    textDecoration: "none",
    cursor: "pointer",
    transition: "color 0.3s",
  },
  bio: {
    fontSize: "14px",
    color: "#555",
    marginTop: "8px",
  },
  stats: {
    display: "flex",
    gap: "15px",
    marginTop: "12px",
    fontSize: "14px",
    color: "#666",
  },
  followButton: {
    marginTop: "12px",
    padding: "8px 16px",
    backgroundColor: "#007BFF",
    color: "#fff",
    border: "none",
    borderRadius: "4px",
    cursor: "pointer",
    transition: "background-color 0.3s",
  },
  followButtonHover: {
    backgroundColor: "#0056b3",
  },
  adminBadge: {
    marginTop: "12px",
    padding: "5px 10px",
    backgroundColor: "#FF5733",
    color: "#fff",
    borderRadius: "4px",
    fontWeight: "bold",
  },
};

export default CommunityMembersPage;
