import axios from "../../Api/Api";

const UserService = {

    async findUser() {
        return await axios.get(`/user/principal`)
            .then(res => {
                return res.data;
            })
    }
};

export default UserService;
