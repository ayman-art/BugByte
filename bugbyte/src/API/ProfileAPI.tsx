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
      const response = await fetch(API_URLS.UPLOAD_IMAGE, {
        method:'POST',
        
        body: formData
      });
      if(!response.ok){
        throw new Error(`Uploading Image Failed`)
      }
      const url = await response.text();
      console.log(url)
      const response2 = await fetch(`${API_URLS.UPDATE_PROFILE_PICTURE}`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body:JSON.stringify({
          'url': url
        })
      });
      if(!response2.ok){
        throw new Error(`Updating Profile Picture failed`)
      }
      return url
    //   const response3 = await fetch(url)
    //   if(!response3.ok){
    //     throw new Error(`Downloading Image Failed`)
    //   }
    //   return response
    } catch (error) {
      throw error;
    }
  };
  export const getUserPosts = async(token:string,limit: number, offset: number)=>{
    const response = await fetch(`${API_URLS.USER_POSTS}?&limit=${limit}&offset=${offset}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
    });
  
    if (!response.ok) {
      throw new Error(`Failed to Fetch User posts`);
    }
  
    const posts = await response.json()
    console.log(posts)
    return posts;
  }