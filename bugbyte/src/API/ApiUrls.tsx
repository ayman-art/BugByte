const API_BASE_URL = "http://localhost:8080";

export const API_URLS = {
  LOGIN: `${API_BASE_URL}/users/login`,
  SIGNUP: `${API_BASE_URL}/users/signup`,
  REGISTER: ``,
  LOGIN_BY_GOOGLE:`${API_BASE_URL}/api/auth/google`,
  FORGOT_PASSWORD:`${API_BASE_URL}/users/reset-password`,
  VALIDATE_CODE: `${API_BASE_URL}/users/validate-email-code`,
  CHANGE_PASSWORD: `${API_BASE_URL}/users/change-password`,
  AUTHENTICATE_TOKEN: `${API_BASE_URL}/users/authorize`,
  PROFILE: `${API_BASE_URL}/users/profile`,
  BIO_UPDATE: `${API_BASE_URL}/users/update-bio`,
  FOLLOW: `${API_BASE_URL}/users/follow`,
  UNFOLLOW: `${API_BASE_URL}/users/unfollow`,
  GET_FOLLOWERS: `${API_BASE_URL}/users/followers`,
  GET_FOLLOWINGS: `${API_BASE_URL}/users/following`,
  QUESTION: `${API_BASE_URL}/posts/questions`,
  ANSWER: `${API_BASE_URL}/posts/answers`,
  REPLY: `${API_BASE_URL}/posts/replies`,
  ADMIN: `${API_BASE_URL}/users/make-admin`,
  UPVOTE:`${API_BASE_URL}/posts/upvote`,
  DOWNVOTE:`${API_BASE_URL}/posts/downvote`,
  UPLOAD_IMAGE: `${API_BASE_URL}/images/upload`,
  UPDATE_PROFILE_PICTURE: `${API_BASE_URL}/users/update-picture`,
  LOAD_COMMUNITY: `${API_BASE_URL}/communities/getCommunity`,
  JOINED_COMMUNITITES: `${API_BASE_URL}/communities/joinedCommunities`,
  JOIN_COMMUNITY: `${API_BASE_URL}/communities/joinCommunity`,
  SEARCH_COMMUNITIES:`${API_BASE_URL}/search/community`,
  SEARCH_QUESTIONS:`${API_BASE_URL}/search/question`,
  SEARCH_FILTERED_QUESTIONS: `${API_BASE_URL}/search/filterQuestion`,
  SEARCH_FILTERED_COMMUNITIES: `${API_BASE_URL}/search/filterCommunity`,
  CREATE_COMMUNITY: `${API_BASE_URL}/communities/createCommunity`,
  GET_FEED: `${API_BASE_URL}/recommendation/feed`,
  
};
