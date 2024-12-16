import { API_URLS } from './ApiUrls';
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
interface Community {
  id: number;
  name: string;
  description: string;
  adminId: number;
  creationDate: string;
  tags: string[];
}
export const searchFilteredQuestions = async (tags: string, page: number, size: number): Promise<Question[]> => {
    try{
          const response = await fetch(
            `${API_URLS.SEARCH_FILTERED_QUESTIONS}?tags=${tags}&page=${page}&size=${size}`,
                   {
                                method: 'GET',
                                headers: {
                                  'Content-Type': 'application/json',
                                  'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                                },
                   }
          );
          const data = await response.json();
                console.log(JSON.stringify(data) );
          return data.questions;
    }
    catch(error )
         {
             console.log("error");
         }
};
export const searchFilteredCommunities = async (tags: string, page: number, size: number): Promise<Question[]> => {
    try{
        const response = await fetch(
          `${API_URLS.SEARCH_FILTERED_QUESTIONS}?tags=${tags}&page=${page}&size=${size}`,
             {
                          method: 'GET',
                          headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${localStorage.getItem('authToken')}`
                          },
             }
        );
        const data = await response.json();
        return data.communities;
     }catch(error )
       {
           console.log("error");
       }
  };