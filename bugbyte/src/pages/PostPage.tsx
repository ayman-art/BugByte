
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import Question from '../components/post/Question';
import Answer from '../components/post/Answer';
import Reply from '../components/post/Reply';
import '../styles/PostPage.css';
import { deleteAnswer, deleteQuestion, deleteReply, getAnswersFromQuestion, getQuestion, getRepliesFromAnswer, verifyAnswer } from '../API/PostAPI';
import { IQuestion, IAnswer, IReply } from '../types/index';


const PostPage: React.FC = () => {
  const { postId } = useParams<{ postId: string }>();
  const [question, setQuestion] = useState<IQuestion | null>(null);
  const [answers, setAnswers] = useState<IAnswer[]>([]);
  const [replies, setReplies] = useState<Map<string, IReply[]>>(new Map());
  const [hasNextAnswers, setHasNextAnswers] = useState(true);
  const [hasNextReplies, setHasNextReplies] = useState<Map<string, boolean>>(new Map());
  const [questionLoading, setQuestionLoading] = useState(true);
  const [verifiedAnswerId, setVerifiedAnswerId] = useState<string | null>(null);

  const pageSize = 3;
  const token = localStorage.getItem('authToken');
  const loggedInUsername = localStorage.getItem('name') || '';

  useEffect(() => {
    setVerifiedAnswerId(null);

    const fetchQuestion = async () => {

        let fetchedQuestion: [IQuestion, IAnswer | null] | null;
        try {
         fetchedQuestion = await getQuestion(postId!, localStorage.getItem('authToken') || '');
        } catch (error) {
          console.error(error);
          setQuestionLoading(false);
          return;
        }

        const [question, verifiedAnswer] = fetchedQuestion;
        setQuestion(question);
        const initialAnswers = [];
        if (verifiedAnswer) {
          initialAnswers.push(verifiedAnswer);
          setVerifiedAnswerId(verifiedAnswer.answerId);
        }

        const fetchedAnswers = await getAnswersFromQuestion(postId!, token!, answers.length, pageSize + 1);
        const hasNext = fetchedAnswers.length > pageSize;
        if (hasNext)
          fetchedAnswers.pop();
        const nonDuplicates = fetchedAnswers.filter(
          (answer) => answer.answerId !== verifiedAnswer?.answerId
        );
        setAnswers([...initialAnswers, ...nonDuplicates]);
        setHasNextAnswers(hasNext);
      
        const nextReplies = new Map<string, boolean>();
        for (const answer of [...initialAnswers, ...nonDuplicates]) {
          const fetchedReplies = await getRepliesFromAnswer(answer.answerId, token!, 0, pageSize + 1);
          const hasMoreReplies = fetchedReplies.length > pageSize;
          nextReplies.set(answer.answerId, hasMoreReplies);
          setReplies((prev) => {
            const newReplies = new Map(prev);
            newReplies.set(answer.answerId, fetchedReplies.slice(0, pageSize)); // Store replies for this answer
            return newReplies;
          });
        }
        setHasNextReplies(nextReplies);

        setQuestionLoading(false);
      };

      fetchQuestion();
      
      
  }, [postId]);

  const fetchMoreAnswers = async () => {
    const newAnswers = await getAnswersFromQuestion(postId!, token!, answers.length, pageSize + 1);
    const addedAnswers = newAnswers.slice(0, pageSize);
    const hasNext = newAnswers.length > pageSize;


    const nextReplies = new Map<string, boolean>();
    for (const answer of addedAnswers) {
      const fetchedReplies = await getRepliesFromAnswer(answer.answerId, token!, 0, pageSize + 1);
      const hasMoreReplies = fetchedReplies.length > pageSize;
      nextReplies.set(answer.answerId, hasMoreReplies);

      setReplies((prev) => {
        const newReplies = new Map(prev);
        newReplies.set(answer.answerId, fetchedReplies.slice(0, pageSize));
        return newReplies;
      });
    }



    setAnswers((prev) => [...prev, ...addedAnswers]);
    setHasNextAnswers(hasNext);
  };


  const fetchMoreReplies = async (answerId: string) => {
    const currentReplies = replies.get(answerId) || [];
    const newReplies = await getRepliesFromAnswer(answerId, token!, currentReplies.length, pageSize + 1);
    const addedReplies = newReplies.slice(0, pageSize);
    const hasNext = newReplies.length > pageSize;
  
    setReplies((prev) => {
      const newRepliesMap = new Map(prev);
      newRepliesMap.set(answerId, [...currentReplies, ...addedReplies]);
      return newRepliesMap;
    });
  
    setHasNextReplies((prev) => {
      const newHasNextReplies = new Map(prev);
      newHasNextReplies.set(answerId, hasNext);
      return newHasNextReplies;
    });
  };

  const onAnswerQuestion = (answer: IAnswer): void => {
    setAnswers((prev) => [answer, ...prev]);
  }

  const onReplyonAnswer = (reply: IReply): void => {
    const answerId = reply.answerId;
    setReplies((prev) => {
      const newReplies = new Map(prev);
      const currentReplies = newReplies.get(answerId) || [];
      newReplies.set(answerId, [reply, ...currentReplies]);
      return newReplies;
    });
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
    await deleteReply(replyId, token!);

    setReplies((prev) => {
      const newReplies = new Map(prev);
      const currentReplies = newReplies.get(answerId) || [];
      newReplies.set(answerId, currentReplies.filter((reply) => reply.replyId !== replyId));
      return newReplies;
    });
  }

  const onVerify = async (answerId: string) => {
    await verifyAnswer(answerId, token!);
    setVerifiedAnswerId(answerId);
  };

  return (
    <div className="post-page">
      <Question {...question} onDelete={onDelteQuestion} onReply = {onAnswerQuestion} />
      <div className="answers-section">
        {answers.map((answer) => (
          <div key={answer.answerId} className="answer-container">
            <Answer {...answer} enabledVerify = {verifiedAnswerId === null} isVerified = {verifiedAnswerId == answer.answerId}
            canVerify = {question.opName === loggedInUsername}
             onDelete={onDeleteAnswer} onVerify={onVerify} onReplyOnAnswer={onReplyonAnswer} />
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


export default PostPage;
