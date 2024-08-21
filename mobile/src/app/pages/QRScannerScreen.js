import { View, Text, StyleSheet, Button } from 'react-native';
import React from 'react';
import { LinearGradient } from 'expo-linear-gradient';

export default function QRScannerScreen({ navigation }) {
  return (
    <View style={styles.qrScanner}>
      <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
        <Text style={styles.text}>QRScannerScreen</Text>
        <Button title='Resultado' onPress={() => navigation.navigate('Resultado')}></Button>
      </LinearGradient>
    </View>
  )
}

const styles = StyleSheet.create({
  qrScanner: {
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