import { API_URLS } from './ApiUrls';
export const getProfile = async (name: string, token:string): Promise<any> => {
  try {
    const response = await fetch(`${API_URLS.PROFILE}?username=${name}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
    });

    if (!response.ok) {
      throw new Error(`Loading profile Failed: ${response.statusText}`);
    }

    const data = await response.json();

    return data;
  } catch (error) {
    console.error('Error in Loading Profile:', error);
    throw error;
  }
};
export const updateBio = async (bio: string, token:string): Promise<any> => {
    try {
      const response = await fetch(`${API_URLS.BIO_UPDATE}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            'bio': bio
        })
      });
  
      if (!response.ok) {
        throw new Error(`Updating Bio Failed: ${response.statusText}`);
      }
  
      const data = await response.json();
  
      return data;
    } catch (error) {
      console.error('Error in Loading Profile:', error);
      throw error;
    }
  };

  export const followUser = async (username: string, token:string): Promise<any> => {
    try {
      const response = await fetch(`${API_URLS.FOLLOW}?username=${username}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
      });
  
      if (!response.ok) {
        throw new Error(`Follow operation Failed: ${response.statusText}`);
      }
  
      const data = await response.text();
  
      return data;
    } catch (error) {
      console.error('Error in Following Profile:', error);
      throw error;
    }
  };

  export const unfollowUser = async (username: string, token:string): Promise<any> => {
    try {
      const response = await fetch(`${API_URLS.UNFOLLOW}?username=${username}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
      });
  
      if (!response.ok) {
        throw new Error(`Follow operation Failed: ${response.statusText}`);
      }
  
      const data = await response.text();
  
      return data;
    } catch (error) {
      console.error('Error in Following Profile:', error);
      throw error;
    }
  };
  export const makeAdmin= async (username: string, token:string): Promise<any> => {
    try {
      const response = await fetch(`${API_URLS.ADMIN}?username=${username}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
      });
  
      if (!response.ok) {
        throw new Error(`Admin operation Failed: ${response.statusText}`);
      }
  
      const data = await response.text();
  
      return data;
    } catch (error) {
      console.error('Error:', error);
      throw error;
    }
  };
  export const updateProfilePicture = async (formData: FormData, token: string) => {
    try {
      const response = await fetch('/api/profile/picture', {
        method:'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        },
        body: formData
      });
      return await response.text();
    } catch (error) {
      throw error;
    }
  };