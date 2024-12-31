import React, { useState, useEffect } from "react";
import { Community } from "../Models/Community.tsx";
import { fetchJoinedCommunities } from "../API/HomeAPI.tsx";
import CommunityPreview from "../components/CommunityPreview.tsx";

const JoinedCommunitiesPage: React.FC = () => {
  const [joinedCommunities, setJoinedCommunities] = useState<Community[]>([]);

  const fetchJoinedComm = async () => {
    try {
      let comms = await fetchJoinedCommunities();
      comms.reverse();

      setJoinedCommunities(comms);
    } catch (e) {
      console.log(e);
    }
  };

  useEffect(() => {
    fetchJoinedComm();
  }, []);

  return (
    <div style={styles.container}>
      {joinedCommunities.map((comm) => (
        <CommunityPreview key={comm.id} community={comm} />
      ))}
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    marginTop: "20px",
    width: "100%",
  },
};

export default JoinedCommunitiesPage;
