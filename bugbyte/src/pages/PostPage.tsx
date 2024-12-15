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
  verified?: boolean;
  enabledVerify?: boolean;
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

  const [question, setQuestion] = useState<QuestionProps | null>(null);
  const [answers, setAnswers] = useState<AnswerProps[]>([]);
  const [replies, setReplies] = useState<Map<string, ReplyProps[]>>(new Map());
  const [hasNextAnswers, setHasNextAnswers] = useState(true);
  const [hasNextReplies, setHasNextReplies] = useState<Map<string, boolean>>(new Map());
  const pageSize = 2;

  useEffect(() => {
    const fetchQuestion = async () => {
      const fetchedQuestion = questionsEx.find((q) => q.postId === postId);
      const fetchedAnswers = answersEx.filter((answer) => answer.postId === postId).slice(0, pageSize);
      const hasNext = answersEx.filter((answer) => answer.postId === postId).length > pageSize;

      setQuestion(fetchedQuestion || null);
      setAnswers(fetchedAnswers);
      setHasNextAnswers(hasNext);

      // Initialize `hasNextReplies` for all fetched answers
      const nextReplies = new Map<string, boolean>();
      fetchedAnswers.forEach((answer) => {
        const hasMoreReplies = (repliesEx[answer.id]?.length || 0) > pageSize;
        nextReplies.set(answer.id, hasMoreReplies);
        setReplies((prev) => {
          const newReplies = new Map(prev);
          newReplies.set(answer.id, (repliesEx[answer.id]?.slice(0, pageSize)) || []);
          return newReplies;
        });
      });
      setHasNextReplies(nextReplies);
    };

    fetchQuestion();
  }, [postId]);

  const fetchMoreAnswers = () => {
    const newAnswers = answersEx.slice(answers.length, answers.length + pageSize + 1);
    const addedAnswers = newAnswers.slice(0, pageSize);
    const hasNext = newAnswers.length > pageSize;
    

    const hasVerifiedAnswer = answers.some((answer) => answer.verified);
    const enableVerify = !hasVerifiedAnswer;

    addedAnswers.forEach((answer) => {
      answer.enabledVerify = enableVerify;
    });

    // Initialize replies and hasNextReplies for the newly added answers
    const nextReplies = new Map<string, boolean>();
    addedAnswers.forEach((answer) => {
      const hasMoreReplies = (repliesEx[answer.id]?.length || 0) > pageSize;
      nextReplies.set(answer.id, hasMoreReplies);
  
      setReplies((prev) => {
        const newReplies = new Map(prev);
        newReplies.set(answer.id, (repliesEx[answer.id]?.slice(0, pageSize)) || []);
        return newReplies;
      });
    });
  
    setHasNextReplies((prev) => {
      const newHasNextReplies = new Map(prev);
      nextReplies.forEach((value, key) => {
        newHasNextReplies.set(key, value);
      });
      return newHasNextReplies;
    });
  
    setAnswers((prev) => [...prev, ...addedAnswers]);
    setHasNextAnswers(hasNext);
  };
  

  const fetchMoreReplies = (answerId: string) => {
    const currentReplies = replies.get(answerId) || [];
    const newReplies = repliesEx[answerId]?.slice(currentReplies.length, currentReplies.length + pageSize + 1) || [];
    const addedReplies = newReplies.slice(0, pageSize);
    const hasNext = newReplies.length > pageSize;

    setReplies((prev) => {
      const newRepliesMap = new Map(prev);
      newRepliesMap.set(answerId, [...(prev.get(answerId) || []), ...addedReplies]);
      return newRepliesMap;
    });

    setHasNextReplies((prev) => {
      const newHasNextReplies = new Map(prev);
      newHasNextReplies.set(answerId, hasNext);
      return newHasNextReplies;
    });
  };

  const onDeleteAnswer = (answerId: string) => {
    setAnswers((prev) => prev.filter((answer) => answer.id !== answerId));

    setReplies((prev) => {
      const newReplies = new Map(prev);
      newReplies.delete(answerId);
      return newReplies;
    });

    setHasNextReplies((prev) => {
      const newHasNextReplies = new Map(prev);
      newHasNextReplies.delete(answerId);
      return newHasNextReplies;
    });
  };

  if (!question) {
    return <p>Question not found.</p>;
  }

  const onVerify = (answerId: string) => {
    // Update the verified answer in the state
    setAnswers((prev) =>
      prev.map((answer) => {
        if (answer.id === answerId) {
          return { ...answer, verified: true, enabledVerify: false };
        } else {
          return { ...answer, verified: false, enabledVerify: false };
        }
        return answer;
      })
    );
  };

  return (
    <div className="post-page">
      <Question {...question} />
      <div className="answers-section">
        {answers.map((answer) => (
          <div key={answer.id} className="answer-container">
            <Answer {...answer} onDelete={onDeleteAnswer} onVerify={onVerify} />
            <div className="replies-section">
              {(replies.get(answer.id) || []).map((reply) => (
                <div key={reply.id} className="reply-container">
                  <Reply {...reply} />
                </div>
              ))}
              {hasNextReplies.get(answer.id) && (
                <button className="show-more-replies" onClick={() => fetchMoreReplies(answer.id)}>
                  Show More Replies
                </button>
              )}
            </div>
          </div>
        ))}
        {hasNextAnswers && (
          <button className="show-more-answers" onClick={fetchMoreAnswers}>
            Show More Answers
          </button>
        )}
      </div>
    </div>
  );
};

