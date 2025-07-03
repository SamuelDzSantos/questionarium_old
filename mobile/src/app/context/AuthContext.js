import React, { createContext, useContext, useState, useEffect } from "react";
import axios from 'axios';
import * as SecureStore from 'expo-secure-store';
import { API_URL, TOKEN_KEY } from '@env';
import { Alert } from "react-native";

const AuthContext = createContext({});

export const useAuth = () => {
    return useContext(AuthContext);
}

export const AuthProvider = ({ children }) => {

    const [authState, setAuthState] = useState({
        token: null,
        authenticated: null
    });

    useEffect(() => {
        const loadToken = async () => {
            const token = await SecureStore.getItemAsync(TOKEN_KEY);

            try {
                const result = await axios.get(`${API_URL}/auth`, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                });
            } catch (error) {
                await logout();
                return;
            }

            if (token) {
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
            const result = await axios.post(`${API_URL}/auth/login`, { login: email, password: password }, {
                headers: {
                        Authorization: null
                    }
            }
            );
            setAuthState({
                token: result.data.token,
                authenticated: true
            });

            axios.defaults.headers.common['Authorization'] = `Bearer ${result.data.token}`;
            await SecureStore.setItemAsync(TOKEN_KEY, result.data.token);

        } catch (error) {
            if (error.response) {
                if (error.response.status === 401) {
                    Alert.alert("Login inválido", "Email ou senha incorretos.");
                } else {
                    Alert.alert("Erro", `Erro no servidor: ${error.response.status}`);
                }
            } else if (error.request) {
                Alert.alert("Erro de conexão", "Não foi possível se conectar ao servidor.");
            } else {
                Alert.alert("Erro", "Erro ao configurar a requisição.");
            }
            return;
        }

        return;
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
