import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash } from 'react-icons/fa';
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
import { editReply } from '../../API/PostAPI';

interface ReplyProps extends IReply{
  onDelete: (replyId: string, answerId: string) => void;
}

const Reply: React.FC<ReplyProps> = ({ replyId, answerId, opName, postedOn, mdContent, onDelete }) => {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const navigate = useNavigate();

  const token = localStorage.getItem('authToken');
  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = localStorage.getItem('is_admin') === 'true';
  const canEdit = loggedInUsername === opName;
  const canDelete = loggedInUsername === opName || isAdmin;



  const handleEditSave = async (postDetails: { content: string }) => {
    await editReply(replyId, postDetails.content, token!);
    setIsEditModalOpen(false);
  }


  return (
    <div className="reply-container">
      <div className="reply-content">
        <header className="reply-header">
          <p className="op-name">
            Replied by: <span className="op-link" onClick={() => {navigate(`/Profile/${opName}`)}}>{opName}</span>
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

        <p className="post-date">on {new Date(postedOn).toLocaleString('en-US')}</p>

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
              <button className="action-button delete-button" onClick={() => onDelete(replyId, answerId)}>
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
