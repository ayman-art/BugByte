import React, { useEffect, useRef, useState } from "react";
import CommunityPreview from "./CommunityPreview";
import { Community } from "../Models/Community";

interface CommunityListingProps {
  comms: Community[];
  fetchCommunities: () => void;
  loading: boolean;
  hasMore: boolean;
}

const CommunityListing: React.FC<CommunityListingProps> = ({
  comms,
  fetchCommunities,
  loading,
  hasMore,
}) => {
  const [lock, setLock] = useState<boolean>(false);
  const loader = useRef<HTMLDivElement>(null);

  // Observe when the loader div comes into view
  useEffect(() => {
    const observer = new IntersectionObserver(
      async (entries) => {
        if (entries[0].isIntersecting && hasMore) {
          console.log(hasMore); fetchCommunities(); 
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
  }, [loader, fetchCommunities, hasMore, lock]);

  return (
    <div>
      {comms.map((comm) => (
        <CommunityPreview key={comm.id} community={comm} />
      ))}

      {loading && <p>Loading...</p>}
      {!hasMore && <p>No more communities to load.</p>}
      <div ref={loader} style={{ height: "20px" }} />
    </div>
  );
};

export default CommunityListing;
