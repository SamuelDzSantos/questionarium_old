import { Button, StyleSheet, Text, View, Image, TouchableOpacity, Linking } from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import { LinearGradient } from 'expo-linear-gradient';
import React, { useState } from 'react';
import * as ImagePicker from 'expo-image-picker';

export default function GabaritoScannerScreen({ navigation }) {
  const [image, setImage] = useState(null);

  const pickImage = async () => {
    let result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: true,
      quality: 1,
      allowsMultipleSelection: false,
    });

    if (!result.canceled) {
      setImage(result.assets[0].uri);
    }
  };

  const takePhoto = async () => {
    let result = await ImagePicker.launchCameraAsync({
      allowsEditing: true,
      aspect: [16, 9],
      quality: 1,
      allowsMultipleSelection: false,
      cameraType: ImagePicker.CameraType.back
    });

    if (!result.canceled) {
      setImage(result.assets[0].uri);
    }
  };

  return (
    <View style={styles.gabarito}>
      <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
        <View style={{ flexDirection: 'row' }}>
          <TouchableOpacity style={styles.buttonCamera} onPress={takePhoto}>
            <MaterialIcons name="camera" size={40} color="#FFF" />
            <Text style={styles.textCamera}>Camera</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.buttonCamera} onPress={pickImage}>
            <MaterialIcons name="image-search" size={40} color="#FFF" />
            <Text style={styles.textCamera}>Gallery</Text>
          </TouchableOpacity>
        </View>

        {image && <>
          <Image source={{ uri: image }} style={styles.image} />
          <View style={{flexDirection: 'row'}}>
            <TouchableOpacity style={styles.buttonCamera} onPress={() => setImage(null)}>
              <MaterialIcons name="clear" size={40} color="#FFF" />
              <Text style={styles.textCamera}>Cancel</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.buttonCamera} onPress={() => navigation.navigate('Resultado')}>
              <MaterialIcons name="check" size={40} color="#FFF" />
              <Text style={styles.textCamera}>Confirm</Text>
            </TouchableOpacity>
          </View>
        </>
        }
        <Text onPress={() => Linking.openURL('https://questionarium.com.br')}
          style={[styles.text, { position: 'absolute', bottom: 1 }]}>
          questionarium.com.br
        </Text>
      </LinearGradient>
    </View>
  );
}

const styles = StyleSheet.create({
  gabarito: {
    flex: 1,
    justifyContent: 'center',
  },
  gradient: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  image: {
    width: '80%',
    height: '60%',
    marginTop: 20,
    borderRadius: 5
  },
  buttonCamera: {
    margin: 20,
    alignItems: 'center',
  },
  textCamera: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'white',
  },
  text: {
    color: '#FFF',
    fontSize: 18,
    marginBottom: 20,
  },
});
