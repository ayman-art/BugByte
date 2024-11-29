const API_BASE_URL = "https://";

export const API_URLS = {
  LOGIN: `${API_BASE_URL}/users/login`,
  REGISTER: ``,
  LOGIN_BY_GOOGLE:`https://www.googleapis.com/oauth2/v3/userinfo`,
  FORGOT_PASSWORD:`${API_BASE_URL}/users/reset-password`,
  VALIDATE_CODE: `${API_BASE_URL}/users/validate-email-code`,
  RESEND_CODE: `${API_BASE_URL}/users/resendCode`,
  CHANGE_PASSWORD: `${API_BASE_URL}/users/change-password`,
};
