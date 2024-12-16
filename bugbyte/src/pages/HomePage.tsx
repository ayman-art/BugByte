import React, { useState } from "react";
import PostListing from "../components/PostListing";
import { getFeed } from "../API/FeedApi";

interface Post {
  id: string;
  communityId: number;
  title: string;
  creatoruserName: string;
  mdContent: string;
  upVotes: number;
  downVotes: number;
}

const HomePage: React.FC = () => {
  const [posts, setPosts] = useState<Post[]>([]); // List of posts
  const [page, setPage] = useState(1); // Current page
  const [loading, setLoading] = useState(false); // Loading state
  const [hasMore, setHasMore] = useState(true); // If more posts are available

  // Fetch posts from the API
  const fetchPosts = async () => {
    if (loading || !hasMore) return;

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

  return (
    <div>
      <h1>Home Page</h1>
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
