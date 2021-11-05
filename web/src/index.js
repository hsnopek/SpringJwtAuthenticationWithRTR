import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter} from 'react-router-dom';
import App from './App';
import reportWebVitals from './reportWebVitals';
import './index.css';

import '../src/i18n/i18n';

ReactDOM.render(
        <BrowserRouter>
            <Suspense fallback={<div>Loading...</div>}>
                <App/>
            </Suspense>
        </BrowserRouter>,
document.getElementById('root')
)

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
