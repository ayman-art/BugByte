import React from 'react';
import { useParams } from 'react-router-dom';
import Question from '../components/post/Question';
import Answer from '../components/post/Answer';
import Reply from '../components/post/Reply'; // Import Reply component
import '../styles/PostPage.css';

interface QuestionProps {
  postId: string;
  title: string
  questionText: string;
  tags: string[];
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
  communityId: string;
  communityName: string;
}

interface AnswerProps {
  postId: string;
  text: string;
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
}

interface ReplyProps {
  id: string;
  text: string;
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
}

// Mock data for questions, answers, and replies
const questions: QuestionProps[] = [
  {
    postId: '1',
    title: "TITLEEEE",
    questionText: 'What is React and how does it work?',
    tags: ['React', 'JavaScript', 'Frontend'],
    upvotes: 10,
    downvotes: 2,
    opName: 'AymOsama',
    date: '2024-12-07',
    communityId: '123',
    communityName: 'React Community',
  },
  {
    postId: '2',
    title: "TITLEEEE",
    questionText:
      "Here We should display or not actually the MD file with all of its content actually. Here We should display the MD file with all of its content actually. Here We should display the MD file with all of its content actually.",
    tags: ['State Management', 'React', 'Hooks'],
    upvotes: 15,
    downvotes: 3,
    opName: 'Jane Smith',
    date: '2024-12-06',
    communityId: '456',
    communityName: 'React Developers',
  },
];

// Mock data for answers and replies
const answers: AnswerProps[] = [
  {
    postId: '1',
    text: 'React is a JavaScript library for building user interfaces, primarily for single-page applications.',
    upvotes: 5,
    downvotes: 1,
    opName: 'JohnDoe',
    date: '2024-12-07',
  },
  {
    postId: '1',
    text: 'It uses a virtual DOM to efficiently update the user interface.',
    upvotes: 3,
    downvotes: 0,
    opName: 'JaneDoe',
    date: '2024-12-07',
  },
];

// Mock data for replies
const replies: ReplyProps[] = [
  {
    id: '1',
    text: 'That makes sense! Thanks for the explanation.',
    upvotes: 2,
    downvotes: 0,
    opName: 'MikeSmith',
    date: '2024-12-08',
  },
  {
    id: '2',
    text: 'Could you explain more about how the virtual DOM works?',
    upvotes: 1,
    downvotes: 0,
    opName: 'ChrisBrown',
    date: '2024-12-08',
  },
];

const PostPage: React.FC = () => {
  // Retrieve the `postId` from the URL parameters
  const { postId } = useParams<{ postId: string }>();

  // Find the question by its `postId`
  const question = questions.find((q) => q.postId === postId);

  // Handle the case when the question is not found
  if (!question) {
    return <p>Question not found.</p>;
  }

  // Filter the answers and replies based on the `postId`
  const postAnswers = answers.filter((answer) => answer.postId === postId);
  const postReplies = replies.filter((reply) => reply.id === postId);

  // Pass the `QuestionProps` data to the `Question` component
  return (
    <div className="post-page">
      <Question
        postId={question.postId}
        title={question.title}
        questionText={question.questionText}
        tags={question.tags}
        upvotes={question.upvotes}
        downvotes={question.downvotes}
        opName={question.opName}
        date={question.date}
        communityId={question.communityId}
        communityName={question.communityName}
      />

      <div className="answers-section">
        {postAnswers.length > 0 ? (
          postAnswers.map((answer, index) => (
            <div key={index} className="answer-container">
              <Answer
                postId={answer.postId}
                text={answer.text}
                upvotes={answer.upvotes}
                downvotes={answer.downvotes}
                opName={answer.opName}
                date={answer.date}
              />
              <div className="replies-section">
                {postReplies.length > 0 &&
                  postReplies.map((reply, index) => (
                    <div key={index} className="reply-container">
                      <Reply
                        id={reply.id}
                        text={reply.text}
                        upvotes={reply.upvotes}
                        downvotes={reply.downvotes}
                        opName={reply.opName}
                        date={reply.date}
                      />
                    </div>
                  ))}
              </div>
            </div>
          ))
        ) : (
          <p>No answers yet.</p>
        )}
      </div>
    </div>
  );
};

export default PostPage;
