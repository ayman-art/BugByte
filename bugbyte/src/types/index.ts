export interface User {
    username: string;
    email: string;
    password: string;
}

export  interface IQuestion {
    questionId: string;
    upVotes: number;
    mdContent: string;
    isDownVoted: boolean;
    title: string;
    isUpVoted: boolean;
    downVotes: number;
    tags: string[];
    opName: string;
    postedOn: string;
    communityName: string;
    communityId: number;
  }

  export interface IAnswer {
    answerId: number;
    questionId: number;
    opName: string;
    postedOn: string; // ISO date format as string
    upVotes: number;
    mdContent: string;
    isDownVoted: boolean;
    isUpVoted: boolean;
    downVotes: number;
  }