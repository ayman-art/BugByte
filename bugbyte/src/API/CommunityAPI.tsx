import { API_URLS } from "./ApiUrls";
import { fetchJoinedCommunities } from "./HomeAPI";

export const getCommunity = async (token: string, id: number) => {
  try {
    const response = await fetch(`${API_URLS.LOAD_COMMUNITY}?id=${id}`, {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    });

    if (!response.ok) {
      throw new Error(`Failed to Fetch Community`);
    }

    const data = await response.json();
    console.log(data);
    return data;
  } catch (error) {
    console.error("Error in Fetching:", error);
    throw error;
  }
};

export const joinCommunity = async (token: string, id: number) => {
  const response = await fetch(`${API_URLS.JOIN_COMMUNITY}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
      communityId: id,
    }),
  });

  if (!response.ok) {
    throw new Error(`Failed to Join Community`);
  }
  const joinedCommunities = await fetchJoinedCommunities();
  localStorage.setItem("joinedCommunities", JSON.stringify(joinedCommunities));
  const data = await response.text();
  return data;
};
export const getCommunityPosts = async (
  token: string,
  id: number,
  limit: number,
  offset: number
) => {
  const response = await fetch(
    `${API_URLS.COMMUNITY_POSTS}?communityId=${id}&limit=${limit}&offset=${offset}`,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );

  if (!response.ok) {
    throw new Error(`Failed to Fetch Community posts`);
  }

  const posts = await response.json();
  console.log(posts);
  return posts;
};
export const fetchCommunities = async (
  token: string,
  page: number,
  size: number
) => {
  try {
    const response = await fetch(
      `${API_URLS.ALL_COMMUNITIES}?page=${page}&size=${size}`,
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      }
    );

    if (!response.ok) {
      console.error("Error while getting communities", response.statusText);
      return [];
    }

    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error fetching communities:", error);
    return [];
  }
};
export const LeaveCommunity = async (token: string, name: string) => {
  const response = await fetch(
    `${API_URLS.LEAVE_COMMUNITY}?communityName=${name}`,
    {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );

  if (!response.ok) {
    throw new Error(`Failed to Leave Community`);
  }
  const joinedCommunities = await fetchJoinedCommunities();
  localStorage.setItem("joinedCommunities", JSON.stringify(joinedCommunities));
  const data = await response.text();
  console.log(response);
  return data;
};

export const createCommunity = async (
  name: string,
  description: string,
  tags: string[],
  token: String
): Promise<any> => {
  try {
    const response = await fetch(API_URLS.CREATE_COMMUNITY, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify({ name, description, tags }),
    });

    if (!response.ok) {
      throw new Error(`failed to create the community: ${response.statusText}`);
    }

    return response;
  } catch (error) {
    console.error("Error in creating community:", error);
    throw error;
  }
};

export const fetchCommunityMembers = async (communityId: string) => {
  const token = localStorage.getItem("authToken");
  const response = await fetch(`${API_URLS.COMMUNTIY_MEMBERS}/${communityId}`, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error("Failed to fetch community members");
  }

  return await response.json();
};

export const updateCommunity = async (
  jwt: string,
  communityId: number,
  updatedData: any
) => {
  const body = {
    id: communityId,
    name: updatedData.name,
    description: updatedData.description,
    tags: updatedData.tags,
  };
  console.log(JSON.stringify(body));
  const response = await fetch(`${API_URLS.UPDATE_COMMUNITY}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${jwt}`,
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    throw new Error("Failed to fetch community members");
  }

  return updatedData;
};
