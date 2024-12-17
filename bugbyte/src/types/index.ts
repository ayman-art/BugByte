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