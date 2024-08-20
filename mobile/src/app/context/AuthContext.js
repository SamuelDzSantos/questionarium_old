import React, { createContext, useContext, useState, useEffect } from "react";
import axios from 'axios';
import * as SecureStore from 'expo-secure-store';

const TOKEN_KEY = 'jwt';
//TODO CHANGE URL
export const API_URL = 'http://localhost:8083'
const AuthContext = createContext({});

export const useAuth = () => {
    return useContext(AuthContext);
}

export const AuthProvider = ({children}) => {
    
    const [authState, setAuthState] = useState({
        token: null,
        authenticated: null
    });

    useEffect(() => {
        const loadToken = async () => {
            const token = await SecureStore.getItemAsync(TOKEN_KEY);
            
            if(token) {
                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

                setAuthState({
                    token: token,
                    authenticated: true
                });
            }
        }
        loadToken();
    }, []);

    const login = async (email, password) => {
        try {
            //TODO CHANGE ENDPOINT
            const result = await axios.post(`${API_URL}/login`, { email, password });
            
            setAuthState({
                token: result.data.token,
                authenticated: true
            });
            
            //TODO CHANGE AUTHORIZATION TYPE
            axios.defaults.headers.common['Authorization'] = `Bearer ${result.data.token}`;

            await SecureStore.setItemAsync(TOKEN_KEY, result.data.token);

            return result;

        } catch (e) {
            return { error: true, msg: e.response.data.msg };
        }
    }

    const logout = async () => {
        await SecureStore.deleteItemAsync(TOKEN_KEY);

        axios.defaults.headers.common['Authorization'] = '';

        setAuthState({
            token: null,
            authenticated: false
        });
    };
    
    const value = {
        onLogin: login,
        onLogout: logout,
        authState
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
