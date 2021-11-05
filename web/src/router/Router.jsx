import React from 'react';
import {Route, Switch} from "react-router-dom";
import HomePage from "../pages/HomePage/HomePage";
import LoginPage from "../pages/LoginPage/LoginPage";
import UserPage from "../pages/UserPage/UserPage";
import './Router.css'

const Router = (props) => {
    return (
        <div>
            {props.children}
            <main>
                <Switch>
                    <Route
                        path="/" component={HomePage} exact/>
                    <Route
                        path="/login"
                        render={(props) =>
                            <LoginPage/>}
                    />
                    <Route
                        path="/user"
                        render={(props) =>
                            <UserPage
                            />}
                    />
                </Switch>
            </main>
        </div>
    );
}

export default Router;