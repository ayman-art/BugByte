import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter } from 'react-router-dom'; // Import BrowserRouter
import App from './App';
import './index.css';
import Login from './component/Login'; // Correct relative path

ReactDOM.render(
  <BrowserRouter> {/* Wrap App in BrowserRouter */}
    <Login />
  </BrowserRouter>,
  document.getElementById('root')
);
export default App; // Ensure you are exporting App as default
