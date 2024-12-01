import React, { ReactNode } from 'react';
import Navbar from '../components/NavBar';

interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div style={styles.container}>
      {/* Navbar */}
      <Navbar/>

      {/* Main Content */}
      <div style={styles.main}>
        {/* Sidebar Links on Left */}
        <aside style={styles.sidebar}>
          <ul style={styles.links}>
            <li>Home</li>
            <li>Profile</li>
            <li>Communities</li>
          </ul>
        </aside>

        {/* Page Content in Center */}
        <main style={styles.content}>{children}</main>

        {/* Empty space on the right */}
        <div style={styles.emptySpace}></div>
      </div>
    </div>
  );
};

// Styles Object
const styles: { [key: string]: React.CSSProperties } = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    height: '100vh',
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
    width: '200px',
    backgroundColor: '#f5f5f5', // Same background color as page background
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
    margin: 0,
    display: 'flex',
    flexDirection: 'column', // Stack the links vertically
    gap: '10px',
    paddingTop: '100%', // Adjust this to control the 1:2 centering ratio
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
    width: '200px',  // You can adjust this width if needed
  },
};

export default Layout;
