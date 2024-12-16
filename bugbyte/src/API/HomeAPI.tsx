import { API_URLS } from './ApiUrls';

export const authorizeToken = async(token :string)=> {
    try {
        const response = await fetch(API_URLS.AUTHENTICATE_TOKEN, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          },
        });
    
        if (!response.ok) {
          throw new Error(`Failed to authenticate token`);
        }
    
        const data = await response.json();
        return data;
      } catch (error) {
        console.error('Error in Authentication:', error);
        throw error;
      }
}
export const clearData = ()=>{
    localStorage.removeItem("is_admin")
    localStorage.removeItem("name")
    localStorage.removeItem("id")
}
export const saveData = (token:string)=>{
    const [header, payload, signature] = token.split('.');

    // Decode payload
    const decodedPayload = base64urlDecode(payload);

    // Parse the payload into a JavaScript object
    const decodedObj = JSON.parse(decodedPayload);
    localStorage.setItem("is_admin", decodedObj.is_admin)
    console.log(decodedObj.is_admin);
    localStorage.setItem("name", decodedObj.sub)
    localStorage.setItem("id", decodedObj.id)
}

function base64urlDecode(base64url: string) {
    // Base64url to Base64
    let base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
    // Decode the Base64 string
    let decodedData = atob(base64);
    return decodedData;
}

export const fetchJoinedCommunities = async()=>{
  const token = localStorage.getItem("authToken");
  const response = await fetch(API_URLS.JOINED_COMMUNITITES,{
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });
  const data = await response.json()
  if (!response.ok) throw new Error(data["message"])
  console.log(data)

}