import React, { useEffect, useState } from "react";
import PostListing from "../components/PostListing";
import { getFeed } from "../API/FeedApi";
import { fetchJoinedCommunities } from "../API/HomeAPI";
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
  tags?: string[];
}

const HomePage: React.FC = () => {
  const [posts, setPosts] = useState<Question[]>([]); // List of posts
  const [page, setPage] = useState(1); // Current page
  const [loading, setLoading] = useState(false); // Loading state
  const [hasMore, setHasMore] = useState(true); // If more posts are available
  const [searchValue, setSearchValue] = useState("");
  const [tagValue, setTagValue] = useState("");
  const [questions, setQuestions] = useState<Question[]>([]);
  let size = 10;

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(event.target.value);
  };

  const handleTagChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setTagValue(event.target.value);
  };

  const handleSearchClick = async () => {
    try {
      if (!(searchValue === "" && tagValue === "")) {
        const fetchedQuestions = await sendRequest(
          searchValue,
          tagValue,
          "home",
          page,
          size
        );
        setQuestions(fetchedQuestions);
        console.log(fetchedQuestions);
        //setTagValue("");
        //setSearchValue("");
      }
    } catch (error) {
      console.error("Error fetching questions:", error);
    }
  };
  // Fetch posts from the API
  const fetchPosts = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("authToken");
      const feed = await getFeed(token!);

      if (feed.length === 0) {
        setHasMore(false); // No more posts
      } else {
        setPosts((prevPosts) => [...prevPosts, ...feed]); // Append new posts
        setPage((prevPage) => prevPage + 1); // Increment page
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
        await fetchJoinedCommunities();
        await fetchPosts(); // Initial post fetch
      } catch (error) {
        console.error("Error during initialization:", error);
      }
    };

    initialize();
  }, []);

  return (
    <div>
      <PostListing
        posts={posts}
        fetchPosts={fetchPosts}
        loading={loading}
        hasMore={hasMore}
      />
    </div>
  );
};

export default HomePage;
