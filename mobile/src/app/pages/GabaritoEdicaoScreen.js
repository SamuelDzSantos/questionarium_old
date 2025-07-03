import { View, Text, StyleSheet, TextInput, ScrollView, TouchableOpacity, Alert } from 'react-native';
import React, { useState, useEffect, useCallback } from 'react';
import { LinearGradient } from "expo-linear-gradient";
import { gradientColors } from "../../assets/GradientColors";
import { RecordService } from './../services/recordService';
import { DeteccaoService } from "../services/deteccaoService";
import { Picker } from '@react-native-picker/picker';
import { useFocusEffect } from '@react-navigation/native';

export default function GabaritoEdicaoScreen({ route, navigation }) {
    const { imageUri, qr_data } = route.params;

    const [recordData, setRecordData] = useState(null);
    const [numRows, setNumRows] = useState(null);
    const [studentName, setStudentName] = useState(null);
    const [rawGabaritoData, setRawGabaritoData] = useState([]);
    const [gabaritoData, setGabaritoData] = useState([]);

    const recordService = new RecordService();
    const deteccaoService = new DeteccaoService();

    useFocusEffect(
        useCallback(() => {
            setRecordData(null);
            setNumRows(null);
            setStudentName(null);
            setRawGabaritoData([]);
            setGabaritoData([]);
        }, [])
    );

    useEffect(() => {
        if (!qr_data) return;
        const getData = async () => {
            const response = await recordService.getRecordData(qr_data);
            if (!response) {
                navigation.navigate('QRScanner');
                return;
            }

            setRecordData(response);
            setStudentName(response.studentName);
            const rowCount = response.correctAnswerKeyLetter.length;
            setNumRows(rowCount);
        };

        getData();
    }, [qr_data]);

    useEffect(() => {
        if (!(imageUri && recordData && numRows)) return;

        const uploadImage = async () => {
            const response = await deteccaoService.uploadImage(imageUri, numRows);
            setRawGabaritoData(response);
        };

        uploadImage();
    }, [imageUri, recordData, numRows]);

    useEffect(() => {
        if (!recordData || !Array.isArray(rawGabaritoData)) return;

        const totalQuestions = recordData.correctAnswerKeyLetter.length;
        const fullAnswers = Array(totalQuestions).fill("P");

        let objectiveIndex = 0;
        for (let i = 0; i < totalQuestions; i++) {
            if (recordData.correctAnswerKeyLetter[i] !== "Z") {
                if (rawGabaritoData[objectiveIndex]) {
                    const [, answer] = rawGabaritoData[objectiveIndex];
                    fullAnswers[i] = answer;
                }
                objectiveIndex++;
            }
        }

        setGabaritoData(fullAnswers);
    }, [recordData, rawGabaritoData]);

    const handleSubmit = async () => {
        Alert.alert(
            "Confirmar envio",
            "Tem certeza que deseja enviar suas respostas?",
            [
                { text: "Cancelar", style: "cancel" },
                {
                    text: "Confirmar",
                    onPress: async () => {
                        const id = Number(qr_data.qr_data);
                        
                        if (typeof id !== 'number' || isNaN(id)) {
                            Alert.alert("ID de avaliação inválido", "Tente novamente.");
                            return;
                        }

                        const data = {
                            id,
                            studentName: studentName || "",
                            studentAnswerKey: gabaritoData,
                        };

                        const response = await recordService.updateScore(data);
                        if (response)
                            navigation.navigate('Resultado', id);
                        else
                            Alert.alert("Erro ao enviar respostas", "Confira preenchimento do gabarito.");
                    }
                }
            ]
        );
    };

    return (
        <LinearGradient colors={gradientColors} style={styles.gradient}>
            <ScrollView contentContainerStyle={styles.scrollContent}>
                <TextInput
                    style={styles.input}
                    placeholder='Aluno'
                    onChangeText={(text) => setStudentName(text)}
                    value={studentName}
                />

                {recordData?.questionSnapshots?.map((question, index) => {
                    const isDiscursive = recordData.correctAnswerKeyLetter[index] === "Z";
                    const currentAnswer = gabaritoData?.[index] || 'X';

                    return (
                        <View key={index} style={styles.itemStyle}>
                            <View style={styles.row}>
                                <Text style={styles.questionLabel}>Questão {index + 1}</Text>
                                <Picker
                                    selectedValue={currentAnswer}
                                    style={styles.picker}
                                    onValueChange={(newValue) => {
                                        const updated = [...gabaritoData];
                                        updated[index] = newValue;
                                        setGabaritoData(updated);
                                    }}
                                >
                                    {isDiscursive ? (
                                        ['X', 'Y', 'N', 'P'].map(option => {
                                            const labelMap = {
                                                X: "X - Em branco/inválido",
                                                Y: "Y - Discursiva correta",
                                                N: "N - Discursiva incorreta",
                                                P: "P - Discursiva não corrigida"
                                            };
                                            return (
                                                <Picker.Item
                                                    label={labelMap[option]}
                                                    value={option}
                                                    key={option}
                                                />
                                            );
                                        })
                                    ) : (
                                        ['A', 'B', 'C', 'D', 'E', 'X'].map(option => (
                                            <Picker.Item label={option} value={option} key={option} />
                                        ))
                                    )}
                                </Picker>
                            </View>
                        </View>
                    );
                })}

                <View style={styles.buttonContainer}>
                    <TouchableOpacity style={styles.button} onPress={handleSubmit}>
                        <Text style={styles.buttonText}>Registrar</Text>
                    </TouchableOpacity>
                </View>
            </ScrollView>
        </LinearGradient>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    gradient: {
        flex: 1,
    },
    scrollContent: {
        alignItems: 'center',
        paddingVertical: 20,
    },
    input: {
        padding: 8,
        margin: 10,
        borderRadius: 10,
        borderColor: '#000',
        borderWidth: 0.5,
        backgroundColor: '#E4E4E4',
        minWidth: '80%',
        elevation: 5
    },
    itemStyle: {
        marginVertical: 8,
        paddingHorizontal: 3,
        borderRadius: 10,
        borderColor: '#000',
        borderWidth: 0.5,
        backgroundColor: '#FFFFFFAA',
        minWidth: '80%',
        elevation: 3,
    },
    row: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    questionLabel: {
        fontSize: 16,
        flex: 1,
    },
    picker: {
        flex: 1,
    },
    buttonContainer: {
        flex: 1,
        justifyContent: 'flex-end',
        marginTop: 15
    },
    button: {
        backgroundColor: '#1BD939',
        borderRadius: 10,
        width: 95,
        alignSelf: 'center',
        elevation: 5
    },
    buttonText: {
        paddingHorizontal: 15,
        paddingVertical: 5,
        textAlign: 'center',
    },
});
