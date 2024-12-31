import React, { useState, useEffect } from "react";
import { Community } from "../Models/Community.tsx";
import { fetchJoinedCommunities } from "../API/HomeAPI.tsx";
import CommunityPreview from "../components/CommunityPreview.tsx";

const JoinedCommunitiesPage: React.FC = () => {
  const [joinedCommunities, setJoinedCommunities] = useState<Community[]>([]);
  const [searchValue, setSearchValue] = useState<string>("");

  const fetchJoinedComm = async () => {
    try {
      let comms = await fetchJoinedCommunities();
      comms.reverse();
      setJoinedCommunities(comms);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    fetchJoinedComm();
  }, []);

  // Filter communities based on search input
  const filteredCommunities = joinedCommunities.filter(
    (comm) => comm.name.toLowerCase().includes(searchValue.toLowerCase()) // Assuming community has a 'name' property
  );

  // Handle input change for search bar
  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value);
  };

  return (
    <div style={styles.container}>
      {/* Search Section */}
      <div style={styles.searchSection}>
        <input
          type="text"
          value={searchValue}
          onChange={handleSearchChange}
          placeholder="Search joined communities..."
          style={styles.searchInput}
        />
      </div>

      {/* Display filtered communities */}
      {filteredCommunities.map((comm) => (
        <CommunityPreview key={comm.id} community={comm} />
      ))}
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
  },
  searchSection: {
    marginBottom: "20px",
    display: "flex",
    justifyContent: "center",
    width: "100%",
  },
  searchInput: {
    padding: "8px",
    marginRight: "8px",
    borderRadius: "4px",
    border: "2px solid #ccc",
    width: "85%",
    height: "50px",
  },
};

export default JoinedCommunitiesPage;
