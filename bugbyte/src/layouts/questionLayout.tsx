import React, { useState } from 'react';
import { ArrowUp, ArrowDown, Container } from 'lucide-react';
import { button } from '@nextui-org/react';

interface CommunityPostProps {
  postId: string;
  communityName: string;
  authorName: string;
  content?: string;
  upvotes: number;
  downvotes: number;
}

const CommunityPost: React.FC<CommunityPostProps> = ({
  postId,
  communityName = "Community Name",
  authorName = "Author Name",
  content = "",
  upvotes: initialUpvotes = 150,
  downvotes: initialDownvotes = 30,
}) => {
  const [upvotes, setUpvotes] = useState(initialUpvotes);
  const [downvotes, setDownvotes] = useState(initialDownvotes);

  const handleVote = async (type: 'up' | 'down') => {
    try {
        if(type == 'up'){
        const response = await fetch(`http://posts/upvote/${postId}`, {
            method: 'POST',
            headers: {
            'Content-Type': 'application/json',
            },
            body: JSON.stringify({ voteType: postId }),
        });

            if (response.ok) {
                if (type === 'up') {
                    setUpvotes(prev => prev + 1);
                }
            }
        }
        } catch (error) {
            console.error('Error updating vote:', error);
        }

        try{
        if (type == 'down') {
            const response = await fetch(`http://posts/downvote/${postId}`, {
                method: 'POST',
                headers: {
                'Content-Type': 'application/json',
                },
                body: JSON.stringify({ voteType: postId }),
            });
    
                if (response.ok) {
                    if (type === 'down') {
                        setDownvotes(prev => prev + 1);
                    }
                }
        }
    } catch (error) {
        console.error('Error updating vote:', error);
    }

  };

  const handleContentClick = (e: React.MouseEvent) => {

    if (
      (e.target as HTMLElement).closest('.vote-button') ||
      (e.target as HTMLElement).closest('.author-link') ||
      (e.target as HTMLElement).closest('.community-link')
    ) {
      return;
    }
    
    window.location.href = `/posts/${postId}`;
  };

  const stopPropagation = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  return (
    <div className="p-4">
      <div>
        <div style={styles.container} onClick={handleContentClick}>

            <div style={styles.postBox}>
                <a 
                    onClick={stopPropagation}
                    style={styles.postNaming}
                >
                    {communityName} <br></br>
                </a>
            </div>

            <div style={styles.postBox}>
                <a 
                    href={`/Profile/${authorName}`}
                    onClick={stopPropagation}
                    style={styles.postNaming}
                >
                    {authorName}
                </a>
            </div>

        <div style={styles.content}>
            {content}
        </div>

        <div onClick={stopPropagation} style={{ display: 'flex', gap: '8px' }}>
            <button onClick={() => handleVote('up')} style={styles.upVoteButton}>
                <ArrowUp/>
                <span>{upvotes}</span>
            </button>
            <button onClick={() => handleVote('down')} style={styles.downVoteButton}>
                <ArrowDown/>
                <span>{downvotes}</span>
            </button>
        </div>
      </div>
    </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
    postBox: {
        border: '3px solid #B1E9A3',
        borderRadius: '10px',
        color: 'black',
    },

    postNaming: {
        marginLeft: '10px',
        textDecoration: 'none',
        color: 'black'
    },

    container: {
      height: '332px',
      width: '787px',
      display: 'flex',
      flexDirection: 'column',
      justifyContent: 'space-between',
      padding: '20px',
      boxSizing: 'border-box',
      border: '3px solid #4caf50',
      borderRadius: '10px',
      backgroundColor: '#ffffff',
    },
  
    content: {
      background: '#F4F4F4',
      height: '160px',
      width: '100%',
      borderRadius: '10px',
      padding: '10px',
      overflow: 'hidden',
      textOverflow: 'ellipsis',
      textAnchor: 'start',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
    },
  
    upVoteButton: {
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      width: '80px',
      padding: '5px',
      backgroundColor: '#b1e9a3',
      border: 'none',
      borderRadius: '20px',
      cursor: 'pointer',
    },

    downVoteButton: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        width: '80px',
        padding: '5px',
        backgroundColor: 'red',
        border: 'none',
        borderRadius: '20px',
        cursor: 'pointer',
      },

  };
  

export default CommunityPost;