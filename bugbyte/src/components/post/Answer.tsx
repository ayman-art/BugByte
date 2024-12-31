import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash, FaReply, FaCheckCircle } from 'react-icons/fa';
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
import imageUploadHandler, { languages, simpleSandpackConfig } from '../../utils/MDconfig';
import { IAnswer, IReply } from '../../types';
import { downvoteAnswer, postReply, removeDownvoteAnswer, removeUpvoteAnswer, upvoteAnswer } from '../../API/PostAPI';
import { isModerator } from '../../API/ModeratorApi';

interface AnswerProps extends IAnswer {
  onDelete: (answerId: string) => void;
  onVerify: (answerId: string) => void;
  onReplyOnAnswer: (reply: IReply) => void;
}
const Answer: React.FC<AnswerProps> = ({
  answerId,
  questionId,
  opName,
  postedOn,
  upVotes,
  mdContent,
  isDownVoted,
  isUpVoted,
  downVotes,
  isVerified,
  enabledVerify,
  canVerify,
  onDelete,
  onVerify,
  onReplyOnAnswer,
}) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upVotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downVotes);
  const [voteStatus, setVoteStatus] = useState(isUpVoted ? 'upvoted' : isDownVoted ? 'downvoted' : 'neutral');
  const [isReplyModalOpen, setIsReplyModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [verified, setVerified] = useState(isVerified);
  const [enableVerifyState, setEnableVerify] = useState(enabledVerify); // State for enabling/disabling verify button
  const [isUserModerator, setIsUserModerator] = useState(false); // New state for moderator status

  const navigate = useNavigate();

  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = localStorage.getItem('is_admin') === 'true';
  const token = localStorage.getItem('authToken');

  //  // Fetch moderator status
  //   useEffect(() => {
  //     const fetchModeratorStatus = async () => {
  //       if (token) {
  //         try {
  //           const result = await isModerator(token, communityId);
  //           setIsUserModerator(result);
  //         } catch (error) {
  //           console.error('Failed to fetch moderator status:', error);
  //         }
  //       }
  //     };

  //     fetchModeratorStatus();
  //   }, [token, communityId]);

  const handleUpvoteAnswer = async() => {
    if (voteStatus === 'upvoted') {
      await handleUpvoteFromUpvoted();
    } else if (voteStatus === 'downvoted') {
      await handleUpvoteFromDownvoted();
    } else {
      await handleUpvoteFromNeutral();
    }
  };

  const handleUpvoteFromUpvoted = async () => {
    await removeUpvoteAnswer(answerId, token!);
    setCurrentUpvotes(currentUpvotes - 1);
    setVoteStatus('neutral');
  }

  const handleUpvoteFromDownvoted = async () => {
    await removeDownvoteAnswer(answerId, token!);
    setCurrentDownvotes(currentDownvotes - 1);
    await upvoteAnswer(answerId, token!);
    setCurrentUpvotes(currentUpvotes + 1);
    setVoteStatus('upvoted');
  }

  const handleUpvoteFromNeutral = async () => {
    await upvoteAnswer(answerId, token!);
    setCurrentUpvotes(currentUpvotes + 1);
    setVoteStatus('upvoted');
  }



  const handleDownvoteAnswer = async () => {
    if (voteStatus === 'downvoted') {
      await handleDownvoteFromDownvoted();
    } else if (voteStatus === 'upvoted') {
      await handleDownvoteFromUpvoted();
    } else {
      await handleDownvoteFromNeutral();
    }
  };

  const handleDownvoteFromDownvoted = async () => {
    await removeDownvoteAnswer(answerId, token!);
    setCurrentDownvotes(currentDownvotes - 1);
    setVoteStatus('neutral');
  }

  const handleDownvoteFromUpvoted = async () => {
    await removeUpvoteAnswer(answerId, token!);
    setCurrentUpvotes(currentUpvotes - 1);
    await downvoteAnswer(answerId, token!);
    setCurrentDownvotes(currentDownvotes + 1);
    setVoteStatus('downvoted');
  }

  const handleDownvoteFromNeutral = async () => {
    await downvoteAnswer(answerId, token!);
    setCurrentDownvotes(currentDownvotes + 1);
    setVoteStatus('downvoted');
  }


  const handleReplySave = async (postDetails: { content: string }) => {

    if (!token) {
      alert('No auth token found. Please log in.');
      return;
    } else if (!postDetails.content || postDetails.content.trim() === '') {
      alert('Reply content cannot be empty.');
      return;
    }

    console.log('Reply saved:', postDetails);
    const replyId = await postReply(postDetails.content, answerId, token!);
    onReplyOnAnswer({
      replyId,
      answerId,
      opName: loggedInUsername,
      postedOn: new Date().toLocaleString('en-US'),
      mdContent: postDetails.content,
    });

    setIsReplyModalOpen(false);
  };

  const handleEditSave = (postDetails: { content: string }) => {
    console.log('Post edited:', postDetails);
    setIsEditModalOpen(false);
  };

  const canEdit = loggedInUsername === opName;
  const canDelete = loggedInUsername === opName || isAdmin || isUserModerator;
  console.log('isAdmin:', isAdmin);
  console.log(loggedInUsername, opName);

  return (
    <div className={`answer-container ${isVerified ? 'verified' : ''}`}>
      <div className="answer-content">
        <header className="answer-header">
          <p className="op-name">
            Answered by:{' '}
            <span onClick={() => {navigate(`/Profile/${opName}`)}} className="op-link">
              {opName}
            </span>
            {isVerified && (
              <span className="verified-badge" title="Verified">
                <FaCheckCircle />
              </span>
            )}
          </p>
        </header>

        <section className="answer-body">
          <MDXEditor
            markdown={mdContent}
            readOnly
            plugins={[
              headingsPlugin(),
              listsPlugin(),
              quotePlugin(),
              linkPlugin(),
              imagePlugin({ imageUploadHandler }),
              tablePlugin(),
              codeBlockPlugin({ defaultCodeBlockLanguage: 'js' }),
              sandpackPlugin({ sandpackConfig: simpleSandpackConfig }),
              codeMirrorPlugin(languages),
              linkDialogPlugin(),
              thematicBreakPlugin(),
              markdownShortcutPlugin(),
            ]}
          />
        </section>

        <p className="post-date">on {new Date(postedOn).toLocaleString('en-US')}</p>

        <footer className="answer-footer">
          <div className="answer-votes">
          <span className="votes-count">{currentUpvotes} </span>
            <button
              onClick={handleUpvoteAnswer}
              className={`vote-button ${voteStatus === 'upvoted' ? 'active' : ''} vote-button-up`}
            >
              ↑
            </button>
            <span className="votes-count">{currentDownvotes} </span>
            <button
              onClick={handleDownvoteAnswer}
              className={`vote-button ${voteStatus === 'downvoted' ? 'active' : ''} vote-button-down`}
            >
              ↓
            </button>
          </div>

          <div className="answer-actions">
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
                onClick={() => onDelete(answerId)}
              >
                <FaTrash />
              </button>
            )}

            {canVerify &&  enabledVerify && (
              <button
                className="action-button verify-button"
                onClick={()=>onVerify(answerId)}
              >
                Verify
              </button>
            )}

            <button
              className="action-button reply-button"
              onClick={() => setIsReplyModalOpen(true)}
            >
              <FaReply />
            </button>
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
        type="md-only"
      />
    </div>
  );
};

export default Answer;
