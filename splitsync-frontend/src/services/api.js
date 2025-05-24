import axios from "axios";

const API = axios.create({
  baseURL: process.env.REACT_APP_API_BASE_UR,
});

export const createGroup = (data) => API.post("/groups", data);
export const joinGroup = (data) => API.post("/users/join", data);
export const getGroup = (id) => API.get(`/groups/${id}`);
export const addExpense = (groupId, expense) => API.post(`/expenses/add?groupId=${groupId}`, expense);
export const getSettlements = (id) => API.get(`/groups/${id}/settlements`);

export default API;
