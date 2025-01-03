import React, { useState, useEffect, useRef } from "react";
import { useNavigate } from "react-router-dom";
import { FaEdit, FaTrash, FaReply, FaEllipsisV } from "react-icons/fa";
import { IAnswer, IQuestion } from "../../types/index";
import {
  isModerator,
  isModeratorByName,
  isAdminByName,
  removeModerator,
  removeMember,
  setModerator,
} from "../../API/ModeratorApi";
import {
  MDXEditor,
  headingsPlugin,
  listsPlugin,
  quotePlugin,
  linkPlugin,
  imagePlugin,
  tablePlugin,
  markdownShortcutPlugin,
  thematicBreakPlugin,
  linkDialogPlugin,
  codeBlockPlugin,
  sandpackPlugin,
  codeMirrorPlugin,
} from "@mdxeditor/editor";
import "@mdxeditor/editor/style.css";
import PostModal from "../PostModal";
import imageUploadHandler, {
  languages,
  simpleSandpackConfig,
} from "../../utils/MDconfig";
import {
  downvoteQuestion,
  editQuestion,
  postAnswer,
  removeDownvoteQuestion,
  removeUpvoteQuestion,
  upvoteQuestion,
} from "../../API/PostAPI";

interface QuestionProps extends IQuestion {
  onDelete: (questionId: number) => void;
  onReply: (answer: IAnswer) => void;
}

