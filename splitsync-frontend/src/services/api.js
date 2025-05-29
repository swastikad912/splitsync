import axios from "axios";

const API = axios.create({
  baseURL: "https://splitsync.onrender.com/api",
  timeout: 10000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

// Request interceptor for auth tokens if needed
API.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const createGroup = (data) => API.post("/groups", data);
export const joinGroup = (data) => API.post("/users/join", data);
export const getGroup = (id) => API.get(`/groups/${id}`);
export const addExpense = (groupId, expense) => API.post(`/expenses/add?groupId=${groupId}`, expense);
export const getSettlements = (id) => API.get(`/groups/${id}/settlements`);

export default API;