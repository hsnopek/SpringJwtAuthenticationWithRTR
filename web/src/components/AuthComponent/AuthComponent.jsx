import React, {useState} from 'react';
import LoginComponent from "../LoginComponent/LoginComponent";
import RegistrationComponent from "../RegistrationComponent/RegistrationComponent";
import './AuthComponent.css'

const AuthComponent = (props) => {

    const [activeComponent, setActiveComponent] = useState('loginComponent');

    const handleComponentChange = function (component) {
        setActiveComponent(component);
    }

    const isMenuItemSelected = function (item) {
        return activeComponent === item ? ' selected' : '';
    }

    return (
        <div className="auth-component-flex-container">
            <div className="auth-component-flex-item">
                <div className="auth-menu">
                    <div
                        className={"auth-menu-item" + isMenuItemSelected('loginComponent')}
                        onClick={() => handleComponentChange('loginComponent')}
                    >
                        Login
                    </div>
                    <div
                        className={"auth-menu-item" + isMenuItemSelected('registrationComponent')}
                        onClick={() => handleComponentChange('registrationComponent')}
                    >
                        Register
                    </div>
                </div>
            </div>
            <div className="auth-component-flex-item">
                <div className="auth-content">
                    {activeComponent === 'loginComponent' ?
                        <LoginComponent>
                        </LoginComponent>
                        :
                        <RegistrationComponent
                            onRegistrationDone={(component) => handleComponentChange(component)}
                        >
                        </RegistrationComponent>
                    }
                </div>
            </div>
        </div>
    );
}

export default AuthComponent;