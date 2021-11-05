import React from 'react';

const AppContext = React.createContext({
    user: null,
    errorMessages: null,
    setUser: () => {
    },
    setErrorMessages: () => {
    }
});

export default AppContext;
