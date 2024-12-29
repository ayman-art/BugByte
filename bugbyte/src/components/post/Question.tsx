import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash, FaReply } from 'react-icons/fa';
import { IAnswer, IQuestion } from '../../types/index'
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
import { downvoteQuestion, postAnswer, removeDownvoteQuestion, removeUpvoteQuestion, upvoteQuestion } from '../../API/PostAPI';

interface QuestionProps extends IQuestion {
  onDelete: (questionId: string) => void;
  onReply: (answer: IAnswer) => void;
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
  onDelete,
  onReply
  
}) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upVotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downVotes);
  const [voteStatus, setVoteStatus] = useState(isUpVoted ? 'upvoted' : isDownVoted ? 'downvoted' : 'neutral');
  const [isReplyModalOpen, setIsReplyModalOpen] = useState(false);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const navigate = useNavigate();
  const loggedInUsername = localStorage.getItem('name') || '';
  const isAdmin = localStorage.getItem('is_admin') === 'true';
  
  const token = localStorage.getItem('authToken');

 
    // Use useEffect to update the state when props change
    useEffect(() => {
      setCurrentUpvotes(upVotes);
      setCurrentDownvotes(downVotes);
      setVoteStatus(isUpVoted ? 'upvoted' : isDownVoted ? 'downvoted' : 'neutral');
    }, [questionId]); // Add questionId to dependencies
  

  const handleUpvoteQuestion = async () => {
    if (voteStatus === 'upvoted') {
      await removeUpvoteQuestion(questionId, token!);
      setCurrentUpvotes(currentUpvotes - 1);
      setVoteStatus('neutral');
      console.log('FROM upvoted to neutral');
    } else if (voteStatus === 'downvoted') {
      await removeDownvoteQuestion(questionId, token!);
      setCurrentDownvotes(currentDownvotes - 1);
      await upvoteQuestion(questionId, token!);
      setCurrentUpvotes(currentUpvotes + 1);
      setVoteStatus('upvoted');
      console.log('FROM downvoted to upvoted');
    } else {
      await upvoteQuestion(questionId, token!);
      setCurrentUpvotes(currentUpvotes + 1);
      setVoteStatus('upvoted');
      console.log('FROM neutral to upvoted');
    }
  };

  const handleDownvoteQuestion = async () => {
    if (voteStatus === 'downvoted') {
      await removeDownvoteQuestion(questionId, token!);
      setCurrentDownvotes(currentDownvotes - 1);
      setVoteStatus('neutral');
      console.log('FROM downvoted to neutral');
    } else if (voteStatus === 'upvoted') {
      await removeUpvoteQuestion(questionId, token!);
      setCurrentUpvotes(currentUpvotes - 1);
      await downvoteQuestion(questionId, token!);
      setCurrentDownvotes(currentDownvotes + 1);
      setVoteStatus('downvoted');
      console.log('FROM upvoted to downvoted');
    } else {
      await downvoteQuestion(questionId, token!);
      setCurrentDownvotes(currentDownvotes + 1);
      setVoteStatus('downvoted');
      console.log('FROM neutral to downvoted');
    }
  };


  const handleReplySave = async (postDetails: { content: string }) => {
    console.log('Reply posted:', postDetails);
    if (!token || !loggedInUsername) {
      alert('No auth token found. Please log in.');
      return;
    } else if (!postDetails.content || postDetails.content.trim() === '') {
      alert('Answer content cannot be empty.');
      return;
    }
    const answerId = await postAnswer(postDetails.content, questionId, token!);
    onReply({
      answerId,
      questionId,
      opName: loggedInUsername,
      postedOn: new Date().toLocaleString('en-US'),
      upVotes: 0,
      mdContent: postDetails.content,
      isDownVoted: false,
      isUpVoted: false,
      downVotes: 0,
      isVerified: false,
      enabledVerify: true
    });
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
            <span className="tag tag-community" onClick={() =>{navigate(`/communities/${communityId}`)}}>c/{communityName}</span>
          </p>
          <p className="op-name">
            Asked by:{' '}
            <span onClick={() => {navigate(`/Profile/${opName}`)}} className="op-link">
              {opName}
            </span>
          </p>
          <section className="question-body">
            <MDXEditor
              key={mdContent}
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
        </header>

        <p className="question-date">on {new Date(postedOn).toLocaleString('en-US')}</p>

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
              <button className="action-button delete-button" onClick={() =>onDelete(questionId)}>
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
        initialData={{ content: mdContent, title, tags }}
        type="no-community"
      />
    </div>
  );
};

export default Question;
