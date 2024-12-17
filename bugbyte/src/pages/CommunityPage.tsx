import React, { useState, useEffect } from "react";
import {
  getCommunity,
  getCommunityPosts,
  joinCommunity,
} from "../API/CommunityAPI";
import { useParams } from "react-router-dom";
import PostListing, { Post } from "../components/PostListing";

export interface Community {
  id: number;
  name: string;
  description: string;
  creationDate: string;
}

const CommunityPage: React.FC = () => {
  const [community, setCommunity] = useState<Community | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [joined, setJoined] = useState<boolean>(false); // Track if the community is joined
  const { communityId } = useParams<{ communityId: string }>();
  const limit = 5;
  const [page, setPage] = useState<number>(0);
  const [postList, setPostList] = useState<Post[]>([]);
  const [hasMore, setHasMore] = useState(true); // If more posts are available

  const fetchPosts = async () => {
    setLoading(true);
    try {
      const jwt = localStorage.getItem("authToken");
      const data = await getCommunityPosts(
        jwt!,
        community?.id!,
        limit,
        page * limit
      );
      const posts: Post[] = data.map(
        (item: {
          questionId: string;
          opName: string;
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
        }) => ({
          id: item.questionId,
          creatorUserName: item.opName,
          mdContent: item.mdContent,
          postedOn: item.postedOn,
          title: item.title,
          communityId: item.communityId,
          upVotes: item.upVotes,
          downVotes: item.downVotes,
          tags: item.tags,
          communityName: item.communityName,
          isUpVoted: item.isUpVoted,
          isDownVoted: item.isDownVoted,
        })
      );
      console.log(posts);
      if (posts.length === 0) {
        setHasMore(false);
      } else {
        console.log("...");
        setPostList((prevPosts) => [...prevPosts, ...posts]); // Append new posts
        setPage((prevPage) => prevPage + 1); // Increment page
      }
    } catch (error) {
      console.error("Failed to fetch posts:", error);
    } finally {
      setLoading(false);
    }
  };
  useEffect(() => {
    const fetchCommunityData = async () => {
      try {
        const jwt = localStorage.getItem("authToken");

        if (!jwt) {
          throw new Error("User is not authenticated");
        }

        const data: Community = await getCommunity(jwt, parseInt(communityId!));
        setCommunity(data);

        // Check if the community is in the user's joined list
        const joinedCommunities = JSON.parse(
          localStorage.getItem("joinedCommunities") || "[]"
        );

        const isJoined = joinedCommunities.some(
          (joinedCommunity: { id: number }) => joinedCommunity.id === data.id
        );
        await fetchPosts();
        setJoined(isJoined); // Update the joined state
      } catch (err: any) {
        setError(err.message || "An error occurred");
      } finally {
        setLoading(false);
      }
    };

    fetchCommunityData();
  }, [communityId]);

  const handleJoinClick = async () => {
    // Add the community to the joined list
    //const joinedCommunities = JSON.parse(localStorage.getItem('joinedCommunities') || '[]');
    const token = localStorage.getItem("authToken");
    try {
      await joinCommunity(token!, community?.id!);

      //localStorage.setItem('joinedCommunities', JSON.stringify(joinedCommunities));
      setJoined(true);
    } catch (e) {
      alert(e);
    }
  };

  const styles: { [key: string]: React.CSSProperties } = {
    container: {
      padding: "20px",
      fontFamily: "Arial, sans-serif",
      width: "100%",
      minHeight: "100vh",
      backgroundColor: "#f9f9f9",
      boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
    },
    header: {
      textAlign: "center",
      color: "#333",
      marginBottom: "20px",
    },
    title: {
      fontSize: "24px",
      fontWeight: "bold",
      color: "#4CAF50",
    },
    label: {
      fontWeight: "bold",
      color: "#555",
    },
    value: {
      color: "#333",
      marginBottom: "10px",
    },
    divider: {
      margin: "20px 0",
      border: "none",
      height: "2px",
      backgroundColor: "#4CAF50",
    },
    error: {
      color: "#d32f2f",
      textAlign: "center",
    },
    loading: {
      color: "#888",
      textAlign: "center",
    },
    button: {
      padding: "8px 12px",
      fontSize: "14px",
      color: "white",
      backgroundColor: "#4CAF50",
      border: "none",
      borderRadius: "4px",
      cursor: "pointer",
      marginTop: "20px",
      float: "right",
    },
  };

  if (loading) return <div style={styles.loading}>Loading...</div>;
  if (error) return <div style={styles.error}>Error: {error}</div>;

  return (
    <div style={styles.container}>
      {community ? (
        <div>
          <h1 style={styles.title}>{community.name}</h1>
          <div>
            <p style={styles.value}>
              <span style={styles.label}>Description:</span>{" "}
              {community.description}
            </p>
            <p style={styles.value}>
              <span style={styles.label}>Created on:</span>{" "}
              {new Date(community.creationDate).toLocaleDateString()}
            </p>
          </div>

          {/* Render Join Button only if not joined */}
          {!joined && (
            <button style={styles.button} onClick={handleJoinClick}>
              Join Community
            </button>
          )}

          <hr style={styles.divider} />
        </div>
      ) : (
        <p style={styles.value}>No community data available.</p>
      )}
      <PostListing
        posts={postList}
        fetchPosts={fetchPosts}
        loading={loading}
        hasMore={hasMore}
      />
    </div>
  );
};

export default CommunityPage;
