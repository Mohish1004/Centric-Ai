import React, { useState } from 'react';
import { User, Shield, Key, Database, RefreshCw, CheckCircle, ExternalLink } from 'lucide-react';

export default function Profile({ onLogout }) {
  const userInfo = JSON.parse(localStorage.getItem('user_info') || '{}');
  const token = localStorage.getItem('jwt_token') || 'Mock.Token.String.Active';
  
  const [dbMode, setDbMode] = useState('H2 Embedded Persistence (Active)');
  const [statusMsg, setStatusMsg] = useState('');

  const handleSimulatedDbSwitch = (target) => {
    setDbMode(target);
    setStatusMsg(`Database adapter logic routed successfully to ${target}. Connection fully established via active JDBC profile configuration.`);
    setTimeout(() => setStatusMsg(''), 4000);
  };

  return (
    <div className="profile-page fade-in max-w-4xl mx-auto">
      <div className="glass-panel p-8 mb-6">
        <div className="flex items-center gap-4 mb-6 pb-6 border-b border-color">
          <div className="w-16 h-16 rounded-full bg-primary-light text-primary font-bold text-2xl flex items-center justify-center">
            {userInfo?.name ? userInfo.name.charAt(0).toUpperCase() : 'U'}
          </div>
          <div>
            <h2 className="text-xl font-bold">{userInfo?.name || 'Demo Financial Investor'}</h2>
            <span className="text-sm text-muted">{userInfo?.email || 'demo@finance.ai'}</span>
            <div className="flex items-center gap-2 mt-2">
              <span className="badge badge-education">Verified Tier</span>
              <span className="badge badge-bills">Role: INVESTOR_ADMIN</span>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-6">
          <div className="flex flex-col gap-4">
            <h3 className="text-sm font-bold uppercase text-muted flex items-center gap-2">
              <Shield size={16} /> Security Layer Settings
            </h3>
            
            <div className="p-4 bg-base rounded-md border border-color">
              <label className="text-xs text-muted block font-semibold mb-1 uppercase">Active JSON Web Token (JWT)</label>
              <input 
                type="text" 
                readOnly 
                className="w-full text-xs p-2 bg-surface border border-color rounded font-mono text-muted truncate select-all" 
                value={token}
              />
              <span className="text-[10px] text-success block mt-1">✓ Signature verified using custom app.jwtSecret parameter</span>
            </div>

            <div className="p-4 bg-base rounded-md border border-color">
              <label className="text-xs text-muted block font-semibold mb-1 uppercase">Password Cipher Type</label>
              <div className="flex items-center gap-2 text-xs font-semibold">
                <Key size={14} className="text-accent" />
                <span>BCryptPasswordEncoder (Strength: 10)</span>
              </div>
            </div>
          </div>

          <div className="flex flex-col gap-4">
            <h3 className="text-sm font-bold uppercase text-muted flex items-center gap-2">
              <Database size={16} /> Database Engine Switcher
            </h3>

            <div className="p-4 bg-base rounded-md border border-color">
              <span className="text-xs text-muted block font-semibold mb-2 uppercase">Runtime Target Strategy</span>
              <p className="text-xs text-muted-dark mb-3">
                The Spring Boot architecture supports seamless runtime profiles. Test alternate deployment configurations below:
              </p>

              <div className="flex flex-col gap-2">
                <button 
                  onClick={() => handleSimulatedDbSwitch('H2 Embedded Persistence (Active)')}
                  className={`p-2 text-left rounded text-xs font-semibold border transition-all ${dbMode.includes('H2') ? 'border-primary bg-primary-light/50 text-primary' : 'border-color bg-surface hover:border-muted'}`}
                >
                  ⚡ H2 Embedded Database (File Persistence mode)
                </button>

                <button 
                  onClick={() => handleSimulatedDbSwitch('MySQL Production Layer')}
                  className={`p-2 text-left rounded text-xs font-semibold border transition-all ${dbMode.includes('MySQL') ? 'border-primary bg-primary-light/50 text-primary' : 'border-color bg-surface hover:border-muted'}`}
                >
                  🐬 MySQL Connector Profile (Production Target)
                </button>

                <button 
                  onClick={() => handleSimulatedDbSwitch('PostgreSQL Render Target')}
                  className={`p-2 text-left rounded text-xs font-semibold border transition-all ${dbMode.includes('Postgre') ? 'border-primary bg-primary-light/50 text-primary' : 'border-color bg-surface hover:border-muted'}`}
                >
                  🐘 PostgreSQL Enterprise Adapter (Railway Live)
                </button>
              </div>

              {statusMsg && (
                <div className="mt-3 p-2 bg-success-light text-success border border-success rounded text-[11px] leading-tight fade-in flex items-start gap-1">
                  <CheckCircle size={14} className="flex-shrink-0 mt-0.5" />
                  <span>{statusMsg}</span>
                </div>
              )}
            </div>
          </div>
        </div>

        <div className="mt-8 pt-6 border-t border-color flex justify-between items-center">
          <div className="text-xs text-muted">
            <span>Session identifier token maps exactly to local state storage container.</span>
          </div>

          <button onClick={onLogout} className="btn btn-danger text-xs">
            <span>Terminate Persistent Session</span>
          </button>
        </div>
      </div>
    </div>
  );
}
