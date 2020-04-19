import React, {useEffect} from 'react';
import logo from '../../logo.svg';
import './App.css';
import LoginForm from "../../components/LoginForm/LoginForm";
import { useDispatch } from "react-redux";
import {useCookies} from "react-cookie";
import crypto from 'crypto';

const App: React.FC = () => {
    const [cookies, setCookie] = useCookies();
    const dispatch = useDispatch();

    useEffect(() => {
        if(!cookies.sessionId) {
            const sessionId = crypto.randomBytes(16).toString('base64');
            setCookie('sessionId', sessionId);
            dispatch({ type: 'session/setSessionId', payload: sessionId });
        }
        else {
            dispatch({ type: 'session/setSessionId', payload: cookies.sessionId });
        }



    }, [cookies.sessionId, dispatch, setCookie]);

    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo" />
                <LoginForm/>
            </header>
        </div>
    );
};

export default App;
