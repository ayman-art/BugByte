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

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(event.target.value);
  };

  const handleTagChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTagValue(event.target.value);
  };

  const fetchCommunities = async (reset: boolean = false) => {
    try {
      if (!hasMore && !reset) return;

      setLoading(true);

      const newPage = reset ? 0 : page;
      const fetchedCommunities = await sendRequest(
        searchValue,
        tagValue,
        "community",
        newPage,
        size
      );

      setCommunities((prev) =>
        reset ? fetchedCommunities : [...prev, ...fetchedCommunities]
      );
      setHasMore(fetchedCommunities.length === size);
      setPage(reset ? 1 : newPage + 1);
    } catch (error) {
      console.error("Error fetching communities:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleSearchClick = async () => {
    if (searchValue === "" && tagValue === "") return;
    await fetchCommunities(true); // Reset the list when a new search is performed
  };

  return (
    <div style={styles.container}>
      {/* Search and Tag Fields */}
      <div style={styles.searchSection}>
        <SearchAndTagFields
          searchValue={searchValue}
          tagValue={tagValue}
          onSearchChange={handleSearchChange}
          onTagChange={handleTagChange}
          onSearchClick={handleSearchClick}
        />
      </div>

      {/* Community Listing */}
      <CommunityListing
        comms={communities}
        fetchCommunities={() => fetchCommunities(false)} // Fetch more data for infinite scrolling
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
