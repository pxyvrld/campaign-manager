import axios from 'axios';

const BASE_URL = 'http://localhost:8080/api/campaigns';

const api = axios.create({
    baseURL: BASE_URL,
});

export const getAllCampaigns = () => api.get('');
export const getCampaignById = (id) => api.get(`/${id}`);
export const createCampaign = (campaign) => api.post('', campaign);
export const updateCampaign = (id, campaign) => api.put(`/${id}`, campaign);
export const deleteCampaign = (id) => api.delete(`/${id}`);
export const getEmeraldBalance = () => api.get('/account/emerald-balance');
export const getTowns = () => api.get('/towns');
export const getKeywords = () => api.get('/keywords');