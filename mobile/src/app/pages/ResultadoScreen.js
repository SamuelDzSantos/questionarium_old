import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import React from 'react';
import { LinearGradient } from 'expo-linear-gradient';

const GRADIENT_COLORS = ['#002436', '#24B4FC'];
const BUTTON_COLOR = '#1BD939';
const NOTE_BACKGROUND_COLOR = '#C6D4FF';

export default function ResultadoScreen() {
    return (
        <View style={styles.container}>
            <LinearGradient colors={GRADIENT_COLORS} style={styles.gradient}>
                <View style={styles.boxResultado}>
                    <Text style={styles.title}>Avaliação</Text>
                    <View style={styles.subtitleContainer}>
                        <Text style={styles.subtitleText}>UFPR</Text>
                        <Text style={styles.subtitleText}>Professor Nome Sobrenome</Text>
                    </View>
                    <Text style={styles.turma}>Turma Turno Ano</Text>
                    <Text style={styles.turma}>Desenvolvimento de Trabalho de Conclusão de Curso II</Text>
                    <Text style={styles.turma}>Link Prova</Text>
                    <View style={styles.noteContainer}>
                        <Text style={styles.noteLabel}>Nota:</Text>
                        <Text style={styles.noteValue}>80%</Text>
                    </View>
                    <View style={styles.questionsContainer}>
                        <Questions title='Acertos' number={6} total={10} />
                        <Questions title='Erros' number={2} total={10} />
                        <Questions title='Em branco' number={2} total={10} />
                    </View>
                    <View style={styles.buttonContainer}>
                        <TouchableOpacity style={styles.button}>
                            <Text style={styles.buttonText}>Registrar</Text>
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
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingVertical: 10,
    },
    subtitleText: {
        fontSize: 18,
        fontWeight: '600',
    },
    turma: {
        fontSize: 16,
        paddingVertical: 10,
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
