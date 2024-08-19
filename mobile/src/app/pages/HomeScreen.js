import { View, Text, StyleSheet, Button } from 'react-native'
import React from 'react'

export default function HomeScreen({ navigation }) {
    return (
        <View style={styles.home}>
            <Text>HomeScreen</Text>
            <Button title='Login' onPress={() => navigation.navigate('Login')}></Button>
        </View>
    )
}

const styles = StyleSheet.create({
    home:{
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center'
    }
})