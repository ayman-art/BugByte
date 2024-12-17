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
import { IReply } from '../../types';

interface ReplyProps extends IReply{

}

const Reply: React.FC<ReplyProps> = ({ replyId, answerId, opName, postedOn, mdContent }) => {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  
  const navigate = useNavigate();
  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = localStorage.getItem('is_admin') === 'true';



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
            markdown={mdContent}
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

        <p className="reply-date">on {postedOn}</p>

        <footer className="reply-footer"> 
          {/* Action buttons */}
          <div className="reply-actions">
            {canEdit && (
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
        initialData={{ content: mdContent }}
        type="md-only"
      />
    </div>
  );
};

export default Reply;