const Question: React.FC<QuestionProps> = ({
  questionId,
  upVotes,
  mdContent,
  isDownVoted,
  title,
  isUpVoted,
  downVotes,
  tags = [],
  opName,
  postedOn,
  communityName,
  communityId,
  onDelete,
  onReply,
}) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upVotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downVotes);
  const [voteStatus, setVoteStatus] = useState(
    isUpVoted ? "upvoted" : isDownVoted ? "downvoted" : "neutral"
  );
  const [isReplyModalOpen, setIsReplyModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isUserModerator, setIsUserModerator] = useState(false);
  const [isAuthorModerator, setIsAuthorModerator] = useState(false);
  const [isAuthorAdmin, setIsAuthorAdmin] = useState(false);
  const [markdownState, setMarkdownState] = useState(mdContent);

  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const navigate = useNavigate();
  const dropdownRef = useRef<HTMLDivElement>(null);

  const token = localStorage.getItem("authToken");
  const loggedInUsername = localStorage.getItem("name") || "";
  const isAdmin = localStorage.getItem("is_admin") === "true";

  useEffect(() => {
    const fetchModeratorStatus = async () => {
      if (token) {
        try {
          const result = await isModerator(token, communityId);
          setIsUserModerator(result);
          const result2 = await isModeratorByName(token, communityId, opName);
          setIsAuthorModerator(result2);
          const result3 = await isAdminByName(token, opName);
          setIsAuthorAdmin(result3);
        } catch (error) {
          console.error("Failed to fetch moderator status:", error);
        }
      }
    };

    fetchModeratorStatus();
  }, [token, communityId, opName]);
  useEffect(() => {
    setCurrentUpvotes(upVotes);
    setCurrentDownvotes(downVotes);
    setVoteStatus(
      isUpVoted ? "upvoted" : isDownVoted ? "downvoted" : "neutral"
    );
  }, [questionId]);
  const handleSetModerator = async () => {
    try {
      await setModerator(token!, opName, communityId);
      setIsAuthorModerator(true);
      setIsDropdownOpen(false);

      console.log("Setting as moderator");
    } catch (error) {
      console.error("Error setting as moderator:", error);
    }
  };

  const handleRemoveModerator = async () => {
    try {
      await removeModerator(token!, communityId, opName);
      setIsAuthorModerator(false);
      setIsDropdownOpen(false);
      console.log("Removing moderator");
    } catch (error) {
      console.error("Error removing moderator:", error);
    }
  };

  const handleRemoveMember = async () => {
    try {
      await removeMember(token!, communityId, opName);
      setIsDropdownOpen(false);
      console.log("Removing member");
    } catch (error) {
      console.error("Error removing member:", error);
    }
  };

  // Voting Functions
  const handleUpvoteQuestion = async () => {
    if (voteStatus === "upvoted") {
      await handleUpvoteFromUpvoted();
    } else if (voteStatus === "downvoted") {
      await handleUpvoteFromDownvoted();
    } else {
      await handleUpvoteFromNeutral();
    }
  };

  const handleUpvoteFromUpvoted = async () => {
    await removeUpvoteQuestion(questionId, token!);
    setCurrentUpvotes(currentUpvotes - 1);
    setVoteStatus("neutral");
  };

  const handleUpvoteFromDownvoted = async () => {
    await removeDownvoteQuestion(questionId, token!);
    setCurrentDownvotes(currentDownvotes - 1);
    await upvoteQuestion(questionId, token!);
    setCurrentUpvotes(currentUpvotes + 1);
    setVoteStatus("upvoted");
  };

  const handleUpvoteFromNeutral = async () => {
    await upvoteQuestion(questionId, token!);
    setCurrentUpvotes(currentUpvotes + 1);
    setVoteStatus("upvoted");
  };

  const handleDownvoteQuestion = async () => {
    if (voteStatus === "downvoted") {
      await handleDownvoteFromDownvoted();
    } else if (voteStatus === "upvoted") {
      await handleDownvoteFromUpvoted();
    } else {
      await handleDownvoteFromNeutral();
    }
  };

  const handleDownvoteFromDownvoted = async () => {
    await removeDownvoteQuestion(questionId, token!);
    setCurrentDownvotes(currentDownvotes - 1);
    setVoteStatus("neutral");
  };

  const handleDownvoteFromUpvoted = async () => {
    await removeUpvoteQuestion(questionId, token!);
    setCurrentUpvotes(currentUpvotes - 1);
    await downvoteQuestion(questionId, token!);
    setCurrentDownvotes(currentDownvotes + 1);
    setVoteStatus("downvoted");
  };

  const handleDownvoteFromNeutral = async () => {
    await downvoteQuestion(questionId, token!);
    setCurrentDownvotes(currentDownvotes + 1);
    setVoteStatus("downvoted");
  };

  const handleReplySave = async (postDetails: { content: string }) => {
    console.log("Reply posted:", postDetails);
    if (!token || !loggedInUsername) {
      alert("No auth token found. Please log in.");
      return;
    } else if (!postDetails.content || postDetails.content.trim() === "") {
      alert("Answer content cannot be empty.");
      return;
    }
    const answerId = await postAnswer(postDetails.content, questionId, token!);
    onReply({
      answerId,
      questionId,
      opName: loggedInUsername,
      postedOn: new Date().toLocaleString("en-US"),
      upVotes: 0,
      mdContent: postDetails.content,
      isDownVoted: false,
      isUpVoted: false,
      downVotes: 0,
      canVerify: false,
      isVerified: false,
      enabledVerify: true,
    });
  };

  const handleEditSave = async (postDetails: {
    content: string;
    title: string;
    tags: string[];
  }) => {
    if (!postDetails.content.trim() || !postDetails.title.trim()) {
      alert("Title and content cannot be empty.");
      return;
    }
    await editQuestion(
      questionId,
      postDetails.title,
      postDetails.content,
      postDetails.tags,
      token!
    );
    console.log("Question edited:", postDetails);
    setMarkdownState(postDetails.content);
    setIsEditModalOpen(false);
  };

  const canEdit = loggedInUsername === opName;
  const canDelete = loggedInUsername === opName || isAdmin || isUserModerator;

  const toggleDropdown = () => {
    setIsDropdownOpen((prev) => !prev);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        dropdownRef.current &&
        !dropdownRef.current.contains(event.target as Node) &&
        !event.target!.closest(".question-actions")
      ) {
        setIsDropdownOpen(false);
      }
    };

    document.addEventListener("click", handleClickOutside);
    return () => {
      document.removeEventListener("click", handleClickOutside);
    };
  }, []);

  return (
    <div className="question-container">
      <div className="question-content">
        <header className="question-header">
          <h2 className="question-title">{title}</h2>
          <p className="community-tag">
            <span
              className="tag tag-community"
              onClick={() => {
                navigate(`/communities/${communityId}`);
              }}
            >
              c/{communityName}
            </span>
          </p>
          <p className="op-name">
            Asked by:{" "}
            <span
              onClick={() => {
                navigate(`/Profile/${opName}`);
              }}
              className="op-link"
            >
              {opName}
            </span>
          </p>
          <section className="question-body">
            <MDXEditor
              key={markdownState}
              markdown={markdownState}
              readOnly
              plugins={[
                headingsPlugin(),
                listsPlugin(),
                quotePlugin(),
                linkPlugin(),
                imagePlugin({ imageUploadHandler }),
                tablePlugin(),
                codeBlockPlugin({ defaultCodeBlockLanguage: "js" }),
                sandpackPlugin({ sandpackConfig: simpleSandpackConfig }),
                codeMirrorPlugin(languages),
                linkDialogPlugin(),
                thematicBreakPlugin(),
                markdownShortcutPlugin(),
              ]}
            />
          </section>
        </header>

        <p className="post-date">
          on {new Date(postedOn).toLocaleString("en-US")}
        </p>

        <footer className="question-footer">
          <div className="question-tags">
            {tags.map((tag, index) => (
              <span key={index} className="tag">
                {tag}
              </span>
            ))}
          </div>

          <div className="question-votes">
            <span className="votes-count">{currentUpvotes} </span>
            <button
              onClick={handleUpvoteQuestion}
              className={`vote-button ${voteStatus === "upvoted" ? "active" : ""} vote-button-up`}
            >
              ↑
            </button>
            <span className="votes-count">{currentDownvotes} </span>
            <button
              onClick={handleDownvoteQuestion}
              className={`vote-button ${voteStatus === "downvoted" ? "active" : ""} vote-button-down`}
            >
              ↓
            </button>
          </div>

          <div className="question-actions">
            {canEdit && (
              <button
                className="action-button edit-button"
                onClick={() => setIsEditModalOpen(true)}
              >
                <FaEdit />
              </button>
            )}

            {canDelete && (
              <button
                className="action-button delete-button"
                onClick={() => onDelete(questionId)}
              >
                <FaTrash />
              </button>
            )}

            <button
              className="action-button reply-button"
              onClick={() => setIsReplyModalOpen(true)}
            >
              <FaReply />
            </button>

            {/* Dropdown Menu */}
            {(isAdmin || isUserModerator) && !isAuthorAdmin && (
              <button
                className="action-button more-button"
                onClick={toggleDropdown}
              >
                <FaEllipsisV />
              </button>
            )}

            {isDropdownOpen && !isAuthorAdmin && (
              <div ref={dropdownRef} className="dropdown-menu show">
                {!isAuthorModerator && isAdmin ? (
                  <button onClick={handleSetModerator}>Set Moderator</button>
                ) : null}
                {isAuthorModerator && isAdmin ? (
                  <button onClick={handleRemoveModerator}>
                    Remove Moderator
                  </button>
                ) : null}
                <button onClick={handleRemoveMember}>Remove Member</button>
              </div>
            )}
          </div>
        </footer>
      </div>

      <PostModal
        isOpen={isReplyModalOpen}
        onClose={() => setIsReplyModalOpen(false)}
        onSave={handleReplySave}
        type="md-only"
      />

      <PostModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        onSave={handleEditSave}
        initialData={{ content: markdownState, title, tags }}
        type="no-community"
      />
    </div>
  );
};

export default Question;
