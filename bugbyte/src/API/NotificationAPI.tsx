import { API_URLS } from "./ApiUrls";


export const fetchNotifications = async()=>{
    const token = localStorage.getItem("authToken");
    try {
        const response = await fetch(`${API_URLS.FETCH_NOTIFICATIONS}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        }
        });

        if (!response.ok) {
            throw new Error(`${response.status}`);
        }
        return await response.json();
    } catch (error) {
        console.error('Error Fetching nostificatoins:', error);
        throw error;
    }
}