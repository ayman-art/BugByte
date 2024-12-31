import React, { useEffect, useState } from "react";
import PostListing from "../components/PostListing";
import { fetchRecommendedCommunities, getFeed } from "../API/FeedApi";
import { fetchJoinedCommunities } from "../API/HomeAPI";
import { useNavigate } from "react-router-dom";
import { Community } from "./CommunityPage";

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
  isUpVoted: boolean;
  isDownVoted: boolean;
}

const HomePage: React.FC = () => {
  const [posts, setPosts] = useState<Question[]>([]); // List of posts
  const [page, setPage] = useState(1); // Current page
  const [loading, setLoading] = useState(false); // Loading state
  const [hasMore, setHasMore] = useState(true); // If more posts are available
  const [recommendedCommunities, setRecommendedCommunities] = useState<
    Community[]
  >([]);
  const navigate = useNavigate();
  let size = 10;
  const token = localStorage.getItem("authToken");

  // Fetch posts from the API
  const fetchPosts = async () => {
    setLoading(true);
    try {
      console.log("FEEED");
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

  // Fetch recommended communities
  const fetchRecommended = async () => {
    try {
      const communities = await fetchRecommendedCommunities(token!);
      setRecommendedCommunities(communities);
    } catch (error) {
      console.error("Failed to fetch recommended communities:", error);
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
        await fetchRecommended();
      } catch (error) {
        console.error("Error during initialization:", error);
      }
    };

    initialize();
  }, []);

  // Handle navigation to the community page
  const navigateToCommunity = (id: number) => {
    navigate(`/communities/${id}`);
  };

  return (
    <div style={styles.homeContainer}>
      <div style={styles.mainContent}>
        <PostListing
          posts={posts}
          fetchPosts={fetchPosts}
          loading={loading}
          hasMore={hasMore}
        />
      </div>

      <div style={styles.sidebar}>
        <h2 style={styles.sidebarTitle}>Recommended Communities</h2>
        <ul style={styles.communityList}>
          {recommendedCommunities.map((community) => (
            <li
              key={community.id}
              onClick={() => navigateToCommunity(community.id)}
              style={styles.communityItem}
            >
              <h3 style={styles.communityName}>{community.name}</h3>
              <p style={styles.communityDescription}>{community.description}</p>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

const styles = {
  homeContainer: {
    display: "flex",
    justifyContent: "space-between",
    marginTop: "20px",
    padding: "0 20px",
    backgroundColor: "#F7F8FA",
  },
  mainContent: {
    flexGrow: 1,
    marginRight: "20px",
  },
  sidebar: {
    width: "300px",
    padding: "20px",
    backgroundColor: "#fff",
    borderRadius: "8px",
    boxShadow: "0 2px 12px rgba(0, 0, 0, 0.1)",
    // position: "sticky",
    top: "20px",
  },
  sidebarTitle: {
    fontSize: "1.8rem",
    marginBottom: "10px",
    color: "#333",
    fontWeight: 600,
  },
  communityList: {
    listStyle: "none",
    padding: "0",
    margin: "0",
  },
  communityItem: {
    padding: "15px",
    marginBottom: "15px",
    backgroundColor: "#F9F9F9",
    borderRadius: "6px",
    cursor: "pointer",
    transition: "all 0.3s ease",
    boxShadow: "0 2px 10px rgba(0, 0, 0, 0.05)",
  },
  communityItemHover: {
    backgroundColor: "#EAEAEA",
  },
  communityName: {
    fontSize: "1.3rem",
    color: "#0056b3",
    marginBottom: "5px",
    fontWeight: 500,
  },
  communityDescription: {
    color: "#777",
    fontSize: "0.9rem",
    lineHeight: "1.4",
    marginTop: "5px",
  },
};

export default HomePage;
