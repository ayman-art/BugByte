import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Question from '../components/post/Question';
import Answer from '../components/post/Answer';
import Reply from '../components/post/Reply';
import '../styles/PostPage.css';

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
  answerId: string;
  text: string;
  upvotes: number;
  downvotes: number;
  opName: string;
  date: string;
}

const PostPage: React.FC = () => {
  const { postId } = useParams<{ postId: string }>();

  const [question, setQuestion] = useState<QuestionProps | null>(questionsEx[0]);
  const [answers, setAnswers] = useState<AnswerProps[]>(answersEx);
  const [replies, setReplies] = useState<ReplyProps[]>(repliesEx);

  // Fetch question, answers, and replies based on postId
  useEffect(() => {
    // mocking
    const fetchedQuestion = questionsEx.find((q) => q.postId === postId);
    const fetchedAnswers = answersEx.filter((answer) => answer.postId === postId);
    const fetchedReplies = repliesEx.filter((reply) => reply.answerId === '1');

    if (fetchedQuestion) {
      setQuestion(fetchedQuestion);
      setAnswers(fetchedAnswers);
      setReplies(fetchedReplies);
    }
  }, [postId]);

  const onDeleteAnswer = (answerId: string) => {
    const updatedAnswers = answers.filter((answer) => answer.id !== answerId);
    setAnswers(updatedAnswers);
  };

  if (!question) {
    return <p>Question not found.</p>;
  }

  const repliesMap = replies.reduce((map, reply) => {
    if (!map[reply.answerId]) {
      map[reply.answerId] = [];
    }
    map[reply.answerId].push(reply);
    return map;
  }, {} as { [key: string]: ReplyProps[] });

  return (
    <div className="post-page">
      <Question {...question} />
      <div className="answers-section">
        {answers.length > 0 ? (
          answers.map((answer) => (
            <div key={answer.id} className="answer-container">
              <Answer
                {...answer}
                onDelete={onDeleteAnswer}
              />
              <div className="replies-section">
                {repliesMap[answer.id]?.map((reply) => (
                  <div key={reply.id} className="reply-container">
                    <Reply {...reply} />
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

const questionsEx: QuestionProps[] = [
  {
    postId: '1',
    title: 'TITLEEEE',
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
    title: 'TITLEEEE',
    questionText: 'Here We should display the MD file with all of its content.',
    tags: ['State Management', 'React', 'Hooks'],
    upvotes: 15,
    downvotes: 3,
    opName: 'Jane Smith',
    date: '2024-12-06',
    communityId: '456',
    communityName: 'React Developers',
  },
];

const answersEx: AnswerProps[] = [
  {
    id: '1',
    postId: '1',
    text: 'React is a JavaScript library for building user interfaces.',
    upvotes: 5,
    downvotes: 1,
    opName: 'JohnDoe',
    date: '2024-12-07',
  },
  {
    id: '2',
    postId: '1',
    text: 'It uses a virtual DOM to efficiently update the UI.',
    upvotes: 3,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-07',
  },
];

const repliesEx: ReplyProps[] = [
  {
    id: '1',
    answerId: '1',
    text: '# That makes sense! Thanks for the explanation.',
    upvotes: 2,
    downvotes: 0,
    opName: 'MikeSmith',
    date: '2024-12-08',
  },
  {
    id: '2',
    answerId: '1',
    text: 'Could you explain more about how the virtual DOM works?',
    upvotes: 1,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-08',
  },
  {
    id: '3',
    answerId: '1',
    text: '# ANOTHER REPLY?',
    upvotes: 1,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-08',
  },
];

export default PostPage;
