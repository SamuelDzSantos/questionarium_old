import React, { useState } from 'react';
import { View, Text, Image, TouchableOpacity, StyleSheet, Linking, Platform } from 'react-native';
import MaterialIcons from 'react-native-vector-icons/MaterialIcons';
import { LinearGradient } from 'expo-linear-gradient';
import * as ImagePicker from 'expo-image-picker';
import { questionariumUrl } from '../../assets/QuestionariumUrl';
import { gradientColors } from '../../assets/GradientColors';

const GabaritoScannerScreen = ({ navigation }) => {
  const [image, setImage] = useState(null);

  const pickImage = async () => {
    const result = await ImagePicker.launchImageLibraryAsync({
      mediaTypes: ImagePicker.MediaTypeOptions.Images,
      allowsEditing: true,
      quality: 1,
    });

    if (!result.canceled) {
      setImage(result.assets[0].uri);
    }
  };

  const takePhoto = async () => {
    const result = await ImagePicker.launchCameraAsync({
      allowsEditing: true,
      quality: 1,
    });

    if (!result.canceled) {
      setImage(result.assets[0].uri);
    }
  };

  return (
    <View style={styles.container}>
      <LinearGradient colors={gradientColors} style={styles.gradient}>
        <View style={{ flexDirection: 'row' }}>
          <TouchableOpacity style={styles.button} onPress={takePhoto}>
            <MaterialIcons name="camera" size={40} color="#FFF" />
            <Text style={styles.buttonText}>Camera</Text>
          </TouchableOpacity>
          <TouchableOpacity style={styles.button} onPress={pickImage}>
            <MaterialIcons name="image-search" size={40} color="#FFF" />
            <Text style={styles.buttonText}>Gallery</Text>
          </TouchableOpacity>
        </View>

        {image && (
          <>
            <Image source={{ uri: image }} style={styles.image} resizeMode='contain' />
            <View style={{ flexDirection: 'row' }}>
              <TouchableOpacity style={styles.button} onPress={() => setImage(null)}>
                <MaterialIcons name="clear" size={40} color="#FFF" />
                <Text style={styles.buttonText}>Cancel</Text>
              </TouchableOpacity>
              <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('Resultado', { imageUri: image })}>
                <MaterialIcons name="check" size={40} color="#FFF" />
                <Text style={styles.buttonText}>Confirm</Text>
              </TouchableOpacity>
            </View>
          </>
        )}

        <Text onPress={() => Linking.openURL(questionariumUrl)} style={styles.link}>
          questionarium.com.br
        </Text>
      </LinearGradient>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  gradient: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  image: {
    width: '80%',
    height: '40%',
    marginTop: 20,
    borderRadius: 5,
  },
  button: {
    margin: 20,
    alignItems: 'center',
  },
  buttonText: {
    fontSize: 18,
    fontWeight: 'bold',
    color: 'white',
  },
  link: {
    color: '#FFF',
    fontSize: 18,
    marginTop: 20,
    marginBottom: 20,
    position: 'absolute',
    bottom: 1
  },
});

export default GabaritoScannerScreen;