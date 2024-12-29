
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Question from '../components/post/Question';
import Answer from '../components/post/Answer';
import Reply from '../components/post/Reply';
import '../styles/PostPage.css';
import { deleteAnswer, deleteQuestion, deleteReply, getAnswersFromQuestion, getQuestion, getRepliesFromAnswer, verifyAnswer } from '../API/PostAPI';
import { IQuestion, IAnswer, IReply } from '../types/index';
interface QuestionProps  extends IQuestion {
  onDelete: (questionId: string) => void;
}





const PostPage: React.FC = () => {
  const { postId } = useParams<{ postId: string }>();

  const [question, setQuestion] = useState<IQuestion | null>(null);
  const [answers, setAnswers] = useState<IAnswer[]>([]);
  const [replies, setReplies] = useState<Map<string, IReply[]>>(new Map());
  const [hasNextAnswers, setHasNextAnswers] = useState(true);
  const [hasNextReplies, setHasNextReplies] = useState<Map<string, boolean>>(new Map());
  const [questionLoading, setQuestionLoading] = useState(true);

  const pageSize = 3;
  const token = localStorage.getItem('authToken');

  useEffect(() => {
    let fetchedQuestion: [IQuestion, IAnswer | null] | null;
      const fetchQuestion = async () => {
        try {
         fetchedQuestion = await getQuestion(postId!, localStorage.getItem('authToken') || '');
        } catch (error) {
          console.error(error);
          setQuestionLoading(false);
          return;
        }
        setQuestion(fetchedQuestion[0]);
        
        const initialAnswers = [];
        if (fetchedQuestion[1]) {
          initialAnswers.push({ ...fetchedQuestion[1], isVerified: true, enabledVerify: false });
        }
      
        const fetchedAnswers = await getAnswersFromQuestion(postId!, token!, answers.length, pageSize + 1);
        console.log(fetchedAnswers.length, pageSize, fetchedQuestion[1]);
      
        const hasNext = fetchedAnswers.length > pageSize;
        if (hasNext) {
          fetchedAnswers.pop();
        }
      
        // Filter out the verified answer from fetched answers to prevent duplication
        const nonDuplicates = fetchedAnswers.filter(
          (answer) => !fetchedQuestion[1] || answer.answerId !== fetchedQuestion[1].answerId
        );
      
        // Mark other answers as not verified
        const processedAnswers = nonDuplicates.map((answer) => ({
          ...answer,
          isVerified: false,
          enabledVerify: !fetchedQuestion[1],
        }));
      
        // Combine initial answers with fetched answers
        setAnswers([...initialAnswers, ...processedAnswers]);
        setHasNextAnswers(hasNext);
      
        // Fetch replies for each answer
        const nextReplies = new Map<string, boolean>();
      
        // Loop over each answer to fetch its replies
        for (const answer of processedAnswers) {
          const fetchedReplies = await getRepliesFromAnswer(answer.answerId, token!, 0, pageSize + 1);
      
          // Check if there are more replies beyond the current page
          const hasMoreReplies = fetchedReplies.length > pageSize;
          nextReplies.set(answer.answerId, hasMoreReplies);
      
          setReplies((prev) => {
            const newReplies = new Map(prev);
            newReplies.set(answer.answerId, fetchedReplies.slice(0, pageSize)); // Store replies for this answer
            return newReplies;
          });
        }
      
        console.log("HW",replies)
        setHasNextReplies(nextReplies);
      };
      
      fetchQuestion();
      
      
  }, [postId]);

  const fetchMoreAnswers = async () => {
    const newAnswers = await getAnswersFromQuestion(postId!, token!, answers.length, pageSize + 1);
    let addedAnswers = newAnswers.slice(0, pageSize);
    const hasNext = newAnswers.length > pageSize;
    

    const hasVerifiedAnswer = answers.some((answer) => answer.isVerified);
    if (hasVerifiedAnswer) {
      const verifiedAnswer = answers.find((answer) => answer.isVerified);
      // Filter out the verified answer from fetched answers to prevent duplication
       addedAnswers = addedAnswers.filter(
        (answer) => answer.answerId !== verifiedAnswer?.answerId
      );
    }
    const enableVerify = !hasVerifiedAnswer;

    addedAnswers.forEach((answer) => ({
      ...answer,
      isVerified: false,
      enabledVerify: enableVerify
    }));

    const nextReplies = new Map<string, boolean>();
      
    // Loop over each answer to fetch its replies
    for (const answer of addedAnswers) {
      const fetchedReplies = await getRepliesFromAnswer(answer.answerId, token!, 0, pageSize + 1);
  
      // Check if there are more replies beyond the current page
      const hasMoreReplies = fetchedReplies.length > pageSize;
      nextReplies.set(answer.answerId, hasMoreReplies);
  
      setReplies((prev) => {
        const newReplies = new Map(prev);
        newReplies.set(answer.answerId, fetchedReplies.slice(0, pageSize)); // Store replies for this answer
        return newReplies;
      });
    }


  
    setAnswers((prev) => [...prev, ...addedAnswers]);
    setHasNextAnswers(hasNext);
  };
  

  const fetchMoreReplies = async (answerId: string) => {
    // Get the current replies for the specific answerId
    const currentReplies = replies.get(answerId) || [];
  
    // Fetch the next set of replies for the given answerId
    const newReplies = await getRepliesFromAnswer(answerId, token!, currentReplies.length, pageSize + 1);
  
    // Add only up to pageSize replies to avoid excess
    const addedReplies = newReplies.slice(0, pageSize);
  
    // Check if there are more replies beyond the current page
    const hasNext = newReplies.length > pageSize;
  
    // Update the replies state to include the new replies
    setReplies((prev) => {
      const newRepliesMap = new Map(prev);
      newRepliesMap.set(answerId, [...currentReplies, ...addedReplies]);
      return newRepliesMap;
    });
  
    // Update the hasNextReplies state to keep track of whether there are more replies
    setHasNextReplies((prev) => {
      const newHasNextReplies = new Map(prev);
      newHasNextReplies.set(answerId, hasNext);
      return newHasNextReplies;
    });
  };

  const onAnswerQuestion = (answer: IAnswer): void => {
    setAnswers((prev) => [...prev, answer]);
  }
  
  const onDelteQuestion = async (questionId: string) => {
    await deleteQuestion(questionId, token!);
    setQuestion(null)
  }

  const onDeleteAnswer = async (answerId: string) => {
    await deleteAnswer(answerId, token!);
    setAnswers((prev) => prev.filter((answer) => answer.answerId !== answerId));

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
    return <>
    {questionLoading && <div>Loading...</div>}
    {!questionLoading && <div>Question not found</div>}
    </>;
  }

  const onDeleteReply = async (replyId: string, answerId: string) => {
    // Delete the reply from the API
    await deleteReply(replyId, token!);

    // Update the replies state to remove the deleted reply
    setReplies((prev) => {
      const newReplies = new Map(prev);
      console.log("NEW",newReplies);
      console.log("answerId",answerId)
      const currentReplies = newReplies.get(answerId) || [];
      console.log("D",currentReplies)
      newReplies.set(answerId, currentReplies.filter((reply) => reply.replyId !== replyId));
      return newReplies;
    }
    );
  }

  const onVerify = async (answerId: string) => {

    await verifyAnswer(answerId, token!);
    setAnswers((prev) =>
      prev.map((answer) => {
        if (answer.answerId === answerId) {
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
      <Question {...question} onDelete={onDelteQuestion} onReply = {onAnswerQuestion} />
      <div className="answers-section">
        {answers.map((answer) => (
          <div key={answer.answerId} className="answer-container">
            <Answer {...answer} onDelete={onDeleteAnswer} onVerify={onVerify} />
            <div className="replies-section">
              {(replies.get(answer.answerId) || []).map((reply) => (
                <div key={reply.replyId} className="reply-container">
                  <Reply {...reply} onDelete={() => onDeleteReply(reply.replyId, answer.answerId)} />
                </div>
              ))}
              {hasNextReplies.get(answer.answerId) && (
                <button className="show-more-replies" onClick={() => fetchMoreReplies(answer.answerId)}>
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
