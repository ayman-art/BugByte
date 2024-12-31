import {API_URLS} from './ApiUrls';
export const setModerator = async (
  token: string,
  moderatorName: string,
  communityId: number
) => {
  const url = `${API_URLS.SET_MODERATOR}/${communityId}/${moderatorName}`;
  const response = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error("Failed to set moderator");
  }

  return response.text();
};

export const removeModerator = async (
  token: string,
  communityId: number,
  moderatorName: string
) => {
  const url = `${API_URLS.REMOVE_MODERATOR}/${communityId}/${moderatorName}`;
  console.log(url);
  const response = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error("Failed to remove moderator");
  }

  return response.text();
};

export const removeMember = async (
  token: string,
  communityId: number,
  userName: string
) => {
  const url = `${API_URLS.REMOVE_MEMBER}/${communityId}/${userName}`;
    console.log(url);

  const response = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    throw new Error("Failed to remove member");
  }

  return response.text();
};

export const isModerator = async (token: string, id: number) => {
    console.log(id);
    const response = await fetch(`${API_URLS.IS_MODERATOR}/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    });

    if (!response.ok) {
        throw new Error('Failed to Fetch isModerator');
    }

    const data = await response.json();
    console.log('isModerator:', data);
    return data;
};
export const isModeratorByName = async (token: string, id: number, name: string) => {
    console.log(id);
    console.log(`${API_URLS.IS_MODERATOR_BY_NAME}/${id}/${name}`);  // Use 'id' instead of 'communityId'
    const response = await fetch(`${API_URLS.IS_MODERATOR_BY_NAME}/${id}/${name}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    });

    if (!response.ok) {
        throw new Error('Failed to Fetch isModerator');
    }

    const data = await response.json();
    console.log('isModeratorByName:', data);
    return data;
};
export const isAdminByName = async (token: string,  name: string) => {
    console.log(`${API_URLS.IS_MODERATOR_BY_NAME}/${name}`);
    const response = await fetch(`${API_URLS.IS_ADMIN}/${name}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    });

    if (!response.ok) {
        throw new Error('Failed to Fetch isAdminByName');
    }

    const data = await response.json();
    console.log('isAdmin:', data);
    return data;
};
