import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import React from 'react';
import { LinearGradient } from 'expo-linear-gradient';

export default function ResultadoScreen({ navigation }) {
    return (
        <View style={styles.resultado}>
            <LinearGradient colors={['#002436', '#24B4FC']} style={styles.gradient}>
                <View style={styles.boxResultado}>
                    <Text style={styles.title}>Avaliação</Text>
                    <View style={styles.subtitle}>
                        <Text style={[styles.subtitleText, { flex: 1, alignSelf: 'flex-start' }]}>UFPR</Text>
                        <Text style={[styles.subtitleText, { alignSelf: 'flex-end' }]}>Professor Nome Sobrenome</Text>
                    </View>
                    <Text style={styles.turma}>Turma Turno Ano</Text>
                    <Text style={styles.turma}>Desenvolvimento de Trabalho de Conclusão de Curso II</Text>
                    <Text style={styles.turma}>Link Prova</Text>
                    <View style={{ flexDirection: 'row', paddingVertical: 15 }}>
                        <Text style={{ flex: 1, fontSize: 24, fontWeight: 'bold' }}>Nota:</Text>
                        <Text style={{ fontSize: 24, backgroundColor: '#C6D4FF', paddingHorizontal: 13, borderRadius: 15, fontWeight: 'bold' }}>80%</Text>
                    </View>
                    <Questions title={'Acertos'} number={6} total={10}/>
                    <Questions title={'Erros'} number={2} total={10}/>
                    <Questions title={'Em branco'} number={2} total={10}/>
                    <TouchableOpacity style={{ backgroundColor: '#1BD939', borderRadius: 10, width: 90, alignSelf: 'center', marginTop: 100 }}>
                        <Text style={{ paddingHorizontal: 15, paddingVertical: 5 }}>Registrar</Text>
                    </TouchableOpacity>
                </View>
            </LinearGradient>
        </View>
    )
}

function Questions({title, number, total}) {
    //TODO ARRUMAR ALINHAMENTO DOS NUMEROS
    return (
        <View style={{
            flexDirection: 'row', backgroundColor: '#D9D9D9', padding: 10,
            borderRadius: 15, alignItems: 'center', justifyContent: 'space-between', marginBottom: 10
        }}>
            <Text>{title}</Text>
            <View style={{ flex: 1, alignItems: 'center' }}>
                <Text>{number}/{total}</Text>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    resultado: {
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
        padding: 15
    },
    title: {
        fontWeight: 'bold',
        fontSize: 24,
        alignSelf: 'center',
    },
    subtitle: {
        flexDirection: 'row',
        paddingVertical: 10,
    },
    subtitleText: {
        fontSize: 18,
        fontWeight: '600'
    },
    turma: {
        fontSize: 16,
        paddingVertical: 10
    }
})
