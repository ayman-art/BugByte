import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { FaEdit, FaTrash } from 'react-icons/fa'; // Importing the icons

interface QuestionProps {
  postId: string;
  title: string;  // Added title prop
  questionText: string;
  tags: string[];
  upvotes: number; // Separate count for upvotes
  downvotes: number; // Separate count for downvotes
  opName: string;
  date: string;
  communityId: string;
  communityName: string;
}

const Question: React.FC<QuestionProps> = ({
  title,  // Destructure title from props
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

  const handleUpvote = () => {
    setCurrentUpvotes(currentUpvotes + 1);
  };

  const handleDownvote = () => {
    setCurrentDownvotes(currentDownvotes + 1);
  };

  const handleNavigateToProfile = () => {
    navigate(`/Profile/${opName}`);
  };

  return (
    <div className="question-container">
      <div className="question-content">
        <header className="question-header">
          {/* Display the title of the question */}
          <h2 className="question-title">{title}</h2> {/* Added title here */}
          
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
            <p>{questionText}</p>
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

          {/* Voting section */}
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

          {/* Edit and Delete buttons with symbols */}
          <div className="question-actions">
            <button className="action-button edit-button">
              <FaEdit /> {/* Edit icon */}
            </button>
            <button className="action-button delete-button">
              <FaTrash /> {/* Delete icon */}
            </button>
          </div>
        </footer>
      </div>
    </div>
  );
};

export default Question;
