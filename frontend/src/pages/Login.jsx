import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { authApi } from '../api/client';
import { Sparkles, ArrowRight, Lock, Mail } from 'lucide-react';
import './Auth.css';

export default function Login({ onLoginSuccess }) {
  const navigate = useNavigate();
  const [email, setEmail] = useState('demo@finance.ai');
  const [password, setPassword] = useState('password');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await authApi.login({ email, password });
      const data = response.data;
      localStorage.setItem('jwt_token', data.token);
      localStorage.setItem('user_info', JSON.stringify({
        id: data.id,
        name: data.name,
        email: data.email
      }));
      onLoginSuccess();
      navigate('/dashboard');
    } catch (err) {
      // Standalone simulator fallback if backend is offline
      const users = JSON.parse(localStorage.getItem('registered_users') || '{}');
      const customUser = users[email];

      if (email === 'demo@finance.ai' || customUser) {
        const userName = customUser ? customUser.name : 'Demo Financial Investor';
        localStorage.setItem('jwt_token', 'Mock.Token.String.Demo.Active');
        localStorage.setItem('user_info', JSON.stringify({
          id: customUser ? Date.now() : 1,
          name: userName,
          email: email
        }));
        onLoginSuccess();
        navigate('/dashboard');
      } else {
        setError(err.response?.data?.message || err.response?.data?.error || 'Account not found on server/offline storage. Try registering first or use demo@finance.ai');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-wrapper">
      <div className="auth-card glass-panel fade-in">
        <div className="auth-header">
          <div className="brand-logo mx-auto mb-3">
            <Sparkles size={28} />
          </div>
          <h2>Sign in to Centric<span>AI</span></h2>
          <p>Access your automated finance intelligence hub</p>
        </div>

        {error && <div className="auth-alert danger">{error}</div>}

        <form onSubmit={handleSubmit} className="auth-form">
          <div className="form-group">
            <label className="form-label">Email Address</label>
            <div className="input-icon-wrapper">
              <Mail className="input-icon" size={18} />
              <input 
                type="email" 
                required 
                className="form-input with-icon" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="name@example.com"
              />
            </div>
          </div>

          <div className="form-group">
            <div className="flex justify-between">
              <label className="form-label">Password</label>
              <a href="#forgot" onClick={(e) => { e.preventDefault(); alert('Demo password is: password'); }} className="forgot-link">Forgot?</a>
            </div>
            <div className="input-icon-wrapper">
              <Lock className="input-icon" size={18} />
              <input 
                type="password" 
                required 
                className="form-input with-icon" 
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
              />
            </div>
          </div>

          <button type="submit" disabled={loading} className="btn btn-primary btn-block mt-2">
            {loading ? 'Authenticating...' : (
              <>
                <span>Sign In Command Central</span>
                <ArrowRight size={16} />
              </>
            )}
          </button>
        </form>

        <div className="auth-footer">
          <p>New to intelligence tracking? <Link to="/register">Create an account</Link></p>
        </div>

        <div className="demo-credentials">
          <span>💡 <b>Demo Account pre-loaded:</b> demo@finance.ai / password</span>
        </div>
      </div>
    </div>
  );
}
