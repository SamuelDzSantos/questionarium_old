import { View, StyleSheet, TextInput, TouchableOpacity, ScrollView, Text, Linking, KeyboardAvoidingView } from 'react-native'
import React, { useState } from 'react'
import { LinearGradient } from 'expo-linear-gradient';
import Icon from 'react-native-vector-icons/FontAwesome';
import { useAuth } from '../context/AuthContext';

export default function LoginScreen({ navigation }) {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errors, setErrors] = useState({});
    const { onLogin } = useAuth();

    const validateForm = () => {
        let errors = {};

        if(!email) errors.email = "Email é obrigatório";
        if(!password) errors.password = "Senha é obrigatória";

        setErrors(errors);

        return Object.keys(errors).length === 0;
    };
    
    const login = async () => {
        if(!validateForm()) return;

        const result = await onLogin(email, password);
        if (result && result.error) {
            alert(result.msg);
        }
    };

    return (
        <View style={styles.login}>
            <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
                <View style={styles.form}>
                    <ScrollView>
                        {
                            errors.email ? <Text style={styles.errorText}>{errors.email}</Text> : null
                        }
                        <TextInput style={styles.input} placeholder='Email'
                            onChangeText={(text) => setEmail(text)} value={email} />
                        {
                            errors.password ? <Text style={styles.errorText}>{errors.password}</Text> : null
                        }
                        <TextInput style={styles.input} placeholder='Senha' secureTextEntry={true}
                            onChangeText={(password) => setPassword(password)} value={password} />
                        <TouchableOpacity onPress={login} style={styles.buttonContainer}>
                            <LinearGradient colors={['#C6D4FF', '#00689C']} style={styles.button}>
                                <Icon name="chevron-right" size={20} color="#000" />
                            </LinearGradient>
                        </TouchableOpacity>
                    </ScrollView>
                    <Text onPress={() => Linking.openURL('https://questionarium.com.br')}
                        style={[styles.text, {
                            marginBottom: 20,
                            flex: 1,
                        }]} >
                        questionarium.com.br
                    </Text>
                </View>
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
    },
    form: {
        marginTop: 120,
        alignItems: 'center',
    },
    text: {
        color: '#FFF',
        fontSize: 18,
    },
    errorText: {
        color: 'red',
        marginLeft: 10
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
    buttonContainer: {
        width: '25%',
        alignSelf: 'center'
    },
    button: {
        margin: 10,
        borderRadius: 10,
        borderStyle: 'solid',
        borderWidth: 0.5,
        paddingVertical: 7,
        paddingHorizontal: 25
    }
})
