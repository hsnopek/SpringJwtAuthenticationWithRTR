import axios from "../../api/api";

const userService = {

    async findUser() {
        return await axios.get(`/user/principal`)
            .then(res => {
                return res.data;
            })
    }
};

export default userService;
