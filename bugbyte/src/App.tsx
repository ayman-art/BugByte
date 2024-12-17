import React, { useEffect, useState } from "react";
import {
  BrowserRouter,
  Navigate,
  Route,
  Routes,
  useNavigate,
} from "react-router-dom";
import QuestionSearchPage from "./pages/QuestionsSearchPage";
import Login from "./pages/LoginPage";
import SignUpPage from "./pages/SignupPage";
import ProfilePage from "./pages/ProfilePage";
import FollowersPage from "./pages/FollowersPage";
import FollowingsPage from "./pages/FollowingsPage";
import { authorizeToken, saveData } from "./API/HomeAPI";
import Layout from "./layouts/MainLayout";
import TestPage from "./pages/TestPage";
import CommunityPage from "./pages/CommunityPage";
import { Home } from "lucide-react";
import HomePage from "./pages/HomePage";
import CommunityListingPage from "./pages/CommunityListPage";
import CommunitySearchPage from "./pages/CommunitySearchPage";

const isLoggedIn = false;

const App: React.FC = () => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);
  const handleLogout = () => {
    // Clear auth token and state

    localStorage.removeItem("authToken");
    setIsAuthenticated(false);
  };
  const handleLogin = () => {
    // Clear auth token and state

    setIsAuthenticated(true);
  };
  useEffect(() => {
    const check = async () => {
      const token = localStorage.getItem("authToken");
      if (token) {
        try {
          const data = await authorizeToken(token);
          const { jwt } = data;
          localStorage.setItem("authToken", jwt);
          saveData(jwt);
          setIsAuthenticated(true); // Set to true when token is valid
        } catch (err) {
          localStorage.removeItem("authToken");
          setIsAuthenticated(false); // Set to false on error
          console.log(err);
        }
      } else {
        setIsAuthenticated(false); // No token, so not authenticated
      }
    };
    check();
  }, []);
  if (isAuthenticated === null) {
    return <div>Loading...</div>; // Loading screen or spinner while checking token
  }
  return (
    <BrowserRouter>
      <Routes>
        {isAuthenticated ? (
          <>
            <Route
              path="/"
              element={
                <Layout onLogout={handleLogout}>
                  <HomePage />
                </Layout>
              }
            />
            <Route
              path="/Profile/:userName"
              element={
                <Layout onLogout={handleLogout}>
                  <ProfilePage />
                </Layout>
              }
            />
            <Route
              path="/Profile/:userName/Followers"
              element={
                <Layout onLogout={handleLogout}>
                  <FollowersPage />
                </Layout>
              }
            />
            <Route
              path="/Profile/:userName/Followings"
              element={
                <Layout onLogout={handleLogout}>
                  <FollowingsPage />
                </Layout>
              }
            />
            <Route
                path="communities/:communityId"
                element={
                  <Layout onLogout={handleLogout}>
                    <CommunityPage />
                  </Layout>
                }
            />
            <Route
              path="/communities"
              element={
                <Layout onLogout={handleLogout}>
                  <CommunityListingPage/>
                </Layout>
              }
            />
            {/*<Route path="/Home" element={<Layout onLogout={handleLogout}><HomePage /></Layout>} />*/}
            <Route path="/SignUp" element={<Navigate to="/" />} />
            <Route path="/LogIn" element={<Navigate to="/" />} />
            <Route path="/search-community" element={<Layout onLogout={handleLogout}><CommunitySearchPage/></Layout>}/>
            <Route
              path="/Search"
              element={
                <Layout onLogout={handleLogout}>
                  <QuestionSearchPage />
                </Layout>
              }
            />
          </>
        ) : (
          <>
            <Route path="/" element={<Navigate to="/SignUp" />} />
            <Route
              path="/SignUp"
              element={<SignUpPage onLogin={handleLogin} />}
            />
            <Route path="/LogIn" element={<Login onLogin={handleLogin} />} />
          </>
        )}
        
      </Routes>
    </BrowserRouter>
  );
};

export default App;
