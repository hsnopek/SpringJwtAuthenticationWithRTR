import i18n from "i18next";
import Backend from "i18next-http-backend";
import LanguageDetector from "i18next-browser-languagedetector";
import {initReactI18next} from "react-i18next";
import message_hr from "../Translations/hr/message.json";
import message_en from "../Translations/en/message.json";

i18n.use(Backend).use(LanguageDetector).use(initReactI18next).init({
    fallbackLng: 'hr',
    debug: true,
    detection: {
        order : ['querystring', 'cookie'],
        cache: ['cookie']
    },
    interpolation: { escapeValue: false },
    resources: {
        hr: {
            translation: message_hr
        },
        en: {
            translation: message_en
        },
    },
});

export default i18n;