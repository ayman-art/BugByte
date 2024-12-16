import React, { useState, useEffect } from 'react';
import { getCommunity } from '../API/CommunityAPI';
import { useParams } from 'react-router-dom';

interface Community {
  id: number;
  name: string;
  description: string;
  creationDate: string;
}

const CommunityPage: React.FC = () => {
  const [community, setCommunity] = useState<Community | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [joined, setJoined] = useState<boolean>(false); // Track if the community is joined
  const { communityId } = useParams<{ communityId: string }>();

  useEffect(() => {
    const fetchCommunityData = async () => {
      try {
        const jwt = localStorage.getItem('authToken');

        if (!jwt) {
          throw new Error('User is not authenticated');
        }

        const data: Community = await getCommunity(jwt, parseInt(communityId!));
        setCommunity(data);

        // Check if the community is in the user's joined list
        const joinedCommunities = JSON.parse(localStorage.getItem('joinedCommunities') || '[]');
        setJoined(joinedCommunities.includes(data.id)); // Update the joined state
      } catch (err: any) {
        setError(err.message || 'An error occurred');
      } finally {
        setLoading(false);
      }
    };

    fetchCommunityData();
  }, [communityId]);

  const handleJoinClick = () => {
    if (joined) {
      // Remove the community from the joined list
      const joinedCommunities = JSON.parse(localStorage.getItem('joinedCommunities') || '[]');
      const updatedJoinedCommunities = joinedCommunities.filter((id: number) => id !== community?.id);
      localStorage.setItem('joinedCommunities', JSON.stringify(updatedJoinedCommunities));
      setJoined(false);
    } else {
      // Add the community to the joined list
      const joinedCommunities = JSON.parse(localStorage.getItem('joinedCommunities') || '[]');
      joinedCommunities.push(community?.id);
      localStorage.setItem('joinedCommunities', JSON.stringify(joinedCommunities));
      setJoined(true);
    }
  };

  const styles: { [key: string]: React.CSSProperties } = {
    container: {
      padding: '20px',
      fontFamily: 'Arial, sans-serif',
      width: '100%', // Full width of the page
      minHeight: '100vh', // Ensures the container covers the full viewport height
      backgroundColor: '#f9f9f9',
      boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
    },
    header: {
      textAlign: 'center',
      color: '#333',
      marginBottom: '20px',
    },
    title: {
      fontSize: '24px',
      fontWeight: 'bold',
      color: '#4CAF50',
    },
    label: {
      fontWeight: 'bold',
      color: '#555',
    },
    value: {
      color: '#333',
      marginBottom: '10px',
    },
    divider: {
      margin: '20px 0',
      border: 'none',
      height: '2px', // Makes the divider thicker
      backgroundColor: '#4CAF50', // Adds a strong green color for visibility
    },
    error: {
      color: '#d32f2f',
      textAlign: 'center',
    },
    loading: {
      color: '#888',
      textAlign: 'center',
    },
    button: {
      padding: '8px 12px', // Smaller padding for a smaller button
      fontSize: '14px', // Smaller font size
      color: 'white',
      backgroundColor: '#4CAF50',
      border: 'none',
      borderRadius: '4px',
      cursor: 'pointer',
      marginTop: '20px',
      width: 'auto', // Auto width to prevent it from stretching
      float: 'right', // Align the button to the right
    },
    buttonLeave: {
      backgroundColor: '#d32f2f', // Red background for "Leave" button
    },
  };

  if (loading) return <div style={styles.loading}>Loading...</div>;
  if (error) return <div style={styles.error}>Error: {error}</div>;

  return (
    <div style={styles.container}>
      {community ? (
        <div>
          <h1 style={styles.title}>{community.name}</h1>
          <div>
            <p style={styles.value}>
              <span style={styles.label}>Description:</span> {community.description}
            </p>
            <p style={styles.value}>
              <span style={styles.label}>Created on:</span>{' '}
              {new Date(community.creationDate).toLocaleDateString()}
            </p>
          </div>
          {/* Join/Leave Button above the divider */}
          <button
            style={{
              ...styles.button,
              ...(joined ? styles.buttonLeave : {}),
            }}
            onClick={handleJoinClick}
          >
            {joined ? 'Leave' : 'Join'} Community
          </button>

          <hr style={styles.divider} /> {/* Clearer divider */}
        </div>
      ) : (
        <p style={styles.value}>No community data available.</p>
      )}
    </div>
  );
};

export default CommunityPage;
