import { View, StyleSheet, Button, TextInput, KeyboardAvoidingView, TouchableOpacity, ScrollView } from 'react-native'
import React from 'react'
import { LinearGradient } from 'expo-linear-gradient';
import Icon from 'react-native-vector-icons/FontAwesome';

export default function HomeScreen({ navigation }) {
    return (
        <View style={styles.home}>
            <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
                <ScrollView>
                    <View style={styles.form}>
                        <TextInput style={styles.input} placeholder='Email'></TextInput>
                        <TextInput style={styles.input} placeholder='Senha'></TextInput>
                        <TouchableOpacity onPress={() => navigation.navigate('Resultado')}>
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
    home: {
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
