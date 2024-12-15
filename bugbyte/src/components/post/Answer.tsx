import React, { useEffect, useState } from 'react';
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

interface AnswerProps {
  id: string;
  postId: string;
  text: string; // Markdown content
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
  isVerified?: boolean;
  enabledVerify?: boolean; // Prop for enabling/disabling verify
  onDelete: (answerId: string) => void;
  onVerify: (answerId: string) => void;
}

const Answer: React.FC<AnswerProps> = ({
  id,
  text,
  upvotes,
  downvotes,
  opName,
  date,
  isVerified = false,  // default to false if not provided
  enabledVerify = true, // prop for enabling/disabling verify button
  onDelete,
  onVerify,
}) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upvotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downvotes);
  const [voteStatus, setVoteStatus] = useState<'upvoted' | 'downvoted' | 'neutral'>('neutral');
  const [isReplyModalOpen, setIsReplyModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [verified, setVerified] = useState(isVerified);
  const [enableVerifyState, setEnableVerify] = useState(enabledVerify); // State for enabling/disabling verify button
  const navigate = useNavigate();

  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = true; // Simulated admin check, replace as necessary

  const handleUpvoteAnswer = () => {
    if (voteStatus === 'upvoted') {
      setCurrentUpvotes((prev) => prev - 1);
      setVoteStatus('neutral');
    } else {
      setCurrentUpvotes((prev) => prev + 1);
      if (voteStatus === 'downvoted') {
        setCurrentDownvotes((prev) => prev - 1);
      }
      setVoteStatus('upvoted');
    }
  };

  const handleDownvoteAnswer = () => {
    if (voteStatus === 'downvoted') {
      setCurrentDownvotes((prev) => prev - 1);
      setVoteStatus('neutral');
    } else {
      setCurrentDownvotes((prev) => prev + 1);
      if (voteStatus === 'upvoted') {
        setCurrentUpvotes((prev) => prev - 1);
      }
      setVoteStatus('downvoted');
    }
  };

  const handleNavigateToProfile = () => {
    navigate(`/Profile/${opName}`);
  };

  const handleReplySave = (postDetails: { content: string }) => {
    console.log('Reply posted:', postDetails);
    setIsReplyModalOpen(false);
  };

  const handleEditSave = (postDetails: { content: string }) => {
    console.log('Post edited:', postDetails);
    setIsEditModalOpen(false);
  };

  const canEdit = loggedInUsername === opName;
  const canDelete = loggedInUsername === opName || isAdmin;

  // Verify the answer
  const handleVerify = () => {
    setVerified(true);
    onVerify(id);
  };
  useEffect(() => {
    setEnableVerify(enabledVerify);
  }, [enabledVerify]);

  return (
    <div className={`answer-container ${verified ? 'verified' : ''}`}>
      <div className="answer-content">
        <header className="answer-header">
          <p className="op-name">
            Answered by:{' '}
            <span onClick={handleNavigateToProfile} className="op-link">
              {opName}
            </span>
            {verified && (
              <span className="verified-badge" title="Verified">
                <FaCheckCircle />
              </span>
            )}
          </p>
        </header>

        <section className="answer-body">
          <MDXEditor
            markdown={text}
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

        <p className="answer-date">on {date}</p>

        <footer className="answer-footer">
          <div className="answer-votes">
            <span className="votes-count">{currentUpvotes}</span>
            <button
              onClick={handleUpvoteAnswer}
              className={`vote-button ${voteStatus === 'upvoted' ? 'active' : ''} vote-button-up`}
            >
              ↑
            </button>
            <span className="votes-count">{currentDownvotes}</span>
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
                onClick={() => onDelete(id)}
              >
                <FaTrash />
              </button>
            )}

            {isAdmin &&  enableVerifyState && (
              <button
                className="action-button verify-button"
                onClick={handleVerify}
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
        initialData={{ content: text }}
        type="md-only"
      />
    </div>
  );
};

export default Answer;
