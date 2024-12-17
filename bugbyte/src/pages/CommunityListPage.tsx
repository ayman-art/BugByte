import React, { useState } from "react";
import { Community } from "../Models/Community.tsx";
import { useNavigate } from "react-router-dom"; // Import useNavigate for programmatic navigation
import CommunityListing from "../components/CommunityListing.tsx";
import { fetchCommunities } from "../API/CommunityAPI.tsx";

const CommunityListPage: React.FC = () => {
  const [searchValue, setSearchValue] = useState("");
  const [tagValue, setTagValue] = useState("");
  const [communities, setCommunities] = useState<Community[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [hasMore, setHasMore] = useState<boolean>(true);
  const [page, setPage] = useState<number>(0);
  const size = 10;

  const navigate = useNavigate(); // Initialize the navigate function

  const fetchComm = async () => {
    setLoading(true);

    try {
      const token = localStorage.getItem("authToken");
      const comms = await fetchCommunities(token!, page, size);

      if (comms.length === 0) {
        setHasMore(false); // No more posts
      } else {
        setCommunities((prevPosts) => [...prevPosts, ...comms]); // Append new posts
        setPage((prevPage) => prevPage + 1); // Increment page
      }
    } catch (error) {
      console.error("Failed to fetch posts:", error);
    } finally {
      setLoading(false);
    }
  };

  // Function to handle search button click
  const handleSearchClick = () => {
    navigate(`/search-community`); // Navigate to the search page with query parameter
  };

  return (
    <div style={styles.container}>
      {/* Search Section */}
      <div style={styles.searchSection}>
        <button onClick={handleSearchClick} style={styles.searchButton}>Search</button>
      </div>

      {/* Community Listing */}
      <CommunityListing
        comms={communities}
        fetchCommunities={fetchComm} // Fetch more data for infinite scrolling
        loading={loading}
        hasMore={hasMore}
      />
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
    border: "1px solid #ccc",
    width: "250px",
  },
  searchButton: {
    padding: "8px 16px",
    borderRadius: "4px",
    backgroundColor: "#007BFF",
    color: "#fff",
    border: "none",
    cursor: "pointer",
  },
};

export default CommunityListPage;
