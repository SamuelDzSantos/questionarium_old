import { View, Text, StyleSheet, Button } from 'react-native'
import React from 'react'
import { LinearGradient } from 'expo-linear-gradient';

export default function ResultadoScreen({ navigation }) {
    return (
        <View style={styles.login}>
            <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
                <Text style={styles.text}>ResultadoScreen</Text>
                <Button title='Back' onPress={navigation.goBack}></Button>
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
        justifyContent: 'center',
    },
    text: {
        color: '#FFF',
        fontSize: 18,
        marginBottom: 20,
    },
})
