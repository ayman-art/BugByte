import React, { useState } from "react";
import { ArrowUp, ArrowDown } from "lucide-react";

import {
  upvotePost,
  downvotePost,
  removeDownvoteQuestion,
  removeUpvoteQuestion,
} from "../API/QuestionAPI";

interface CommunityPostProps {
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

const CommunityPost: React.FC<CommunityPostProps> = ({
  id,
  creatorUserName,
  mdContent,
  postedOn,
  title,
  communityId,
  upVotes,
  downVotes,
  tags = [],
  communityName = "",
  isUpvoted,
  isDownVoted,
}) => {
  const [upvotes, setUpvotes] = useState(upVotes);
  const [downvotes, setDownvotes] = useState(downVotes);
  const [isUpvote, setIsUpvoted] = useState(isUpvoted);
  const [isDownvoted, setIsDownvoted] = useState(isDownVoted);
  const token = localStorage.getItem("authToken");
  if (isUpvoted) console.log("Upvoteddd");

  const handleVote = async (type: "up" | "down") => {
    if (type === "up") {
      if (isUpvote) {
        setUpvotes((prev) => prev - 1);
        removeUpvoteQuestion(id, token!);
        setIsUpvoted(false);
        return;
      }

      const success = await upvotePost(id, token!);
      if (success) {
        setUpvotes((prev) => prev + 1);

        if (isDownvoted) {
          setDownvotes((prev) => prev - 1);
          removeDownvoteQuestion(id, token!);
        }

        setIsUpvoted(true);
        setIsDownvoted(false);
      }
    } else if (type === "down") {
      if (isDownvoted) {
        setDownvotes((prev) => prev - 1);
        removeDownvoteQuestion(id, token!);
        setIsDownvoted(false);
        return;
      }

      const success = await downvotePost(id, token!);
      if (success) {
        setDownvotes((prev) => prev + 1);

        if (isUpvote) {
          setUpvotes((prev) => prev - 1);
          removeUpvoteQuestion(id, token!);
        }

        setIsDownvoted(true);
        setIsUpvoted(false);
      }
    }
  };

  const handleContentClick = (e: React.MouseEvent) => {
    if (
      (e.target as HTMLElement).closest(".vote-button") ||
      (e.target as HTMLElement).closest(".author-link") ||
      (e.target as HTMLElement).closest(".community-link")
    ) {
      return;
    }
    window.location.href = `/posts/${id}`;
  };

  const stopPropagation = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  return (
    <div style={styles.container} onClick={handleContentClick}>
      {/* Title */}
      <h2 style={styles.title}>{title}</h2>

      {/* Community Name */}
      <div>
        <a
          href={`/communities/${communityId}`}
          onClick={stopPropagation}
          style={styles.communityLink}
        >
          {communityName || `Community ${communityId}`}
        </a>
      </div>

      {/* Author and Date */}
      <div style={styles.metadata}>
        <a
          href={`/Profile/${creatorUserName}`}
          onClick={stopPropagation}
          style={styles.authorLink}
        >
          {creatorUserName}
        </a>
        <span style={styles.postedOn}>
          {new Date(postedOn).toLocaleDateString()}{" "}
          {new Date(postedOn).toLocaleTimeString()}
        </span>
      </div>

      {/* Content */}
      <div style={styles.content}>{mdContent}</div>

      {/* Tags */}
      {tags.length > 0 && (
        <div style={styles.tagsContainer}>
          {tags.map((tag, index) => (
            <span key={index} style={styles.tag}>
              #{tag}
            </span>
          ))}
        </div>
      )}

      {/* Voting Section */}
      <div style={styles.votingContainer}>
        <button
          className="vote-button"
          onClick={(e) => {
            e.stopPropagation();
            handleVote("up");
          }}
          style={{
            ...styles.voteButton,
            backgroundColor: isUpvote ? "#a5d6a7" : "#e8f5e9",
          }}
        >
          <ArrowUp />
          <span>{upvotes}</span>
        </button>
        <button
          className="vote-button"
          onClick={(e) => {
            e.stopPropagation();
            handleVote("down");
          }}
          style={{
            ...styles.voteButton,
            backgroundColor: isDownvoted ? "#ef9a9a" : "#ffebee",
          }}
        >
          <ArrowDown />
          <span>{downvotes}</span>
        </button>
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    width: "100%",
    maxWidth: "800px",
    margin: "20px auto",
    padding: "20px",
    border: "1px solid #ccc",
    borderRadius: "10px",
    backgroundColor: "#fff",
    boxShadow: "0 4px 6px rgba(0,0,0,0.1)",
    cursor: "pointer",
    transition: "transform 0.2s",
  },
  title: {
    fontSize: "1.5rem",
    fontWeight: "bold",
    color: "#333",
    marginBottom: "10px",
  },
  communityLink: {
    fontSize: "1rem",
    fontWeight: "500",
    color: "#4caf50",
    textDecoration: "none",
    marginBottom: "5px",
    display: "block",
  },
  metadata: {
    fontSize: "0.9rem",
    color: "#666",
    marginBottom: "15px",
    display: "flex",
    gap: "10px",
    alignItems: "center",
  },
  authorLink: {
    textDecoration: "none",
    color: "#1e88e5",
    fontWeight: "bold",
  },
  postedOn: {
    color: "#999",
  },
  content: {
    padding: "10px",
    backgroundColor: "#f4f4f4",
    borderRadius: "10px",
    marginBottom: "15px",
    color: "#333",
    fontSize: "1rem",
  },
  tagsContainer: {
    display: "flex",
    flexWrap: "wrap",
    gap: "8px",
    marginBottom: "15px",
  },
  tag: {
    backgroundColor: "#e8f5e9",
    color: "#1b5e20",
    padding: "5px 10px",
    borderRadius: "12px",
    fontSize: "0.85rem",
    fontWeight: "500",
    cursor: "pointer",
  },
  votingContainer: {
    display: "flex",
    gap: "10px",
  },
  voteButton: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    gap: "5px",
    padding: "10px 20px",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "bold",
    transition: "background-color 0.2s",
  },
};

export default CommunityPost;
