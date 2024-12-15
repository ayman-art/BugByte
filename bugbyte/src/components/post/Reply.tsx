import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash } from 'react-icons/fa'; // Importing icons for Edit and Delete buttons
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

interface ReplyProps {
  id: string;
  answerId: string;
  text: string;
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
}

const Reply: React.FC<ReplyProps> = ({ text, upvotes, downvotes, opName, date }) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upvotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downvotes);
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

  const handleEditSave = (postDetails: { content: string }) => {
    console.log(postDetails);
    setIsEditModalOpen(false);
  }

  // Navigate to the user's profile
  const handleNavigateToProfile = () => {
    navigate(`/Profile/${opName}`);
  };

  // Check if the logged-in user is the post owner
  const canEdit = loggedInUsername === opName;
  const canDelete = loggedInUsername === opName || isAdmin; // Admin can delete as well

  return (
    <div className="reply-container">
      <div className="reply-content">
        <header className="reply-header">
          <p className="op-name">
            Replied by: <span className="op-link" onClick={handleNavigateToProfile}>{opName}</span>
          </p>
        </header>

        {/* Use MDXEditor for markdown reply content */}
        <section className="reply-body">
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

        <p className="reply-date">on {date}</p>

        <footer className="reply-footer"> 
          {/* Action buttons */}
          <div className="reply-actions">
            { (
              <button className="action-button edit-button"
                onClick={() => setIsEditModalOpen(true)}>
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

export default Reply;
