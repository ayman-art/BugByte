import React from 'react';

import CommunityPost from '../layouts/questionLayout';

const HomePage: React.FC = () => {
    return (
            
            <div>

                <div>
                <CommunityPost 
                    postId="123"
                    communityName="depressed programmers"
                    authorName="Ali Al jayyar"
                    content="asking questions"
                    upvotes={150}
                    downvotes={30}
                    />
                </div>

                <div>
                    <CommunityPost 
                        postId="125"
                        communityName="depressed lawyers searching for a career shifts"
                        authorName="Saul Goodman"
                        content="asking another questions"
                        upvotes={1000}
                        downvotes={0}
                        />
                </div>

                <div>
                    <CommunityPost 
                        postId="126"
                        communityName="new mexico community"
                        authorName="Walter White"
                        content="just ask"
                        upvotes={0}
                        downvotes={0}
                        />
                </div>
            </div>

      );
};

export default HomePage;