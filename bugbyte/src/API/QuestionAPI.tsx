import {API_URLS} from './ApiUrls';

export const upvotePost = async (postId: string, token:string): Promise<boolean> => {
    try {
      const response = await fetch(`${API_URLS.UPVOTE}?postId=${postId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      return response.ok;
    } catch (error) {

      console.error('Error upvoting post:', error);
      return false;
    }
  };

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
  
  export const downvotePost = async (postId: string, token: string): Promise<boolean> => {
    try {
      const response = await fetch(`${API_URLS.DONWVOTE}?postId=${postId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ voteType: postId }),
      });
      return response.ok;
    } catch (error) {
      console.error('Error downvoting post:', error);
      return false;
    }
  };
  
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