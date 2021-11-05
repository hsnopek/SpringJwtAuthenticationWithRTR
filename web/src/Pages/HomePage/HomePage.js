import React from 'react';
import './HomePage.css'
import {useTranslation} from "react-i18next";

function HomePage(props) {

    const { t, i18n } = useTranslation("translation");    return (
        <div className={"home-page"}>{t("page.homePage.title", { username: "Hrvoje" })}</div>
    );
}

export default HomePage;