import React, { useState } from "react";
import { ArrowUp, ArrowDown } from "lucide-react";
import { upvotePost, downvotePost } from "../API/QuestionAPI";

interface CommunityPostProps {
  postId: string;
  communityName: string;
  authorName: string;
  content?: string;
  upvotes: number;
  downvotes: number;
  tags?: string[]; // New prop for tags list
}

const CommunityPost: React.FC<CommunityPostProps> = ({
  postId,
  communityName = "Community Name",
  authorName = "Author Name",
  content = "",
  upvotes: initialUpvotes = 150,
  downvotes: initialDownvotes = 30,
  tags = [], // Default empty array for tags
}) => {
  const [upvotes, setUpvotes] = useState(initialUpvotes);
  const [downvotes, setDownvotes] = useState(initialDownvotes);

  const handleVote = async (type: "up" | "down") => {
    if (type === "up") {
      const success = await upvotePost(postId);
      if (success) setUpvotes((prev) => prev + 1);
    } else if (type === "down") {
      const success = await downvotePost(postId);
      if (success) setDownvotes((prev) => prev + 1);
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

    window.location.href = `/posts/${postId}`;
  };

  const stopPropagation = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  return (
    <div className="p-4">
      <div>
        <div style={styles.container} onClick={handleContentClick}>
          <div style={styles.postBox}>
            <a onClick={stopPropagation} style={styles.postNaming}>
              Community: {communityName} <br />
            </a>
          </div>

          <div style={styles.postBox}>
            <a
              href={`/Profile/${authorName}`}
              onClick={stopPropagation}
              style={styles.postNaming}
            >
              OP: {authorName}
            </a>
          </div>

          <div style={styles.content}>{content}</div>

          {/* Tags Section */}
          {tags != null && tags.length > 0 && (
            <div style={styles.tagsContainer} onClick={stopPropagation}>
              {tags.map((tag, index) => (
                <span key={index} style={styles.tag}>
                  {tag}
                </span>
              ))}
            </div>
          )}

          {/* Voting Buttons */}
          <div
            onClick={stopPropagation}
            style={{ display: "flex", gap: "8px" }}
          >
            <button
              onClick={() => handleVote("up")}
              style={styles.upVoteButton}
            >
              <ArrowUp />
              <span>{upvotes}</span>
            </button>
            <button
              onClick={() => handleVote("down")}
              style={styles.downVoteButton}
            >
              <ArrowDown />
              <span>{downvotes}</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  postBox: {
    border: "3px solid #B1E9A3",
    borderRadius: "10px",
    color: "black",
    margin: "5px",
  },

  postNaming: {
    marginLeft: "10px",
    textDecoration: "none",
    color: "black",
    margin: "5px",
  },

  container: {
    height: "auto",
    width: "787px",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    padding: "20px",
    boxSizing: "border-box",
    border: "3px solid #4caf50",
    borderRadius: "10px",
    backgroundColor: "#ffffff",
    margin: "5px",
  },

  content: {
    background: "#F4F4F4",
    minHeight: "160px",
    width: "100%",
    borderRadius: "10px",
    padding: "10px",
    overflow: "hidden",
    textOverflow: "ellipsis",
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    margin: "5px",
  },

  tagsContainer: {
    display: "flex",
    flexWrap: "wrap",
    gap: "10px",
    marginTop: "8px",
  },

  tag: {
    backgroundColor: "#e8f5e9",
    color: "#1b5e20",
    padding: "4px 12px",
    borderRadius: "12px",
    fontSize: "0.85rem",
    fontWeight: "500",
    cursor: "pointer",
    transition: "background-color 0.2s",
    margin: "5px",
  },

  tagHover: {
    backgroundColor: "#c8e6c9",
  },

  upVoteButton: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    gap: "8px",
    width: "90px",
    padding: "8px 12px",
    backgroundColor: "#e8f5e9",
    border: "1px solid #a5d6a7",
    borderRadius: "8px",
    cursor: "pointer",
    color: "#1b5e20",
    fontWeight: "bold",
    transition: "background-color 0.2s, transform 0.1s",
    margin: "5px",
  },

  upVoteHover: {
    backgroundColor: "#c8e6c9",
  },

  downVoteButton: {
    display: "flex",
    alignItems: "center",
    justifyContent: "center",
    gap: "8px",
    width: "90px",
    padding: "8px 12px",
    backgroundColor: "#ffebee",
    border: "1px solid #ef9a9a",
    borderRadius: "8px",
    cursor: "pointer",
    color: "#b71c1c",
    fontWeight: "bold",
    transition: "background-color 0.2s, transform 0.1s",
    margin: "5px",
  },

  downVoteHover: {
    backgroundColor: "#ffcdd2",
  },
};
export default CommunityPost;
