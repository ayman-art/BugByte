import { API_URLS } from './ApiUrls';
import { fetchJoinedCommunities } from './HomeAPI';


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
        console.log(data)
        return data;
      } catch (error) {
        console.error('Error in Fetching:', error);
        throw error;
      }
}

export const joinCommunity = async(token: string, id: number)=>{
    
        const response = await fetch(`${API_URLS.JOIN_COMMUNITY}`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
          body:JSON.stringify({
            'communityId': id
          })
        });
    
        if (!response.ok) {
          throw new Error(`Failed to Join Community`);
        }
        const joinedCommunities = await fetchJoinedCommunities()
        // localStorage.setItem('joinedCommunities', JSON.stringify(joinedCommunities));
        const data = await response.text();
        return data;
      
}