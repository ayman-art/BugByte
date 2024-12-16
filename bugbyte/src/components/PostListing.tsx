import React, { useEffect, useRef } from "react";
import CommunityPost from "./QuestionPreview";

interface Post {
  id: string;
  communityId: number;
  title: string;
  creatorUserName: string;
  mdContent: string;
  upVotes: number;
  downVotes: number;
}

interface PostListingProps {
  posts: Post[];
  fetchPosts: () => void;
  loading: boolean;
  hasMore: boolean;
}

const PostListing: React.FC<PostListingProps> = ({
  posts,
  fetchPosts,
  loading,
  hasMore,
}) => {
  const loader = useRef<HTMLDivElement>(null);

  // Observe when the loader div comes into view
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasMore && !loading) {
          fetchPosts(); // Fetch posts when loader is visible
        }
      },
      { threshold: 1.0 }
    );

    if (loader.current) {
      observer.observe(loader.current);
    }

    return () => {
      if (loader.current) observer.unobserve(loader.current);
    };
  }, [loader, fetchPosts]);

  return (
    <div>
      {posts.map((post, index) => (
        <CommunityPost
          key={`${post.id}+${index}`}
          postId={post.id}
          communityName={`${post.communityId}`}
          authorName={post.creatorUserName}
          content={post.mdContent}
          upvotes={post.upVotes}
          downvotes={post.downVotes}
        />
      ))}

      {loading && <p>Loading...</p>}
      {!hasMore && <p>No more posts to load.</p>}
      <div ref={loader} style={{ height: "20px" }} />
    </div>
  );
};

export default PostListing;
