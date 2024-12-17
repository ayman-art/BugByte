import React, { useEffect, useRef, useState } from "react";
import CommunityPost from "./QuestionPreview";

export interface Post {
  id: string;
  creatorUserName: string;
  mdContent: string;
  postedOn: Date;
  title: string;
  communityId: number;
  upVotes: number;
  downVotes: number;
  tags?: string[];
  communityName?: string;
  isUpvoted: boolean;
  isDownVoted: boolean;
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
  const [lock, setLock] = useState<boolean>(false);
  const loader = useRef<HTMLDivElement>(null);

  // Observe when the loader div comes into view
  useEffect(() => {
    const observer = new IntersectionObserver(
      async (entries) => {
        if (entries[0].isIntersecting && hasMore && !lock) {
          console.log(hasMore);
          setLock(true);
          await fetchPosts(); // Fetch posts when loader is visible
          setLock(false);
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
          id={post.id}
          creatorUserName={post.creatorUserName}
          mdContent={post.mdContent}
          postedOn={post.postedOn} // Example: Replace with actual `postedOn` field if available
          title={post.title || "Untitled Post"} // Ensure `post.title` is valid or fallback to a default
          communityId={post.communityId}
          upVotes={post.upVotes}
          downVotes={post.downVotes}
          tags={post.tags}
          communityName={post.communityName || `Community ${post.communityId}`}
          isUpvoted={post.isUpvoted} // Replace with actual logic
          isDownVoted={post.isDownVoted} // Replace with actual logic
        />
      ))}

      {loading && <p>Loading...</p>}
      {!hasMore && <p>No more posts to load.</p>}
      <div ref={loader} style={{ height: "20px" }} />
    </div>
  );
};

export default PostListing;
