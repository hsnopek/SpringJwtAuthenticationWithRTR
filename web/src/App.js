import './App.css';
import React, {useState} from "react";
import {Link} from "react-router-dom";
import authService from "./Service/Auth/AuthService";
import AppContext from "./Context/AppContext";
import Router from "./Router/Router";


const App = () => {

    const [user, setUser] = useState(null);
    const [errorMessages, setErrorMessages] = useState({});

    const handleLogoutClicked = function () {
        authService.logout()
            .then(res => {
                localStorage.removeItem("accessToken");
                setUser(null);
            })
    }

    const getMenu = function () {

        const getNavLink = function (text, link, callback) {
            if (callback)
                return (<li><Link to={link} onClick={callback}>{text}</Link></li>);
            else
                return (<li><Link to={link}>{text}</Link></li>);
        }

        const navHome = getNavLink("Home", "/", null);
        const navLogout = getNavLink("Logout", "#", handleLogoutClicked);
        const navLoginRegister = getNavLink("Login/Register", "/login", null);
        const navUser = getNavLink("User", "/user", null);

        return (<div className="menu">
            <ul>
                {navHome}
                {user ? navLogout : navLoginRegister}
                {user ? navUser : null}
            </ul>
        </div>);
    }

    const getHeader = function () {
        return (
            <header className="App-header">
                <h1 className="App-title">SpringBootJwtAuthenticationWithRTR</h1>
            </header>
        );
    }
    const header = getHeader();
    const menu = getMenu();

    return (
        <AppContext.Provider value={{user: user, errorMessages: errorMessages, setUser, setErrorMessages}}>
            <div className="App">
                {header}
                <Router> {menu} </Router>
            </div>
        </AppContext.Provider>
    );
};

export default App;
