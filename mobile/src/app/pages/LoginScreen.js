import { View, StyleSheet, TextInput, TouchableOpacity, ScrollView } from 'react-native'
import React, { useState } from 'react'
import { LinearGradient } from 'expo-linear-gradient';
import Icon from 'react-native-vector-icons/FontAwesome';
import { useAuth } from '../context/AuthContext';

export default function LoginScreen({ navigation }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const { onLogin } = useAuth();

    const login = async () => {
        const result = await onLogin(email, password);
        if (result && result.error) {
            alert(result.msg);
        }
    }

    return (
        <View style={styles.login}>
            <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
                <ScrollView>
                    <View style={styles.form}>
                        <TextInput style={styles.input} placeholder='Email'
                            onChangeText={(text) => setEmail(text)} value={email} />

                        <TextInput style={styles.input} placeholder='Senha' secureTextEntry={true}
                            onChangeText={(password) => setPassword(password)} value={password} />

                        <TouchableOpacity onPress={login}>
                            <LinearGradient colors={['#C6D4FF', '#00689C']} style={styles.button}>
                                <Icon name="chevron-right" size={20} color="#000" />
                            </LinearGradient>
                        </TouchableOpacity>
                    </View>
                </ScrollView>
            </LinearGradient>
        </View>
    )
}

const styles = StyleSheet.create({
    login: {
        flex: 1,
    },
    gradient: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'flex-start',
    },
    form: {
        marginTop: 120,
        alignItems: 'center',
    },
    text: {
        color: '#FFF',
        fontSize: 18,
    },
    input: {
        padding: 8,
        margin: 10,
        borderRadius: 10,
        borderColor: '#000',
        borderStyle: 'solid',
        borderWidth: 0.5,
        backgroundColor: '#E4E4E4',
        minWidth: '80%',
        elevation: 5
    },
    button: {
        margin: 10,
        borderRadius: 10,
        borderColor: '#000',
        borderStyle: 'solid',
        borderWidth: 0.5,
        paddingVertical: 7,
        paddingHorizontal: 25
    }
})
