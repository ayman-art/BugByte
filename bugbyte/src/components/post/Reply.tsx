import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

interface ReplyProps {
  id: string;
  text: string;
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
}

const Reply: React.FC<ReplyProps> = ({ text, upvotes, downvotes, opName, date }) => {
  const [currentUpvotes, setCurrentUpvotes] = useState(upvotes);
  const [currentDownvotes, setCurrentDownvotes] = useState(downvotes);

  const handleUpvote = () => {
    setCurrentUpvotes(currentUpvotes + 1);
  };

  const handleDownvote = () => {
    setCurrentDownvotes(currentDownvotes + 1);
  };

  const navigate = useNavigate();
  const handleNavigateToProfile = () => {
    navigate(`/Profile/${opName}`);
  };

  return (
    <div className="reply-container">
      <div className="reply-content">
        <header className="reply-header">
          <p className="op-name">
            Replied by: <span className="op-link" onClick={handleNavigateToProfile}>{opName}</span>
          </p>
          <section className="reply-body">
            <p>{text}</p>
          </section>
        </header>

        <p className="reply-date">on {date}</p>

        <footer className="reply-footer">
          {/* Voting section */}
          <div className="reply-votes">
            <span className="votes-count">{currentUpvotes} </span>
            <button onClick={handleUpvote} className="vote-button vote-button-up">
              ↑
            </button>
            <span className="votes-count">{currentDownvotes} </span>
            <button onClick={handleDownvote} className="vote-button vote-button-down">
              ↓
            </button>
          </div>
        </footer>
      </div>
    </div>
  );
};

export default Reply;
