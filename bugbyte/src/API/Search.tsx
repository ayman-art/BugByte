import { API_URLS } from "./ApiUrls";

interface Community {
  id: number;
  name: string;
  description: string;
  adminId: number;
  creationDate: string;
  tags: string[];
}
interface Question {
  id: number;
  creatorUserName: string;
  mdContent: string;
  postedOn: string;
  title: string;
  communityId: number;
  upVotes: number;
  downVotes: number;
  validatedAnswerId: number;
  tags: string[];
}
export const searchCommunities = async (
  content: string,
  page: number,
  size: number
) => {
  try {
    const response = await fetch(
      `${API_URLS.SEARCH_COMMUNITIES}?content=${content}&page=${page}&size=${size}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${localStorage.getItem("authToken")}`,
        },
      }
    );
    const data = await response.json();
    console.log(JSON.stringify(data));
    return data.communities;
  } catch (error) {
    console.log("error");
  }
};

export const searchQuestions = async (
  content: string,
  page: number,
  size: number,
  token: String
) => {
  try {
    const response = await fetch(
      `${API_URLS.SEARCH_QUESTIONS}?content=${content}&page=${page}&size=${size}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );
    console.log("asdfasdf");
    const data = await response.json();
    console.log("asdfasdf");
    return data.questions;
  } catch (error) {
    console.log("error");
  }
};