// Mock Data (unchanged)
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
  {
    id: '3',
    postId: '1',
    text: 'It usesss a virtual DOM to efficiently update the UI.',
    upvotes: 3,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-07',
  },
  {
    id: '4',
    postId: '1',
    text: 'It usesssssss a virtual DOM to efficiently update the UI.',
    upvotes: 3,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-07',
  },
  {
    id: '5',
    postId: '2',
    text: 'It usesss a virtual DOM to efficiently update the UI to ans2.',
    upvotes: 3,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-07',
  },
  {
    id: '6',
    postId: '2',
    text: 'It usesssssss a virtual DOM to efficiently update the UI to ans2.',
    upvotes: 3,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-07',
  },
  {
    id: '7',
    postId: '2',
    text: 'It usesss a virtual DOM to efficiently update the UI to ans4.',
    upvotes: 3,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-07',
  },
  {
    id: '8',
    postId: '2',
    text: 'It usesssssss a virtual DOM to efficiently update the UI to ans4.',
    upvotes: 3,
    downvotes: 0,
    opName: 'Ayman Algamal',
    date: '2024-12-07',
  },
];

const repliesEx: { [key: string]: ReplyProps[] } =
  {
    "1": [
      {
        id: '1',
        answerId: '1',
        text: 'React is a JavaScript library for building user interfaces.',
        upvotes: 5,
        downvotes: 1,
        opName: 'JohnDoe',
        date: '2024-12-07',
      },
      {
        id: '2',
        answerId: '1',
        text: 'It uses a virtual DOM to efficiently update the UI.',
        upvotes: 3,
        downvotes: 0,
        opName: 'Ayman Algamal',
        date: '2024-12-07',
      },
     
    ],
    "2": [
      {
        id: '5',
        answerId: '2',
        text: 'It usesss a virtual DOM to efficiently update the UI to ans2.',
        upvotes: 3,
        downvotes: 0,
        opName: 'Ayman Algamal',
        date: '2024-12-07',
      },
      {
        id: '6',
        answerId: '2',
        text: 'It usesssssss a virtual DOM to efficiently update the UI to ans2.',
        upvotes: 3,
        downvotes: 0,
        opName: 'Ayman Algamal',
        date: '2024-12-07',
      },
      {
        id: '8',
        answerId: '2',
        text: 'It usesssssss a virtual DOM to efficiently update the UI to ans4.',
        upvotes: 3,
        downvotes: 0,
        opName: 'Ayman Algamal',
        date: '2024-12-07',
      },
    ],
    "4": [
      {
        id: '7',
        answerId: '4',
        text: 'It usesss a virtual DOM to efficiently update the UI to ans4.',
        upvotes: 3,
        downvotes: 0,
        opName: 'Ayman Algamal',
        date: '2024-12-07',
      },
      {
        id: '8',
        answerId: '4',
        text: 'It usesssssss a virtual DOM to efficiently update the UI to ans4.',
        upvotes: 3,
        downvotes: 0,
        opName: 'Ayman Algamal',
        date: '2024-12-07',
      },
      {
        id: '8',
        answerId: '4',
        text: 'It usesssssss a virtual DOM to efficiently update the UI to ans4.',
        upvotes: 3,
        downvotes: 0,
        opName: 'Ayman Algamal',
        date: '2024-12-07',
      },
    ]
  };
export default PostPage;
