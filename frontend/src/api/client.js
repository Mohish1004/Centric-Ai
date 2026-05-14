import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api';

export const apiClient = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor to add auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor to handle invalid tokens
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('jwt_token');
      localStorage.removeItem('user_info');
      // optional redirect logic handled at router boundary
    }
    return Promise.reject(error);
  }
);

// API Abstractions
export const authApi = {
  login: (data) => apiClient.post('/auth/login', data),
  register: (data) => apiClient.post('/auth/register', data),
};

export const expenseApi = {
  getAll: () => apiClient.get('/expenses'),
  create: (data) => apiClient.post('/expenses', data),
  update: (id, data) => apiClient.put(`/expenses/${id}`, data),
  delete: (id) => apiClient.delete(`/expenses/${id}`),
};

export const incomeApi = {
  getAll: () => apiClient.get('/income'),
  create: (data) => apiClient.post('/income', data),
  delete: (id) => apiClient.delete(`/income/${id}`),
};

export const budgetApi = {
  getAll: () => apiClient.get('/budgets'),
  getForMonth: (month) => apiClient.get(`/budgets/${month}`),
  setBudget: (data) => apiClient.post('/budgets', data),
};

export const analyticsApi = {
  getMonthly: (months = 6) => apiClient.get(`/analytics/monthly?months=${months}`),
  getCategory: (month = '') => apiClient.get(`/analytics/category${month ? `?month=${month}` : ''}`),
  getSavings: (month = '') => apiClient.get(`/analytics/savings${month ? `?month=${month}` : ''}`),
};

export const aiApi = {
  getInsights: () => apiClient.get('/ai/insights'),
  getPredictions: () => apiClient.get('/ai/predict'),
  scanOcr: (base64Image, fileName) => apiClient.post('/ai/ocr', { image: base64Image, fileName }),
};
