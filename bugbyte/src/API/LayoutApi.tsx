import { API_URLS } from "./ApiUrls";
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

    console.log("Request to API:", API_URLS.CREATE_COMMUNITY);
    console.log("Request Body:", { name, description });

    if (!response.ok) {
      throw new Error(`failed to create the community: ${response.statusText}`);
    }
    const data = await response.text();

    console.log("create Community Response:", data);

    return response;
  } catch (error) {
    console.error("Error in logIn:", error);
    throw error;
  }
};
