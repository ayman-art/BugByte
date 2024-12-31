interface ValidationError {
    errors: string[];
    isValid: boolean;
  }
  
  const validatePostDetails = (postDetails: PostDetails): ValidationError => {
    const errors: string[] = [];
  
    if (!postDetails.title?.trim()) {
      errors.push('Title cannot be empty.');
    }
    if (!postDetails.content?.trim()) {
      errors.push('Body cannot be empty.');
    }
    if (!postDetails.communityId) {
      errors.push('Community ID is required.');
    }
  
    return {
      errors,
      isValid: errors.length === 0
    };
  };
export default validatePostDetails;