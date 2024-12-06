import React, { ReactNode } from 'react';
import Navbar from '../components/NavBar';
import { useNavigate } from 'react-router-dom';

interface LayoutProps {
  children: ReactNode;
  onLogout: () => void; 
}

const Layout: React.FC<LayoutProps> = ({ children, onLogout }) => {
  const navigate = useNavigate()
  const visitProfile = ()=>{
    const username = localStorage.getItem("name");
    navigate(`/Profile/${username}`)
  }
  const handleLogout = () => {
    onLogout()
    navigate('/')
  };
  return (
    <div style={styles.container}>
      {/* Navbar */}
      <Navbar onLogout={handleLogout}/>
      {/* Main Content */}
      <div style={styles.main}>
        {/* Sidebar Links on Left */}
        <aside style={styles.sidebar}>
          <center>
            <ul style={styles.links}>
              
              <aside style={styles.link_option1}>
                <li onClick={()=>{navigate('/')}}>Home</li>
              </aside>

              <aside style={styles.link_option2}>
                <li onClick={visitProfile}>Profile</li>
              </aside>
              
              <aside style={styles.link_option3}>
                <li onClick={()=>{}}>Communities</li>
              </aside>

            </ul>
          </center>
        </aside>

        {/* Page Content in Center */}
        <main style={styles.content}>{children}</main>

        {/* Empty space on the right */}
        <div style={styles.emptySpace}></div>
      </div>
    </div>
  );
};

const styles: { [key: string]: React.CSSProperties } = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    backgroundColor: '#f5f5f5', // Page background color
  },
  navbar: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '0 20px',
    height: '50px',
    backgroundColor: '#4caf50',
    color: 'white',
  },
  main: {
    display: 'flex',
    flex: 1,
    padding: '0 10%',
  },
  sidebar: {
    fontSize: '20px',
    backgroundColor: '#f5f5f5', // Same background color as page background
    marginLeft: '-100px',
    width: '200px',
    padding: '10px',
    borderRadius: '5px',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'space-between', // Distribute space top and bottom
    height: '100%', // Ensure the sidebar takes up full height
  },
  links: {
    listStyle: 'none',
    padding: 0,
    marginTop: '-50px', 
    display: 'flex',
    flexDirection: 'column', // Stack the links vertically
    gap: '10px',
    paddingTop: '100%', // Adjust this to control the 1:2 centering ratio
    cursor:'pointer',
    userSelect:'none'
  },

  link_option1: {
    marginTop: '50px',
    height: '50px',
    background: '#7DDDDE',
    borderRadius: '15px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    textAlign: 'center',
  },
  
  link_option2: {
    marginTop: '50px',
    height: '50px',
    background: '#A5CFA1',
    borderRadius: '15px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    textAlign: 'center',
  },

  link_option3: {
    marginTop: '50px',
    height: '50px',
    background: '#9EEA65',
        borderRadius: '15px',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    textAlign: 'center',
  },

  content: {
    flex: 1,
    backgroundColor: 'white',
    padding: '20px',
    marginLeft: '20px',
    marginRight: '20px',
    borderRadius: '5px',
    borderBottom: 'None',
    boxShadow: '0 2px 5px rgba(0, 0, 0, 0.1)',
  },
  emptySpace: {
    width: '50px',  // You can adjust this width if needed
  },
};

export default Layout;
