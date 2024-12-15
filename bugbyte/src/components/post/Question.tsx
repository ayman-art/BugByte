import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash, FaReply } from 'react-icons/fa';
import { MDXEditor, 
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

interface QuestionProps {
  postId: string;
  title: string;
  questionText: string;
  tags: string[];
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
  communityId: string;
  communityName: string;
  initialVoteStatus: 'upvoted' | 'downvoted' | 'neutral'; // New prop for the initial vote status
}

const Question: React.FC<QuestionProps> = ({
  title,
  questionText,
  tags,
  upvotes,
  downvotes,
  opName,
  date,
  communityId,
  communityName,
  initialVoteStatus, // Access the new prop
}) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upvotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downvotes);
  const [voteStatus, setVoteStatus] = useState(initialVoteStatus || 'neutral'); // Track vote status
  const [isReplyModalOpen, setIsReplyModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const navigate = useNavigate();
  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = localStorage.getItem('is_admin') === 'true';

 

  const handleUpvoteQuestion = () => {
    if (voteStatus === 'upvoted') {
      // todo: call api(remove upvote)
      setCurrentUpvotes(currentUpvotes - 1);
      setVoteStatus('neutral');
      console.log('FROM upvoted to neutral');
    } else if (voteStatus === 'downvoted') {
      // call api(remove downvote, add upvote)
      setCurrentDownvotes(currentDownvotes - 1);
      setCurrentUpvotes(currentUpvotes + 1);
      setVoteStatus('upvoted');
      console.log('FROM downvoted to upvoted');
    } else {
      // call api(add upvote)
      setCurrentUpvotes(currentUpvotes + 1);
      setVoteStatus('upvoted');
      console.log('FROM neutral to upvoted');
    }
  };

  const handleDownvoteQuestion = () => {
    if (voteStatus === 'downvoted') {
      // todo: call api(remove downvote)
      setCurrentDownvotes(currentDownvotes - 1);
      setVoteStatus('neutral');
      console.log('FROM downvoted to neutral');
    } else if (voteStatus === 'upvoted') {
      // call api(remove upvote, add downvote)
      setCurrentUpvotes(currentUpvotes - 1);
      setCurrentDownvotes(currentDownvotes + 1);
      setVoteStatus('downvoted');
      console.log('FROM upvoted to downvoted');
    } else {
      // call api(add downvote)
      setCurrentDownvotes(currentDownvotes + 1);
      setVoteStatus('downvoted');
      console.log('FROM neutral to downvoted');
    }
  };

  const handleNavigateToProfile = () => {
    navigate(`/Profile/${opName}`);
  };

  const handleReplySave = (postDetails: { content: string }) => {
    console.log('Reply posted:', postDetails);
    // Add functionality to save reply here
  };

  const handleEditSave = (postDetails: { content: string }) => {
    console.log('Post edited:', postDetails);
    setIsEditModalOpen(false);
  };

  const canEdit = loggedInUsername === opName;
  const canDelete = loggedInUsername === opName || isAdmin;

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
              markdown={questionText}
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
        </header>

        <p className="question-date">on {date}</p>

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
              className={`vote-button ${voteStatus === 'upvoted' ? 'active' : ''} vote-button-up`}
            >
              ↑
            </button>
            <span className="votes-count">{currentDownvotes} </span>
            <button
              onClick={handleDownvoteQuestion}
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
              <button className="action-button delete-button">
                <FaTrash />
              </button>
            )}

            <button className="action-button reply-button" onClick={() => setIsReplyModalOpen(true)}>
              <FaReply /> {/* Reply icon */}
            </button>
          </div>
        </footer>
      </div>

      {/* Reply Modal */}
      <PostModal
        isOpen={isReplyModalOpen}
        onClose={() => setIsReplyModalOpen(false)}
        onSave={handleReplySave}
        type="md-only"
      />
      
      {/* Edit Modal */}
      <PostModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        onSave={handleEditSave}
        initialData={{ content: questionText }}
        type="no-community"
      />
    </div>
  );
};

export default Question;
