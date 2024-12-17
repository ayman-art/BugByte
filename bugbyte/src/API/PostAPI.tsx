import { API_URLS } from './ApiUrls';
import { IQuestion, IAnswer } from '../types/index'

export interface QuestionResponse {
    answerDownVotes?: number;
    questionId: string;
    validatedAnswerId: string;
    answerOp?: string;
    upVotes: number;
    mdContent: string;
    isDownVoted: boolean;
    title: string;
    isUpVoted: boolean;
    answerMdContent?: string;
    downVotes: number;
    tags: string[] | null;
    answerUpVotes?: number;
    opName: string;
    postedOn: string;
    answerPostedOn?: string;
    communityName: string;
    communityId: string;
  }



  export interface ReplyData {
    answerId: string;
    opName: string;
    replyId: string;
    postedOn: string; // ISO date format as string
    mdContent: string;
  }

export const postQuestion = async (
  mdContent: string, 
  title: string, 
  tags: string[], 
  communityId: string, 
  token: string
): Promise<any> => {
  try {
    const response = await fetch(`${API_URLS.QUESTION}?communityId=${communityId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ mdContent, title, tags }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to post question');
    }

    const data = await response.json();
    return data.questionId;
  } catch (error) {
    console.error('Error posting question:', error);
    throw error;
  }
};

export const postAnswer = async (
  mdContent: string, 
  questionId: string, 
  token: string
): Promise<any> => {
  try {
    const response = await fetch(`${API_URLS.ANSWER}?questionId=${questionId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ mdContent }),
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Failed to post answer');
    }

    const data = await response.json();
    console.log(data);
    return data.answerId;
  } catch (error) {
    console.error('Error posting answer:', error);
    throw error;
  }
};

export const postReply = async (
    mdContent: string, 
    answerId: string, 
    token: string
    ): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.REPLY}?answerId=${answerId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ mdContent }),
        });
    
        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to post reply');
        }
    
        const data = await response.json();
        return data.replyId
    } catch (error) {
        console.error('Error posting reply:', error);
        throw error;
    }
};

export const getQuestion = async (
    questionId: string,
    token: string
  ): Promise<[IQuestion, IAnswer | null]> => {
    try {
      console.log(token);
      const response = await fetch(`${API_URLS.QUESTION}?questionId=${questionId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
      });
  
      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to get question');
      }
  
      const data = await response.json();
      const tags = data.tags || []; // Handle null tags
  
      // Construct the question object
      const question: IQuestion = {
        ...data,
        tags,
      };
  
      // Parse the validated answer if it exists
      let validatedAnswer: IAnswer | null = null;
      if (data.validatedAnswerId !== 0 && data.answerMdContent) {
        validatedAnswer = {
          answerId: data.validatedAnswerId,
          questionId: data.questionId,
          mdContent: data.answerMdContent,
          upVotes: data.answerUpVotes || 0,
          downVotes: data.answerDownVotes || 0,
          postedOn: data.answerPostedOn || '',
          opName: data.answerOp || '',
          isDownVoted: false,
          isUpVoted: false,
            isVerified: false,
            enabledVerify: true,
        };
      }
  
      return [question, validatedAnswer];
    } catch (error) {
      console.error('Error getting question:', error);
      throw error;
    }
  };
  
export const getAnswer = async (answerId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.ANSWER}/${answerId}`, { // Path variable
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to get answer');
        }

        const data = await response.json();
        return data.answerId;
    } catch (error) {
        console.error('Error getting answer:', error);
        throw error;
    }
}

export const getReply = async (replyId: string, token: string): Promise<ReplyData> => {
    try {
        const response = await fetch(`${API_URLS.REPLY}/${replyId}`, { // Path variable
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to get reply');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error getting reply:', error);
        throw error;
    }
}

export const editQuestion = async (questionId: string, title: string, mdContent: string, tags: string[], token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.QUESTION}?questionId=${questionId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ title, mdContent, tags }),
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to edit question');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error editing question:', error);
        throw error;
    }
}

export const editAnswer = async (answerId: string, mdContent: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.ANSWER}?answerId=${answerId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ mdContent }),
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to edit answer');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error editing answer:', error);
        throw error;
    }
}

export const editReply = async (replyId: string, mdContent: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.REPLY}?replyId=${replyId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ mdContent }),
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to edit reply');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error editing reply:', error);
        throw error;
    }
}

export const deleteQuestion = async (questionId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.QUESTION}?questionId=${questionId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to delete question');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error deleting question:', error);
        throw error;
    }
}

export const deleteAnswer = async (answerId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.ANSWER}?answerId=${answerId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to delete answer');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error deleting answer:', error);
        throw error;
    }
}

export const deleteReply = async (replyId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.REPLY}?replyId=${replyId}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to delete reply');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error deleting reply:', error);
        throw error;
    }
}




export const verifyAnswer = async (answerId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.ANSWER}/verify?answerId=${answerId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to verify answer');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error verifying answer:', error);
        throw error;
    }
}

export const upvoteQuestion = async (questionId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.POST}/upvoteQuestion?postId=${questionId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to upvote question');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error upvoting question:', error);
        throw error;
    }
}

export const downvoteQuestion = async (questionId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.POST}/downvoteQuestion?postId=${questionId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to downvote question');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error downvoting question:', error);
        throw error;
    }
}

export const removeUpvoteQuestion = async (questionId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.POST}/removeUpvoteQuestion?postId=${questionId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to remove upvote question');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error removing upvote question:', error);
        throw error;
    }
}

export const removeDownvoteQuestion = async (questionId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.POST}/removeDownvoteQuestion?postId=${questionId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to remove downvote question');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error removing downvote question:', error);
        throw error;
    }
}

export const upvoteAnswer = async (answerId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.POST}/upvoteAnswer?postId=${answerId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to upvote answer');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error upvoting answer:', error);
        throw error;
    }
}

export const downvoteAnswer = async (answerId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.POST}/downvoteAnswer?postId=${answerId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to downvote answer');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error downvoting answer:', error);
        throw error;
    }
}

export const removeUpvoteAnswer = async (answerId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.POST}/removeUpvoteAnswer?postId=${answerId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to remove upvote answer');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error removing upvote answer:', error);
        throw error;
    }
}

export const removeDownvoteAnswer = async (answerId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.POST}/removeDownvoteAnswer?postId=${answerId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to remove downvote answer');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error removing downvote answer:', error);
        throw error;
    }
}




