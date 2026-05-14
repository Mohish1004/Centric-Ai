import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import { Sun, Moon, Bell, Search } from 'lucide-react';
import './Topbar.css';

export default function Topbar({ theme, toggleTheme }) {
  const location = useLocation();
  const [showNotifications, setShowNotifications] = useState(false);

  const getPageTitle = () => {
    switch (location.pathname) {
      case '/dashboard': return 'Command Central Dashboard';
      case '/expenses': return 'Expense Ledger & Smart OCR';
      case '/budget': return 'Monthly Budget & Limits';
      case '/reports': return 'Financial Analytics & Charts';
      case '/insights': return 'AI Advisory & Forecasts';
      case '/profile': return 'Investor Profile Settings';
      default: return 'Finance Intelligence';
    }
  };

  const notifications = [
    { id: 1, text: 'Budget exceeded alert: Shopping crossed 80% of limit', time: '10m ago', type: 'warning' },
    { id: 2, text: 'Auto-saved sample transaction list via demo module', time: '1h ago', type: 'info' },
    { id: 3, text: 'AI detected unusual transport expense cluster', time: '5h ago', type: 'alert' }
  ];

  return (
    <header className="app-topbar glass-panel">
      <div className="topbar-title">
        <h1>{getPageTitle()}</h1>
        <p className="topbar-subtitle">Welcome back, check your portfolio dynamics below.</p>
      </div>

      <div className="topbar-actions">
        <div className="search-bar">
          <Search size={18} className="search-icon" />
          <input type="text" placeholder="Search transactions, tags..." className="search-input" />
        </div>

        <button 
          onClick={toggleTheme} 
          className="btn-action theme-toggle" 
          title="Switch theme mode"
        >
          {theme === 'light' ? <Moon size={20} /> : <Sun size={20} />}
        </button>

        <div className="notification-wrapper">
          <button 
            onClick={() => setShowNotifications(!showNotifications)} 
            className={`btn-action notification-btn ${showNotifications ? 'active' : ''}`}
          >
            <Bell size={20} />
            <span className="notification-dot"></span>
          </button>

          {showNotifications && (
            <div className="notifications-dropdown glass-panel fade-in">
              <div className="dropdown-header">
                <h4>Smart Notifications</h4>
                <span className="badge">3 New</span>
              </div>
              <div className="dropdown-list">
                {notifications.map(n => (
                  <div key={n.id} className={`notification-item ${n.type}`}>
                    <p>{n.text}</p>
                    <span>{n.time}</span>
                  </div>
                ))}
              </div>
            </div>
          )}
        </div>
      </div>
    </header>
  );
}
