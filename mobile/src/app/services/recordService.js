import axios from 'axios';
import { Alert } from 'react-native';
import { API_URL } from '@env';

export class RecordService {
    getRecordData = async (qr_data) => {

        qr_data = Number(qr_data.qr_data)

        if (typeof qr_data != 'number' || isNaN(qr_data)) {
            Alert.alert("QR Code inválido", "Por favor. Tente novamente.")
            return;
        }

        try {
            const response = await axios.get(`${API_URL}/record-assessments/public/${qr_data}`, {
                headers: {
                    'Content-Type': 'application/json',
                },
                timeout: 3000,
            });
            return response.data;
        } catch (error) {
            if (error.response) {
                if (error.response.status === 404) {
                    Alert.alert("Registro de avaliação não encontrado", "Por favor, tente novamente.");
                }
            } else {
                Alert.alert('Erro', 'Erro interno do servidor. Por favor, tente novamente.');
            }
        }
    }

    updateScore = async (data) => {
        if (!data.id || !data.studentAnswerKey) {
            return;
        }

        const lettersOnly = data.studentAnswerKey.map(item => item[1]);

        body = { studentAnswerKey: lettersOnly, studentName: data.studentName }

        try {
            const response = await axios.patch(`${API_URL}/record-assessments/${data.id}`, body, {
                headers: {
                    'Content-Type': 'application/json',
                },
                timeout: 3000,
            });
            return response.data;
        } catch (error) {
            if (error.response.status === 404) {
                Alert.alert("Registro de avaliação não encontrado", "Por favor, tente novamente.");
            }
            else if (error.response.status === 400) {
                Alert.alert("Erro no corpo da requisição", "Por favor, tente novamente.");
            }
            else {
                Alert.alert('Erro', 'Erro interno do servidor. Por favor, tente novamente.');
            }
            return null;
        }
    }

    getResult = async (id) => {

        try {
            const response = await axios.get(`${API_URL}/record-assessments/result/${id}`, {
                headers: {
                    'Content-Type': 'application/json',
                },
                timeout: 3000,
            });
            return response.data;
        } catch (error) {
            if (error.response) {
                if (error.response.status === 404) {
                    Alert.alert("Registro de avaliação não encontrado", "Por favor, tente novamente.");
                }
                else if (error.response.status === 400) {
                    Alert.alert("Erro no corpo da requisição", "Por favor, tente novamente.");
                }
            }
            else {
                console.log(error)
                Alert.alert('Erro', 'Erro interno do servidor. Por favor, tente novamente.');
            }
            return null;
        }
    }
}