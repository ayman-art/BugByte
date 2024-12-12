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
}) => {
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

  // Check if the logged-in user is the post owner or an admin
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
            <button onClick={handleUpvote} className="vote-button vote-button-up">
              ↑
            </button>
            <span className="votes-count">{currentDownvotes} </span>
            <button onClick={handleDownvote} className="vote-button vote-button-down">
              ↓
            </button>
          </div>

          <div className="question-actions">
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

export default Question;
