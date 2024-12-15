import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash, FaReply } from 'react-icons/fa'; // Importing the icons
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
import imageUploadHandler, { languages, simpleSandpackConfig } from '../../utils/MDconfig';
import PostModal from '../PostModal';

interface AnswerProps {
  id: string;
  postId: string;
  text: string; // Markdown content
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
  onDelete: (answerId: string) => void;
}

const Answer: React.FC<AnswerProps> = ({ text, upvotes, downvotes, opName, date, onDelete, id }) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upvotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downvotes);
  const [isReplyModalOpen, setIsReplyModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const navigate = useNavigate();
  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = localStorage.getItem('is_admin') === 'true';

  const handleUpvote = () => {
    setCurrentUpvotes(currentUpvotes + 1);
  };

  const handleDownvote = () => {
    setCurrentDownvotes(currentDownvotes + 1);
  };

  const handleNavigateToProfile = () => {
    navigate(`/Profile/${opName}`);
  };

  const handleReplySave = (postDetails: { content: string }) => {
    console.log(postDetails);
    setIsReplyModalOpen(false);
  }

  const handleEditSave = (postDetails: { content: string }) => {
    console.log(postDetails);
    setIsEditModalOpen(false);
  }

  // Check if the logged-in user is the post owner
  const canEdit = loggedInUsername === opName;
  const canDelete = loggedInUsername === opName || isAdmin; // Allow admin to delete as well

  return (
    <div className="answer-container">
      <div className="answer-content">
        <header className="answer-header">
          <p className="op-name">
            Answered by:{" "}
            <span onClick={handleNavigateToProfile} className="op-link">
              {opName}
            </span>
          </p>
        </header>

        {/* Use MDXEditor for markdown answer content */}
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
              codeBlockPlugin({ defaultCodeBlockLanguage: "js" }),
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
          {/* Voting section */}
          <div className="answer-votes">
            <span className="votes-count">{currentUpvotes} </span>
            <button onClick={handleUpvote} className="vote-button vote-button-up">
              ↑
            </button>
            <span className="votes-count">{currentDownvotes} </span>
            <button onClick={handleDownvote} className="vote-button vote-button-down">
              ↓
            </button>
          </div>

          {/* Action buttons */}
          <div className="answer-actions">
            {canEdit && (
              <button className="action-button edit-button"
                onClick={() => setIsEditModalOpen(true)}>
                <FaEdit /> {/* Edit icon */}
              </button>
            )}

            {(
              <button
                className="action-button delete-button"
                onClick={() => onDelete(id)}
              >
                <FaTrash /> {/* Delete icon */}
              </button>
            )}
            <button
              className="action-button reply-button"
              onClick={() => setIsReplyModalOpen(true)}
            >
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
        initialData={{ content: text }}
        type="md-only"
      />
    </div>
  );
};

export default Answer;
