import axios from "../../api/api";

const authService = {

    async login(username, password, deviceId) {
        return await axios.post("/auth/login", {
            username: username,
            password: password,
            userDeviceId: deviceId ? deviceId : 'WebBrowser'
        }).then(res => {
            localStorage.setItem("accessToken", res.data.accessToken)
        });
    },
    async register(userDTO) {
        return await axios.post("/user/register", userDTO)
            .then(res => {
                return res.data;
            })
    },
    async logout() {
        return await axios.post("/auth/revoke-token", {token: null})
            .then(res => {
                if (res.data.revoked)
                    Promise.resolve();
                else
                    Promise.reject("Token not revoked!")
            })
    }

};

export default authService;
