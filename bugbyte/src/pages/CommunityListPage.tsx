import React, { useState } from "react";
import { Community } from "../Models/Community.tsx";
import SearchAndTagFields, { sendRequest } from "../components/SearchAndTagFields.tsx";
import CommunityListing from "../components/CommunityListing.tsx";

const CommunitySearchPage: React.FC = () => {
  const [searchValue, setSearchValue] = useState("");
  const [tagValue, setTagValue] = useState("");
  const [communities, setCommunities] = useState<Community[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [hasMore, setHasMore] = useState<boolean>(true);
  const [page, setPage] = useState<number>(0);
  const size = 10;


  const fetchCommunities = async () => {
      setLoading(true);

     try {
           const token = localStorage.getItem("authToken");
           const comms = await getCommunities(token!);
     
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


  return (
    <div style={styles.container}>
      

      {/* Community Listing */}
      <CommunityListing
        comms={communities}
        fetchCommunities={fetchCommunities} // Fetch more data for infinite scrolling
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
  },
  searchSection: {
    marginBottom: "20px",
    display: "flex",
    justifyContent: "center",
    width: "100%",
  },
};

export default CommunitySearchPage;
