import { API_URLS } from './ApiUrls';

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
    return data;
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
    return data;
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
        return data;
    } catch (error) {
        console.error('Error posting reply:', error);
        throw error;
    }
};

export const getQuestion = async (questionId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.QUESTION}?questionId=${questionId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to get question');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error getting question:', error);
        throw error;
    }
}
    

export const getAnswer = async (answerId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.ANSWER}?answerId=${answerId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        });

        if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to get answer');
        }

        const data = await response.json();
        return data;
    } catch (error) {
        console.error('Error getting answer:', error);
        throw error;
    }
}

export const getReply = async (replyId: string, token: string): Promise<any> => {
    try {
        const response = await fetch(`${API_URLS.REPLY}?replyId=${replyId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
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
        const response = await fetch(`${API_URLS.VERIFY_ANSWER}?answerId=${answerId}`, {
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






