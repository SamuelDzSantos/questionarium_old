import React, { use, useEffect, useState } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, Alert } from 'react-native';
import { LinearGradient } from 'expo-linear-gradient';
import { RecordService } from './../services/recordService';

const GRADIENT_COLORS = ['#002436', '#24B4FC'];
const BUTTON_COLOR = '#1BD939';
const NOTE_BACKGROUND_COLOR = '#C6D4FF';

export default function ResultadoScreen({ route, navigation }) {

    const [recordId, setRecordId] = useState();
    const [resultData, setResultData] = useState({});
    const [totalCount, setTotalCount] = useState(0);
    const [correctCount, setCorrectCount] = useState(0);
    const [wrongCount, setWrongCount] = useState(0);
    const [blankCount, setBlankCount] = useState(0);

    const recordService = new RecordService()

    useEffect(() => {
        setRecordId(route.params)
    }, [])

    useEffect(() => {
        getData = async () => {
            if (recordId !== undefined && recordId !== null) {
                const id = Number(recordId);
                if (typeof id != 'number' || isNaN(id)) {
                    Alert.alert("ID de avaliação inválido", "Tente novamente.")
                    return;
                }
                const response = await recordService.getResult(id)
                if (!response) {
                    navigation.goBack();
                }
                setResultData(response)
                setTotalCount(response.correctAnswerKeyLetter?.length || 0)

                const correctAnswers = response.correctAnswerKeyLetter;
                const studentAnswers = response.studentAnswerKey;

                let correct = 0;
                let wrong = 0;
                let blank = 0;

                const minLength = Math.min(correctAnswers.length, studentAnswers.length);

                for (let i = 0; i < minLength; i++) {
                    const correctLetter = correctAnswers[i];
                    const studentLetter = studentAnswers[i];

                    if (studentLetter === 'X') {
                        blank++;
                    } else if (studentLetter === correctLetter) {
                        correct++;
                    } else {
                        wrong++;
                    }
                }

                setCorrectCount(correct);
                setWrongCount(wrong);
                setBlankCount(blank);
            }
        };
        getData();
    }, [recordId]);

    return (
        <View style={styles.container}>
            <LinearGradient colors={GRADIENT_COLORS} style={styles.gradient}>
                <View style={styles.boxResultado}>
                    <Text style={styles.title}>Avaliação</Text>
                    <View style={styles.subtitleContainer}>
                        <Text style={styles.subtitleText}>{resultData['institution']}</Text>
                        <Text style={styles.subtitleText}>Prof: {resultData['professor']}</Text>
                    </View>
                    <Text style={styles.turma}>Departamento: {resultData['department']}</Text>
                    <Text style={styles.turma}>Turma: {resultData['classroom']}</Text>
                    <Text style={[styles.turma, styles.identificador]}>id:questionarium/{recordId}</Text>
                    <Text style={styles.title}>Aluno {resultData['studentName']}</Text>

                    <View style={styles.noteContainer}>
                        <Text style={styles.noteLabel}>Nota:</Text>
                        <Text style={styles.noteValue}>{Number(resultData['obtainedScore']).toFixed(2)}/{resultData['totalScore']}</Text>
                    </View>
                    <View style={styles.questionsContainer}>
                        <Questions title='Acertos' number={correctCount} total={totalCount} />
                        <Questions title='Erros' number={wrongCount} total={totalCount} />
                        <Questions title='Em branco' number={blankCount} total={totalCount} />
                    </View>
                    <View style={styles.buttonContainer}>
                        <TouchableOpacity style={styles.button} onPress={() => (navigation.navigate('QRScanner'))}>
                            <Text style={styles.buttonText}>Próxima Prova</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </LinearGradient>
        </View>
    );
}

function Questions({ title, number, total }) {
    return (
        <View style={styles.questionItem}>
            <Text>{title}</Text>
            <Text>{number}/{total}</Text>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    gradient: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    boxResultado: {
        backgroundColor: 'white',
        width: '85%',
        height: '85%',
        borderRadius: 10,
        padding: 15,
    },
    title: {
        fontWeight: 'bold',
        fontSize: 24,
        textAlign: 'center',
    },
    subtitleContainer: {
        flexDirection: 'column',
        paddingVertical: 10,
        alignItems: 'center'
    },
    subtitleText: {
        fontSize: 18,
        fontWeight: '600',
    },
    turma: {
        fontSize: 16,
        paddingVertical: 10,
    },
    identificador: {
        textDecorationLine: 'underline'
    },
    noteContainer: {
        flexDirection: 'row',
        paddingVertical: 15,
    },
    noteLabel: {
        flex: 1,
        fontSize: 24,
        fontWeight: 'bold',
    },
    noteValue: {
        fontSize: 24,
        backgroundColor: NOTE_BACKGROUND_COLOR,
        paddingHorizontal: 13,
        borderRadius: 15,
        fontWeight: 'bold',
        elevation: 5
    },
    questionsContainer: {
        marginTop: 20,
    },
    questionItem: {
        flexDirection: 'row',
        backgroundColor: '#D9D9D9',
        padding: 10,
        borderRadius: 15,
        alignItems: 'center',
        justifyContent: 'space-between',
        marginBottom: 20,
        elevation: 5
    },
    buttonContainer: {
        flex: 1,
        justifyContent: 'flex-end',
    },
    button: {
        backgroundColor: BUTTON_COLOR,
        borderRadius: 10,
        width: 95,
        alignSelf: 'center',
        elevation: 5
    },
    buttonText: {
        paddingHorizontal: 15,
        paddingVertical: 5,
        // color: 'white',
        textAlign: 'center',
    },
});
