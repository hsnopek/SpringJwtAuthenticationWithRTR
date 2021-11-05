import React, {useState} from 'react';
import {useHistory} from "react-router-dom";
import authService from "../../Service/Auth/AuthService";
import totpService from "../../Service/Totp/TotpService";
import './RegistrationComponent.css'

const RegistrationComponent = (props) => {

    // setup

    let history = useHistory();

    const formDataInitialState = {
        username: '',
        password: '',
        email: '',
        firstName: '',
        lastName: '',
        totp: false
    };

    const registrationResponseIntialState = {
        status: '',
        username: '',
        secret: '',
        qrCodeUrl: ''
    };

    const [qrCode, setQrCode] = useState(null);
    const [formData, setFormData] = useState(formDataInitialState);
    const [registrationResponse, setRegistrationResponse] = useState(registrationResponseIntialState);
    const [step, setStep] = useState(1);
    const [lastStep, setLastStep] = useState(false);

    // handlers

    const handleInputChange = function ({target: {name, value, type, checked}}) {

        if (type === 'checkbox') {
            setFormData({...formData, [name]: checked});
        } else {
            setFormData({...formData, [name]: value});
        }
    }

    const handleStepButtonClick = () => {
        if (step === 1) {
            authService.register(formData)
                .then(response => {
                    setRegistrationResponse(
                        {
                            ...registrationResponse,
                            status: response.status,
                            username: response.username,
                            secret: response.secret,
                            qrCodeUrl: response.qrCodeUrl
                        });
                })
            if (!formData.totp) {
                setLastStep(true);
            }
            setStep(step + 1);
        } else if (step === 2) {
            if (registrationResponse.status === 'OK' && !lastStep) {
                totpService.generateQrCodeFromUrl(registrationResponse.qrCodeUrl)
                    .then(res => {
                        setQrCode(res);
                        setLastStep(true)
                        setStep(step + 1);
                    })
            }
            if (lastStep) {
                cleanUp();
            }
        } else if (step === 3) {
            cleanUp()
        }
    };

    // helper functions

    const getFormStep1 = function () {
        return (<form className="registration-form" name="registrationForm">
            <div className="registration-form-item">
                <label htmlFor="usernameInput">Username:</label><br/>
                <input
                    name="username"
                    id="usernameInput"
                    onChange={(e) => handleInputChange(e)}
                    value={formData.username}
                />
            </div>
            <div className="registration-form-item">
                <label htmlFor="passwordInput">Password:</label><br/>
                <input
                    name="password"
                    id="passwordInput"
                    type="password"
                    onChange={(e) => handleInputChange(e)}
                    value={formData.password}
                />
            </div>
            <div className="registration-form-item">
                <label htmlFor="emailInput">Email:</label><br/>
                <input
                    name="email"
                    id="emailInput"
                    type="text"
                    onChange={(e) => handleInputChange(e)}
                    value={formData.email}
                />
            </div>
            <div className="registration-form-item">
                <label htmlFor="firstNameInput">First name:</label><br/>
                <input
                    name="firstName"
                    id="firstNameInput"
                    type="text"
                    onChange={(e) => handleInputChange(e)}
                    value={formData.firstName}
                />
            </div>
            <div className="registration-form-item">
                <label htmlFor="lastNameInput">Last name:</label><br/>
                <input
                    name="lastName"
                    id="lastNameInput"
                    type="text"
                    onChange={(e) => handleInputChange(e)}
                    value={formData.lastName}
                />
            </div>
            <div className="registration-form-item">
                <label htmlFor="checkboxInput">2-factor-auth:</label>
                <input
                    name="totp"
                    id="checkboxInput"
                    type="checkbox"
                    onChange={(e) => handleInputChange(e)}
                    value={formData.totp}
                />
            </div>
        </form>)
    };

    const getFormStep2 = function () {
        const messageHeader = "Registration successful!";
        let messageConfirmEmail = "Please confirm your e-mail address to activate your account, then click 'Finish'"

        // user has enabled totp
        if (registrationResponse.secret) {
            messageConfirmEmail = "Please confirm your e-mail address to activate your account, then click 'Next'"
        }

        return (<div>
            <h1>{messageHeader}</h1>
            <p>{messageConfirmEmail}</p>
        </div>)
    };

    const getFormStep3 = function () {
        const message = "Account activated";
        const message2 = "Write down your secret, and confirm."
        return (<div>
            <h1>{message}</h1>
            <p>{message2}</p>
            <p>SECRET: {registrationResponse.secret}</p>
            <img id="image" src={qrCode} alt={"qr code"}></img>
        </div>)
    };

    const cleanUp = function () {
        // clean-up
        setRegistrationResponse(registrationResponseIntialState);
        setFormData(formDataInitialState)
        setStep(1);
        setLastStep(false);

        // open login
        props.onRegistrationDone('loginComponent');
        history.push("/login");
    }

    const getStepButton = function () {
        const button = <button onClick={() => handleStepButtonClick()}>{lastStep ? 'Finish' : 'Next'}</button>;
        return button;
    }

    const getCurrentForm = function () {
        switch (step) {
            case  1:
                const form = getFormStep1();
                return form;
            case 2:
                const formStep2 = getFormStep2();
                return formStep2;
            case 3:
                const formStep3 = getFormStep3();
                return formStep3;
            default:
                break;
        }
    }

    const form = getCurrentForm();
    const button = getStepButton();

    return (
        <div className="registration-component">
            <div className="registration-component-item">
                {form}
            </div>
            <div className="registration-component-item">
                {button}
            </div>
        </div>
    );
}

export default RegistrationComponent;