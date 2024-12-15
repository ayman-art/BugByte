import React, { useEffect, useState } from "react";
import { getFollowings } from '../API/FollowingsAPI';

const followings: React.FC = () => {
    const username = localStorage.getItem("name");
    const token = localStorage.getItem('authToken');
    const [followings, setfollowings] = useState<any[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        if (username) {
            getFollowings(username, token!).then((fetchedFollowings) => {
                setfollowings(fetchedFollowings);
                setLoading(false);
            }).catch((error) => {
                console.error("Error fetching followings:", error);
                setLoading(false);
            });
        }
    }, [username]);

    const directFollowing = (followingUsername: string) => {
        
    };

    return(
        <div style={styles.container} onClick={directFollowing}>
            {loading ? "Loading..." : (
                followings.length > 0 ? (
                    <div>
                        <h3>followings of {username}:</h3>
                        <ul>
                            {followings.map((following, index) => (
                                <div
                                    key={index}
                                    style={styles.field}
                                    onClick={() => directFollowing(following.userName)}
                                >
                                    <a href={`/Profile/${following.userName}`} style={styles.followingName}>{following.userName}</a>
                                    <a style={styles.followingReputation}> Reputation {following.reputation}</a>
                                    <a style={styles.followingBio}>{following.bio}</a>
                                </div>
                            ))}
                        </ul>
                    </div>
                ) : (
                    <p>No followings found.</p>
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

    followingName: {
        marginLeft: '15px',
        marginTop: '5px',
        color: 'black',
        textDecoration: 'none',
        fontSize: '24px',
        fontWeight: 'bold'
    },

    followingBio: {
        marginLeft: '15px',
        color: 'gray',
        marginTop: '10px',
        wordWrap: 'break-word'
    },

    followingReputation: {
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


export default followings;
