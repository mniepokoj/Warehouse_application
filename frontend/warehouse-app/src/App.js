import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './components/Header';
import HomePage from './components/HomePage';
import Login from './components/Login';

function App() {
  const [isLogged, setIsLogged] = useState(false);

  const handleLogin = () => {
    setIsLogged(true);
  };

  const handleLogout = () => 
  {
    localStorage.removeItem('authToken');
    setIsLogged(false);
  };

  useEffect(() => 
  {
    var token = localStorage.getItem('authToken');
    var isLoggedIn = false;
    if(token != null)
    {
      var tokenParts = token.split("_");
      var tokenDate = tokenParts[1];
      // Konwersja daty na obiekt typu Date
      var tokenDateTime = new Date(tokenDate);
  
      // Pobranie bieżącej daty
      var currentDate = new Date();

      isLoggedIn = tokenDateTime > currentDate;
    }

    setIsLogged(isLoggedIn);
  }, []);

  return (
    <Router>
      <Header isLogged={isLogged} onLogin={handleLogin} onLogout={handleLogout} />
      <Routes>
        {isLogged ? (
          <Route path="/" element={ isLogged ? (<HomePage />) : (<div/>)} />
        ) : (
          <Route path="/login" element={<Login onLogin={handleLogin} />} />
        )}
      </Routes>
    </Router>
  );
}

export default App;