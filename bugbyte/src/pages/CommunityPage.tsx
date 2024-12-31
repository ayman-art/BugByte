import React, { useState, useEffect } from "react";
import {
  getCommunity,
  getCommunityPosts,
  joinCommunity,
  LeaveCommunity,
  updateCommunity,
} from "../API/CommunityAPI";
import { useNavigate, useParams } from "react-router-dom";
import PostListing, { Post } from "../components/PostListing";
import CommunityModal from "../components/CommunityModal";

export interface Community {
  id: number;
  name: string;
  adminId: number;
  description: string;
  creationDate: string;
  tags: string[];
}

interface CommunityDetails {
  name: string;
  description?: string;
  tags?: string[];
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
  const navigate = useNavigate();

  const [showEditModal, setShowEditModal] = useState(false);
  const userId = localStorage.getItem("id");

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
        console.log("...");
        setPostList((prevPosts) => {
          const mergedPosts = [...prevPosts, ...posts];
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

  console.log("=================================");
  console.log(userId);
  console.log(community?.adminId);
  console.log("=================================");
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
  const handleLeaveClick = async () => {
    const token = localStorage.getItem("authToken");
    try {
      await LeaveCommunity(token!, community?.name!);
      setJoined(false);
    } catch (e) {
      alert(e);
    }
  };

  const handleViewMembers = () => {
    navigate(`/community-members/${communityId}`);
  };

  const handleEditSave = async (updatedData: CommunityDetails) => {
    try {
      const jwt = localStorage.getItem("authToken");
      if (!jwt) throw new Error("User is not authenticated");

      const updatedCommunity = await updateCommunity(
        jwt,
        community?.id!,
        updatedData
      );
      setCommunity(updatedCommunity); // Update local state
      setShowEditModal(false); // Close modal
      alert("Community updated successfully!");
    } catch (err: any) {
      console.error(err.message || "Failed to update community");
      alert("An error occurred while updating the community.");
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
    viewMembersButton: {
      marginTop: "10px",
      padding: "8px 16px",
      backgroundColor: "#007BFF",
      color: "#fff",
      border: "none",
      borderRadius: "4px",
      cursor: "pointer",
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

            <button
              onClick={handleViewMembers}
              style={styles.viewMembersButton}
            >
              View Members
            </button>
          </div>

          <div style={{ padding: "20px" }}>
            {community && (
              <>
                {userId === String(community.adminId) && (
                  <button
                    style={{
                      margin: "10px",
                      padding: "10px",
                      backgroundColor: "#4CAF50",
                      color: "white",
                      border: "none",
                      borderRadius: "5px",
                    }}
                    onClick={() => setShowEditModal(true)}
                  >
                    Edit Community
                  </button>
                )}
              </>
            )}

            {/* Edit Modal */}
            <CommunityModal
              isOpen={showEditModal}
              onClose={() => setShowEditModal(false)}
              onSave={handleEditSave}
              initialData={{
                name: community?.name || "",
                description: community?.description || "",
                tags: community?.tags,
              }}
            />
          </div>

          {/* Join or Leave Button */}
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
