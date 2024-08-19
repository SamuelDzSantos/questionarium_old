import { View, Text, StyleSheet, Button } from 'react-native'
import React from 'react'

export default function LoginScreen({ navigation }) {
    return (
        <View style={styles.login}>
            <Text>LoginScreen</Text>
            <Button title='Back' onPress={navigation.goBack} ></Button>
        </View>
    )
}

const styles = StyleSheet.create({
    login:{
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center'
    }
})