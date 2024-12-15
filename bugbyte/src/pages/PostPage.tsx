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
  id: string;
  postId: string;
  text: string;
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
}

interface ReplyProps {
  id: string;
  answerId: string;  // Add answerId to map replies to answers
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
    questionText: `
    # h1 Heading 8-)
    ## h2 Heading
    ### h3 Heading
    #### h4 Heading
    ##### h5 Heading
    ###### h6 Heading


    ## Horizontal Rules
    ![Minion](https://octodex.github.com/images/minion.png)
    `,
    tags: ['React', 'JavaScript', 'Frontend'],
    upvotes: 10,
    downvotes: 2,
    opName: 'Ayman Algamal',
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
    id: '1',
    postId: '1',
    text: 'React is a JavaScript library for building user interfaces, primarily for single-page applications.',
    upvotes: 5,
    downvotes: 1,
    opName: 'JohnDoe',
    date: '2024-12-07',
  },
  {
    id: '2',
    postId: '1',
    text: 'It uses a virtual DOM to efficiently update the user interface.',
    upvotes: 3,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-07',
  },
];

// Mock data for replies
const replies: ReplyProps[] = [
  {
    id: '1',
    answerId: '1',  // Map to answerId
    text: '# That makes sense! Thanks for the explanation.',
    upvotes: 2,
    downvotes: 0,
    opName: 'MikeSmith',
    date: '2024-12-08',
  },
  {
    id: '2',
    answerId: '2',  // Map to answerId
    text: 'Could you explain more about how the virtual DOM works?',
    upvotes: 1,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-08',
  },{
    id: '3',
    answerId: '2',  // Map to answerId
    text: '# ANOTHER REPLY?',
    upvotes: 1,
    downvotes: 0,
    opName: 'Ayman Algamal',
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

  // Filter the answers based on the `postId`
  const postAnswers = answers.filter((answer) => answer.postId === postId);

  // Filter the replies based on the `answerId` (this was the issue)
  const postReplies = (answerId: string) =>
    replies.filter((reply) => reply.answerId === answerId);

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
                id={answer.id}
                postId={answer.postId}
                text={answer.text}
                upvotes={answer.upvotes}
                downvotes={answer.downvotes}
                opName={answer.opName}
                date={answer.date}
              />
              <div className="replies-section">
                {/* Use postReplies for each answer, filtered by answerId */}
                {postReplies(answer.id).length > 0 &&
                  postReplies(answer.id).map((reply, index) => (
                    <div key={index} className="reply-container">
                      <Reply
                        id={reply.id}
                        answerId={reply.answerId} // Pass answerId to Reply component
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