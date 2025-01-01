export interface User {
    username: string;
    email: string;
    password: string;
}

export  interface IQuestion {
    questionId: number;
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
    isUpvoted: boolean;
    isDownvoted: boolean;
  }

  export interface IAnswer {
    answerId: number;
    questionId: number;
    opName: string;
    postedOn: string;
    upVotes: number;
    mdContent: string;
    isDownVoted: boolean;
    isUpVoted: boolean;
    downVotes: number;
    isVerified: boolean;
    enabledVerify: boolean;
    canVerify: boolean;
  }

  export interface IReply {
    replyId: string;
    answerId: string;
    opName: string;
    postedOn: string; // ISO date format as string
    mdContent: string;
  }