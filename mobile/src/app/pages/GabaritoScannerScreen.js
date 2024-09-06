import { Button, StyleSheet, Text, View, TouchableOpacity } from 'react-native'
import { LinearGradient } from 'expo-linear-gradient';
import React, {useState} from 'react';
import { CameraView, CameraType, useCameraPermissions } from 'expo-camera';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';

export default function GabaritoScannerScreen({navigation}) {
  return (
    <View style={styles.gabarito}>
      <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
        <CameraScanner />
        <Button title='Resultado' onPress={() => navigation.navigate('Resultado')}></Button>
      </LinearGradient>
    </View>
  )
}

function CameraScanner() {
    const [permission, requestPermission] = useCameraPermissions();
    const [facing, setFacing] = useState('back');
  
    if (!permission) {
      // Camera permissions are still loading.
      return <View />;
    }
  
    function toggleCameraFacing() {
      setFacing(current => (current === 'back' ? 'front' : 'back'));
    }
  
    return (
      <CameraView facing={facing} style={{ width: '80%', height: '80%', borderRadius: 5 }}>
        <View style={styles.buttonContainer}>
          <TouchableOpacity style={styles.buttonCamera} onPress={toggleCameraFacing}>
            <MaterialIcons name="flip-camera-android" size={40} color="#FFF" />
            <Text style={styles.textCamera}>Flip</Text>
          </TouchableOpacity>
        </View>
      </CameraView>
    );
  }

const styles = StyleSheet.create({
    gabarito: {
        flex: 1,
        justifyContent: 'center'
    },
    gradient: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    buttonContainer: {
        flex: 1,
        flexDirection: 'row',
        backgroundColor: 'transparent',
        margin: 64,
      },
      buttonCamera: {
        flex: 1,
        alignSelf: 'flex-end',
        alignItems: 'center',
      },
      textCamera: {
        fontSize: 18,
        fontWeight: 'bold',
        color: 'white',
      },
})