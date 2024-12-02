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