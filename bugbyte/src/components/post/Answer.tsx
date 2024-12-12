import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash } from 'react-icons/fa'; // Importing the icons
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

interface AnswerProps {
  id: string;
  postId: string;
  text: string; // Markdown content

  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
}

const Answer: React.FC<AnswerProps> = ({ text, upvotes, downvotes, opName, date }) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upvotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downvotes);
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
              <button className="action-button edit-button">
                <FaEdit /> {/* Edit icon */}
              </button>
            )}

            {canDelete && (
              <button className="action-button delete-button">
                <FaTrash /> {/* Delete icon */}
              </button>
            )}
          </div>
        </footer>
      </div>
    </div>
  );
};

export default Answer;
