import axios from 'axios';

// Backend adresimiz
const api = axios.create({
    baseURL: 'http://localhost:8080',
});

// Her isteğe Token'ı otomatik ekle (Interceptor)
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

export default api;