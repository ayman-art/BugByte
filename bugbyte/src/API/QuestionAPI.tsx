export const upvotePost = async (postId: string): Promise<boolean> => {
    try {
      const response = await fetch(`http://posts/upvote/${postId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ voteType: postId }),
      });
      return response.ok;
    } catch (error) {
      console.error('Error upvoting post:', error);
      return false;
    }
  };
  
  export const downvotePost = async (postId: string): Promise<boolean> => {
    try {
      const response = await fetch(`http://posts/downvote/${postId}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ voteType: postId }),
      });
      return response.ok;
    } catch (error) {
      console.error('Error downvoting post:', error);
      return false;
    }
  };
  