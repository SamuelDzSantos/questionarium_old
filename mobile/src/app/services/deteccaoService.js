import React from 'react';
import { Alert } from "react-native";
import axios from 'axios';
import { API_URL } from '@env';
import * as FileSystem from 'expo-file-system';
import mime from 'mime';

export class DeteccaoService {
    uploadImage = async (uri, num_rows) => {

        const base64String = await FileSystem.readAsStringAsync(uri, { encoding: FileSystem.EncodingType.Base64 });

        const fileExtension = uri.split('.').pop();
        const mimeType = mime.getType(fileExtension) || 'image/jpeg';

        const data = {
            image: `data:${mimeType};base64,${base64String}`,
        };

        try {
            const response = await axios.post(`${API_URL}/deteccao/process`, data, {
                headers: {
                    'Content-Type': 'application/json',
                },
                params: {
                    num_rows: num_rows
                },
                timeout: 50000,
            });
            return response.data;
        } catch (error) {
            if (error.code == '400') {
                Alert.alert('Erro', 'Verifique se a imagem foi carregada e tente novamente.');
            } else {
                Alert.alert('Erro', 'Erro interno do servidor. Por favor, tente novamente.');
            }
            console.log("error", error)
        }
    };
}