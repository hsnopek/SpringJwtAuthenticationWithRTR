import axios from 'axios';
import {API_URL, ERROR_CODE_ACCESS_TOKEN_INVALID} from "../Constants/Constants";

const axiosInstance = axios.create({
    baseURL: API_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    rejectUnauthorized: true,
    withCredentials: true
})

axiosInstance.interceptors.request.use(
    config => {
        let accessToken = localStorage.getItem("accessToken");
        if (accessToken) {
            config.headers.Authorization = "Bearer " + accessToken;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    config => {
        return Promise.resolve(config);
    },
    (error) => {
        if (error?.response?.status === 401 && (error?.response?.errorCode === ERROR_CODE_ACCESS_TOKEN_INVALID)) {
            const originalRequest = error.config
            originalRequest._retry = true;
            return axiosInstance.post('/auth/refresh-token')
                .then(res => {
                    if (res.status === 200) {
                        localStorage.setItem("accessToken", res.data.accessToken);
                        axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem("accessToken");
                        return axiosInstance(originalRequest);
                    }
                })
        }
        return Promise.reject(error);
    }
);
export default axiosInstance;
