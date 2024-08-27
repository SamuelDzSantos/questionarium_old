import { View, Text, StyleSheet, Button, Linking } from 'react-native';
import React from 'react';
import { LinearGradient } from 'expo-linear-gradient';

export default function ResultadoScreen({ navigation }) {
    return (
        <View style={styles.resultado}>
            <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
                <Text style={styles.text}>ResultadoScreen</Text>
                <Button title='Back' onPress={navigation.goBack}></Button>
                <Text onPress={() => Linking.openURL('https://questionarium.com.br')}
                    style={[styles.text, { position: 'absolute', bottom: 1 }]}>
                    questionarium.com.br
                </Text>
            </LinearGradient>
        </View>
    )
}

const styles = StyleSheet.create({
    resultado: {
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
