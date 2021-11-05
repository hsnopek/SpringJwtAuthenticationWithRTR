import AppContext from "../../Context/AppContext";
import React, {useContext} from 'react';
import './UserPage.css'

function LoginPage(props) {
    const {user} = useContext(AppContext);

    return (
        <div className="user-page">Welcome {user?.username}</div>
    );
}

export default LoginPage;