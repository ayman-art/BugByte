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
