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

  export const removeUpvotePost = async (postId: string, token:string): Promise<boolean> => {
    try {
      const response = await fetch(`${API_URLS.REMOVE_UPVOTE}?postId=${postId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      return response.ok;
    } catch (error) {

      console.error('Error updating upvoting post:', error);
      return false;
    }
  };
  
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
  
  export const removeDownvotePost = async (postId: string, token:string): Promise<boolean> => {
    try {
      const response = await fetch(`${API_URLS.REMOVE_DOWNVOTE}?postId=${postId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      return response.ok;
    } catch (error) {

      console.error('Error updating upvoting post:', error);
      return false;
    }
  };