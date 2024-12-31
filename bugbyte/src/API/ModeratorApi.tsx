import {API_URLS} from './ApiUrls';

export const deleteQuestion = async(token: string, id: number)=>{
  const response = await fetch(`${API_URLS.QUESTION}?questionId=${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to delete Question`);
  }
    const data = await response.text();
    console.log(response);
  return data;
}

export const deleteAnswer = async(token: string, id: number)=>{
  const response = await fetch(`${API_URLS.ANSWER}?answerId=${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to delete Answer`);
  }
    const data = await response.text();
    console.log(response);
  return data;
}

export const deleteReply = async(token: string, id: number)=>{
  const response = await fetch(`${API_URLS.REPLY}?replyId=${id}`, {
    method: 'DELETE',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
  });

  if (!response.ok) {
    throw new Error(`Failed to delete Reply`);
  }
    const data = await response.text();
    console.log(response);
  return data;
}

export const setModerator = async (token: string, moderatorName:string , communityId:number) => {
  const response = await fetch(API_URLS.SET_MODERATOR, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ "communityId" :communityId,
        "jwt": token,
        "moderatorName":moderatorName
    }),
  });

  if (!response.ok) {
    throw new Error("Failed to set moderator");
  }

  return response.text();
};

export const removeModerator = async (token: string, moderatorName:string, communityId:number) => {
  const response = await fetch(API_URLS.REMOVE_MODERATOR, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ "communityId" :communityId,
            "jwt": token,
            "moderatorName":moderatorName
        }),
      });

  if (!response.ok) {
    throw new Error("Failed to remove moderator");
  }

  return response.text();
};

export const removeMember = async (token: string, communityId:number , user_name : string) => {
  const response = await fetch(API_URLS.REMOVE_MEMBER, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({
        "user_name":user_name,
        "communityId":communityId,
        "jwt":token
    }),
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
