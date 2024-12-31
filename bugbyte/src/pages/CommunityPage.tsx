import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  getCommunity,
  getCommunityPosts,
  joinCommunity,
  LeaveCommunity,
  deleteCommunity,
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
  const [joined, setJoined] = useState<boolean>(false);
  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [showDropdown, setShowDropdown] = useState<boolean>(false);
  const { communityId } = useParams<{ communityId: string }>();
  const limit = 5;
  const [page, setPage] = useState<number>(0);
  const [postList, setPostList] = useState<Post[]>([]);
  const [hasMore, setHasMore] = useState(true);
  const navigate = useNavigate();

  const fetchPosts = async () => {
    setLoading(true);
    try {
      const jwt = localStorage.getItem("authToken");
      const data = await getCommunityPosts(
        jwt!,
        parseInt(communityId!),
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
        setPostList((prevPosts) => {
          const uniquePosts = [
            ...prevPosts,
            ...posts.filter((post) => !prevPosts.some((p) => p.id === post.id)),
          ];
          return uniquePosts;
        });
        setPage((prevPage) => prevPage + 1);
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
        if (!jwt) throw new Error("User is not authenticated");

        const data: Community = await getCommunity(jwt, parseInt(communityId!));
        setCommunity(data);

        const joinedCommunities = JSON.parse(
          localStorage.getItem("joinedCommunities") || "[]"
        );
        setJoined(
          joinedCommunities.some(
            (joinedCommunity: { id: number }) => joinedCommunity.id === data.id
          )
        );

        const isAdmin = localStorage.getItem("is_admin") === "true";
        setIsAdmin(isAdmin);

        await fetchPosts();
      } catch (err: any) {
        setError(
          err.message || "An error occurred while fetching community data."
        );
      } finally {
        setLoading(false);
      }
    };

    fetchCommunityData();
  }, [communityId]);

  const handleJoinClick = async () => {
    const token = localStorage.getItem("authToken");

    joinCommunity(token!, community?.id!);
    setJoined(true);
  };

  const handleLeaveClick = async () => {
    const token = localStorage.getItem("authToken");
    try {
      await LeaveCommunity(token!, community?.name!);
      setJoined(false);
    } catch (e) {
      alert(e);
    }
  };

  const handleDeleteCommunity = async () => {
    const confirmed = window.confirm(
      `Are you sure that you want to delete the community "${community?.name}"?`
    );
    if (!confirmed) return;

    try {
      const token = localStorage.getItem("authToken");
      if (!community) return;
      await deleteCommunity(token, community.id);
      navigate("/communities");
      alert("Community deleted successfully");
    } catch (error) {
      console.error("Failed to delete community:", error);
      alert("Failed to delete community");
    }
  };

  const toggleDropdown = () => {
    setShowDropdown((prev) => !prev);
  };

  const styles: { [key: string]: React.CSSProperties } = {
    container: {
      padding: "20px",
      fontFamily: "Arial, sans-serif",
      width: "100%",
      minHeight: "100vh",
      backgroundColor: "#f9f9f9",
    },
    header: {
      display: "flex",
      justifyContent: "space-between",
      alignItems: "center",
    },
    dropdown: {
      position: "relative",
    },
    dropdownButton: {
      backgroundColor: "#4CAF50",
      color: "white",
      border: "none",
      padding: "10px 15px",
      borderRadius: "5px",
      cursor: "pointer",
    },
    dropdownContent: {
      display: showDropdown ? "block" : "none",
      position: "absolute",
      top: "100%",
      right: 0,
      backgroundColor: "white",
      border: "1px solid #ccc",
      borderRadius: "5px",
      boxShadow: "0 4px 8px rgba(0, 0, 0, 0.1)",
      zIndex: 1,
    },
    dropdownItem: {
      padding: "10px 15px",
      cursor: "pointer",
      color: "#d32f2f",
      textAlign: "center",
    },
    button: {
      padding: "10px 15px",
      fontSize: "14px",
      color: "white",
      backgroundColor: "#4CAF50",
      border: "none",
      borderRadius: "5px",
      cursor: "pointer",
      marginTop: "20px",
    },
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div style={styles.container}>
      {community && (
        <div>
          <div style={styles.header}>
            <h1>{community.name}</h1>
            {isAdmin && (
              <div style={styles.dropdown}>
                <button style={styles.dropdownButton} onClick={toggleDropdown}>
                  Options
                </button>
                <div style={styles.dropdownContent}>
                  <div
                    style={styles.dropdownItem}
                    onClick={handleDeleteCommunity}
                  >
                    Delete Community
                  </div>
                </div>
              </div>
            )}
          </div>
          <p>{community.description}</p>
          {!joined ? (
            <button style={styles.button} onClick={handleJoinClick}>
              Join Community
            </button>
          ) : (
            <button
              style={{ ...styles.button, backgroundColor: "#d32f2f" }}
              onClick={handleLeaveClick}
            >
              Leave Community
            </button>
          )}
        </div>
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
