import React, { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash, FaReply, FaEllipsisV } from 'react-icons/fa';
import { IQuestion } from '../../types/index';
import { isModerator,isModeratorByName ,isAdminByName, removeModerator ,removeMember,  setModerator } from '../../API/ModeratorApi';
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
} from '@mdxeditor/editor';
import '@mdxeditor/editor/style.css';
import PostModal from '../PostModal';
import { downvoteQuestion, removeDownvoteQuestion, removeUpvoteQuestion, upvoteQuestion } from '../../API/PostAPI';

interface QuestionProps extends IQuestion {
  onDelete: (questionId: string) => void;
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
  onDelete
}) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upVotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downVotes);
  const [voteStatus, setVoteStatus] = useState(isUpVoted ? 'upvoted' : isDownVoted ? 'downvoted' : 'neutral');
  const [isReplyModalOpen, setIsReplyModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [isUserModerator, setIsUserModerator] = useState(false);
   const [isAuthorModerator, setIsAuthorModerator] = useState(false);
   const [isAuthorAdmin, setIsAuthorAdmin] = useState(false);

  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const navigate = useNavigate();
  const dropdownRef = useRef<HTMLDivElement>(null);

  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = localStorage.getItem('is_admin') === 'true';
  const token = localStorage.getItem('authToken');

 useEffect(() => {
   const fetchModeratorStatus = async () => {
     if (token) {
       try {
         const result = await isModerator(token, communityId);
         setIsUserModerator(result);
         const result2 = await isModeratorByName(token, communityId, opName);
         setIsAuthorModerator(result2);
         const result3 = await isAdminByName(token , opName);
         setIsAuthorAdmin(result3);
       } catch (error) {
         console.error('Failed to fetch moderator status:', error);
       }
     }
   };

   fetchModeratorStatus();
 }, [token, communityId, opName]);

const handleSetModerator = async () => {
  try {
    await setModerator(token, opName, communityId);
    console.log('Setting as moderator');
  } catch (error) {
    console.error('Error setting as moderator:', error);
  }
};

const handleRemoveModerator = async () => {
  try {
    await removeModerator(token, opName, communityId);
    console.log('Removing moderator');
  } catch (error) {
    console.error('Error removing moderator:', error);
  }
};

const handleRemoveMember = async () => {
  try {
    await removeMember(token, opName, communityId);
    console.log('Removing member');
  } catch (error) {
    console.error('Error removing member:', error);
  }
};



  // Voting Functions
  const handleUpvote = () => {
    if (voteStatus === 'upvoted') {
      removeUpvoteQuestion(questionId, token!);
      setCurrentUpvotes(prev => prev - 1);
      setVoteStatus('neutral');
    } else if (voteStatus === 'downvoted') {
      removeDownvoteQuestion(questionId, token!);
      setCurrentDownvotes(prev => prev - 1);
      upvoteQuestion(questionId, token!);
      setCurrentUpvotes(prev => prev + 1);
      setVoteStatus('upvoted');
    } else {
      upvoteQuestion(questionId, token!);
      setCurrentUpvotes(prev => prev + 1);
      setVoteStatus('upvoted');
    }
  };

  const handleDownvote = () => {
    if (voteStatus === 'downvoted') {
      removeDownvoteQuestion(questionId, token!);
      setCurrentDownvotes(prev => prev - 1);
      setVoteStatus('neutral');
    } else if (voteStatus === 'upvoted') {
      removeUpvoteQuestion(questionId, token!);
      setCurrentUpvotes(prev => prev - 1);
      downvoteQuestion(questionId, token!);
      setCurrentDownvotes(prev => prev + 1);
      setVoteStatus('downvoted');
    } else {
      downvoteQuestion(questionId, token!);
      setCurrentDownvotes(prev => prev + 1);
      setVoteStatus('downvoted');
    }
  };

  const handleNavigateToProfile = () => {
    navigate(`/Profile/${opName}`);
  };

  const handleReplySave = (postDetails: { content: string }) => {
    console.log('Reply posted:', postDetails);
  };

  const handleEditSave = (postDetails: { content: string }) => {
    console.log('Post edited:', postDetails);
    setIsEditModalOpen(false);
  };

  const canEdit = loggedInUsername === opName;
  const canDelete = loggedInUsername === opName || isAdmin || isUserModerator;

  const toggleDropdown = () => {
    setIsDropdownOpen(prev => !prev);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node) && !event.target.closest('.question-actions')) {
        setIsDropdownOpen(false);
      }
    };

    document.addEventListener('click', handleClickOutside);
    return () => {
      document.removeEventListener('click', handleClickOutside);
    };
  }, []);

  return (
    <div className="question-container">
      <div className="question-content">
        <header className="question-header">
          <h2 className="question-title">{title}</h2>
          <p className="community-tag">
            <span className="tag tag-community">c/{communityName}</span>
          </p>
          <p className="op-name">
            Asked by:{' '}
            <span onClick={handleNavigateToProfile} className="op-link">
              {opName}
            </span>
          </p>
          <section className="question-body">
            <MDXEditor
              markdown={mdContent}
              readOnly
              plugins={[headingsPlugin, listsPlugin, quotePlugin, linkPlugin, imagePlugin, tablePlugin, markdownShortcutPlugin, thematicBreakPlugin, linkDialogPlugin, codeBlockPlugin, sandpackPlugin, codeMirrorPlugin]}
            />
          </section>
        </header>

        <p className="question-date">on {postedOn}</p>

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
              onClick={handleUpvote}
              className={`vote-button ${voteStatus === 'upvoted' ? 'active' : ''} vote-button-up`}
            >
              ↑
            </button>
            <span className="votes-count">{currentDownvotes} </span>
            <button
              onClick={handleDownvote}
              className={`vote-button ${voteStatus === 'downvoted' ? 'active' : ''} vote-button-down`}
            >
              ↓
            </button>
          </div>

          <div className="question-actions">
            {canEdit && (
              <button className="action-button edit-button" onClick={() => setIsEditModalOpen(true)}>
                <FaEdit />
              </button>
            )}

            {canDelete && (
              <button className="action-button delete-button" onClick={() => onDelete(questionId)}>
                <FaTrash />
              </button>
            )}

            <button className="action-button reply-button" onClick={() => setIsReplyModalOpen(true)}>
              <FaReply />
            </button>

            {/* Dropdown Menu */}
          {isAdmin && !isAuthorAdmin &&(
            <button className="action-button more-button" onClick={toggleDropdown}>
              <FaEllipsisV />
            </button>
          )}


          {isDropdownOpen && !isAuthorAdmin && (
            <div ref={dropdownRef} className="dropdown-menu show">
              {!isAuthorModerator ? (
                <button onClick={handleSetModerator}>Set Moderator</button>
              ) : null}
              {isAuthorModerator ? (
                <button onClick={handleRemoveModerator}>Remove Moderator</button>
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
        initialData={{ content: mdContent }}
        type="no-community"
      />
    </div>
  );
};

export default Question;
