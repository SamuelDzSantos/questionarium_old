import { StyleSheet, Text, View } from 'react-native'
import { CameraView, CameraType, useCameraPermissions } from 'expo-camera';
import React from 'react'

export default function Camera() {
  return (
    <View style={styles.camera}>
      <Text>CameraView</Text>
    </View>
  )
}

const styles = StyleSheet.create({
  camera: {
    flex: 1,
    justifyContent: 'center'
  },
})