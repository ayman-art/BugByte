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
  ADMIN: `${API_BASE_URL}/users/make-admin`,
  UPVOTE:`${API_BASE_URL}/posts/upvote`,
  DOWNVOTE:`${API_BASE_URL}/posts/downvote`,
  UPLOAD_IMAGE: `${API_BASE_URL}/images/upload`,
  UPDATE_PROFILE_PICTURE: `${API_BASE_URL}/users/update-picture`,
  LOAD_COMMUNITY: `${API_BASE_URL}/communities/getCommunity`,
  JOINED_COMMUNITITES: `${API_BASE_URL}/communities/joinedCommunities`
};
