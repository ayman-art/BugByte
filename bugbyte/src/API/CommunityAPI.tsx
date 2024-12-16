import { API_URLS } from './ApiUrls';


export const getCommunity = async(token :string, id: number)=> {
    try {
        const response = await fetch(`${API_URLS.LOAD_COMMUNITY}?id=${id}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
        });
    
        if (!response.ok) {
          throw new Error(`Failed to Fetch Community`);
        }
    
        const data = await response.json();
        return data;
      } catch (error) {
        console.error('Error in Fetching:', error);
        throw error;
      }
}