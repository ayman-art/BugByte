import React, { useEffect, useState } from "react";
import PostListing from "../components/PostListing";
import { getFeed } from "../API/FeedApi";
import { fetchJoinedCommunities } from "../API/HomeAPI";

interface Question {
  id: string;
  creatorUserName: string;
  mdContent: string;
  postedOn: Date;
  title: string;
  communityId: number;
  upVotes: number;
  downVotes: number;
  tags?: string[];
  communityName?: string;
  isUpvoted: boolean;
  isDownVoted: boolean;
}

const HomePage: React.FC = () => {
  const [posts, setPosts] = useState<Question[]>([]); // List of posts
  const [page, setPage] = useState(1); // Current page
  const [loading, setLoading] = useState(false); // Loading state
  const [hasMore, setHasMore] = useState(true); // If more posts are available
  let size = 10;

  // Fetch posts from the API
  const fetchPosts = async () => {
    setLoading(true);
    try {
      console.log("FEEED");
      const token = localStorage.getItem("authToken");
      const feed = await getFeed(token!);

      if (feed.length === 0) {
        setHasMore(false); // No more posts
      } else if (feed.length < size) {
        console.log(feed.length);
        setHasMore(false); // No more posts
        // setPosts((prevPosts) => [...prevPosts, ...feed]); // Append new posts

        setPosts((prevPosts) => {
          const mergedPosts = [...prevPosts, ...feed];
          const uniquePosts = Array.from(
            new Map(mergedPosts.map((post) => [post.id, post])).values()
          );
          return uniquePosts;
        });
        setPage((prevPage) => prevPage + 1); // Increment page
      } else {
        // setPosts((prevPosts) => [...prevPosts, ...feed]); // Append new posts
        setPosts((prevPosts) => {
          const mergedPosts = [...prevPosts, ...feed];
          const uniquePosts = Array.from(
            new Map(mergedPosts.map((post) => [post.id, post])).values()
          );
          return uniquePosts;
        });
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
        setPosts([]);
        setLoading(false);
        setHasMore(true);
        setPage(1);
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
