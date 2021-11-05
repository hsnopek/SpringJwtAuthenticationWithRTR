import axios from "../../Api/Api";

const TotpService = {

    async generateQrCodeFromUrl(url) {
        return await axios.get(`/totp/get-qr-code?url=${url}`, {responseType: 'arraybuffer'})
            .then(res => {
                let blob = new Blob(
                    [res.data],
                    {type: res.headers['content-type']}
                )
                let image = URL.createObjectURL(blob);
                return image;
            })
    }, async verifyTotpCode(code, deviceId) {
        return await axios.post(`/totp/verify-otp?code=${code}&deviceId=${deviceId ? deviceId : 'WebBrowser'}`)
            .then(res => {
                localStorage.setItem("accessToken", res.data);
            })
    }
};

export default TotpService;
