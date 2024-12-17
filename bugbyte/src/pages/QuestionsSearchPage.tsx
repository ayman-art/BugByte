import React, { useEffect, useState } from "react";
import PostListing from "../components/PostListing";
import SearchAndTagFields, {
  sendRequest,
} from "../components/SearchAndTagFields";

interface Question {
  id: string;
  communityId: number;
  title: string;
  creatorUserName: string;
  mdContent: string;
  upVotes: number;
  downVotes: number;
}

const QuestionSearchPage: React.FC = () => {
  const [posts, setPosts] = useState<Question[]>([]); // List of posts
  const [page, setPage] = useState(1); // Current page
  const [loading, setLoading] = useState(false); // Loading state
  const [hasMore, setHasMore] = useState(false); // If more posts are available
  const [searchValue, setSearchValue] = useState("");
  const [tagValue, setTagValue] = useState("");
  //   const [questions, setQuestions] = useState<Question[]>([]);
  let size = 10;

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(event.target.value);
  };

  const handleTagChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTagValue(event.target.value);
  };

  const handleSearchClick = async () => {
    try {
      console.log("CLICKED");
      setPosts([]);
      setLoading(false);
      setHasMore(true);
      setPage(0);

      await fetchPosts();
    } catch (error) {
      console.error("Error fetching questions:", error);
    }
  };
  // Fetch posts from the API
  const fetchPosts = async () => {
    setLoading(true);
    try {
      if (!(searchValue === "" && tagValue === "")) {
        const fetchedQuestions = await sendRequest(
          searchValue,
          tagValue,
          "home",
          page,
          size
        );
        if (fetchedQuestions.length === 0) {
          setHasMore(false); // No more posts
        } else {
          setPosts((prevPosts) => [...prevPosts, ...fetchedQuestions]); // Append new posts
          setPage((prevPage) => prevPage + 1); // Increment page
          console.log(page);
        }
        console.log(fetchedQuestions);
      }
    } catch (error) {
      console.error("Failed to fetch posts:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const initialize = async () => {
      try {
        setPage(0);
        setHasMore(false);
        await handleSearchClick();
        // await fetchPosts(); // Initial post fetch
      } catch (error) {
        console.error("Error during initialization:", error);
      }
    };

    initialize();
  }, []);

  return (
    <div>
      <div style={styles.searchSection}>
        <SearchAndTagFields
          searchValue={searchValue}
          tagValue={tagValue}
          onSearchChange={handleSearchChange}
          onTagChange={handleTagChange}
          onSearchClick={handleSearchClick}
        />
      </div>
      <PostListing
        posts={posts}
        fetchPosts={fetchPosts}
        loading={loading}
        hasMore={hasMore}
      />
    </div>
  );
};

export default QuestionSearchPage;

// Styles for the page
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
  questionCard: {
    border: "1px solid #ccc",
    padding: "10px",
    margin: "10px",
    borderRadius: "5px",
    width: "80%",
    boxSizing: "border-box",
  },
};
