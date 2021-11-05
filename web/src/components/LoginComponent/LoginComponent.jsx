import React, {useContext, useState} from 'react';
import AppContext from "../../context/AppContext";
import authService from "../../services/auth/authService";
import totpService from "../../services/totp/totpService";
import userService from "../../services/user/userService";
import {useHistory} from "react-router-dom";

import './LoginComponent.css'

const LoginComponent = (props) => {

    // setup

    let history = useHistory();

    const {setUser, errorMessages, setErrorMessages} = useContext(AppContext);

    const [formData, setFormData] = useState({username: '', password: '', otpCode: ''});
    const [step, setStep] = useState(1);

    // handlers

    const handleInputChange = function ({target: {name, value}}) {
        setFormData({...formData, [name]: value});
    }

    const handleLogin = function () {
        if (step === 1) {
            // login to get new access token and refresh token
            loginUser();
        } else {
            // verify otp code to get valid accessToken
            totpService.verifyTotpCode(formData.otpCode, null)
                .then(res => {
                    loginUser();
                })
        }
    }

    // helper functions

    const setErrorMessage = function (error) {
        setErrorMessages({
            ...errorMessages,
            loginComponentErrorMessage: error.response.data.errorMessage,
            loginComponentErrorCode: error.response.data.errorCode
        })
    }

    const clearErrorMessage = function () {
        setErrorMessages({
            ...errorMessages,
            loginComponentErrorMessage: null,
            loginComponentErrorCode: null
        })
    }

    const loginUser = function () {
        // if valid accessToken aquired, try to login user
        authService.login(formData.username, formData.password, null)
            .then(res => {
                // on successful login, set user in AppContext
                userService.findUser()
                    .then(res => {
                        // set user in AppContext and redirect to homepage
                        setUser(res);
                        history.push('/')
                    }).catch((error) => {
                    // if error happened while getting user, set errorMessage in AppContext
                    setErrorMessage(error);
                });
            }).catch((error) => {
            // if authentication failed, set error message in AppContext
            setErrorMessage(error);
        });
    }

    const getStep1 = function () {
        return (<div>
            <form className="login-form" name="loginForm">
                <div className="login-form-item">
                    <label htmlFor="usernameInput">Username:</label><br/>
                    <input
                        name="username"
                        id="usernameInput"
                        onChange={(e) => handleInputChange(e)}
                        value={formData.username}
                    />
                </div>

                <div className="login-form-item">
                    <label htmlFor="passwordInput">Password:</label><br/>
                    <input
                        name="password"
                        id="passwordInput"
                        type="password"
                        onChange={(e) => handleInputChange(e)}
                        value={formData.password}
                    />
                </div>
            </form>
        </div>)
    };

    const getStep2 = function () {
        return (
            <form className="login-form" name="loginForm">
                <div className="login-form-item">
                    <label htmlFor="otpCodeInput">OTP code:</label><br/>
                    <input
                        name="otpCode"
                        id="otpCodeInput"
                        onChange={(e) => handleInputChange(e)}
                        value={formData.otpCode}
                    />
                </div>
            </form>)
    };

    const getCurrentForm = function () {
        switch (step) {
            case  1:
                const formStep1 = getStep1();

                // two factor verification required, load step 2 form
                if (errorMessages?.loginComponentErrorCode === 10) {
                    setStep(step + 1);
                    clearErrorMessage();
                }

                return formStep1;
            case 2:
                const formStep2 = getStep2();
                return formStep2;
            default:
                break;
        }
    }

    const getStepButtonCaption = function () {
        return step === 1 && errorMessages?.loginComponentErrorCode === 10 ? 'Next' : 'Login';
    }

    const stepButtonCaption = getStepButtonCaption();
    const form = getCurrentForm();
    return (
        <div className="login-component">
            <div className="login-component-item">
                <div>
                    {form}
                </div>
            </div>
            <div className="login-component-item">
                {
                    errorMessages?.loginComponentErrorMessage ?
                        <p className={"errorMsg"}>{errorMessages?.loginComponentErrorMessage}</p>
                        :
                        <div></div>
                }
            </div>
            <div className="login-component-item">
                <button onClick={handleLogin}>{stepButtonCaption}</button>
            </div>

        </div>
    );
};

export default LoginComponent;
