import React, { useEffect, useState } from "react";
import { getFollowers } from '../API/FollowersAPI';
import { useParams } from "react-router-dom";

const Followers: React.FC = () => {
    const {userName} = useParams<{ userName: string }>();
    const token = localStorage.getItem('authToken');
    const [followers, setFollowers] = useState<any[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        if (userName) {
            getFollowers(userName, token!).then((fetchedFollowers) => {
                setFollowers(fetchedFollowers);
                setLoading(false);
            }).catch((error) => {
                console.error("Error fetching followers:", error);
                setLoading(false);
            });
        }
    }, [userName]);

    const directFollower = (followerUsername: string) => {
        
    };

    return(
        <div style={styles.container} onClick={directFollower}>
            {loading ? "Loading..." : (
                followers.length > 0 ? (
                    <div>
                        <h3>Followers of {userName}:</h3>
                        <ul>
                            {followers.map((follower, index) => (
                                <div
                                    key={index}
                                    style={styles.field}
                                    onClick={() => directFollower(follower.userName)}
                                >
                                    <a href={`/Profile/${follower.userName}`} style={styles.followerName}>{follower.userName}</a>
                                    <a style={styles.followerReputation}> Reputation {follower.reputation}</a>
                                    <a style={styles.followerBio}>{follower.bio}</a>
                                </div>
                            ))}
                        </ul>
                    </div>
                ) : (
                    <p>No followers found.</p>
                )
            )}
        </div>
    );
}

const styles = {
    container: {
        height: '83vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'flex-start',
        paddingBottom: '20px',
        overflow: 'auto'
    },

    field: {
        border: '3px solid #B1E9A3',
        width: '400px',
        borderRadius: '10px',
        color: 'black',
        margin: '20px',
        marginLeft: "250px",
        padding: '10px', // Adds some padding inside the box
        minHeight: '80px', // Minimum height to ensure space even if the bio is small
        display: 'flex',
        flexDirection: 'column', // Stacks the username and bio vertically
        justifyContent: 'flex-start'// Ensures the content is aligned from the top
    },

    followerName: {
        marginLeft: '15px',
        marginTop: '5px',
        color: 'black',
        textDecoration: 'none',
        fontSize: '24px',
        fontWeight: 'bold'
    },

    followerBio: {
        marginLeft: '15px',
        color: 'gray',
        marginTop: '10px',
        wordWrap: 'break-word'
    },

    followerReputation: {
        marginLeft: '30px',
        marginTop: '5px',
        fontSize: '18px',
        fontWeight: 'bold',
        backgroundImage: 'linear-gradient(to right, orange, #FF7F50)',
        color: 'transparent',
        backgroundClip: 'text',
        WebkitBackgroundClip: 'text',
    }
    
}


export default Followers;
