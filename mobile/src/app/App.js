import { StyleSheet, Image, View, Text } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { LoginScreen, ResultadoScreen } from './pages';
import { AuthProvider, useAuth } from './context/AuthContext';
import { Button } from 'react-native-vector-icons/FontAwesome';


export default function App() {

  return (
    <AuthProvider>
      <Layout></Layout>
    </AuthProvider>
  );

}

export const Layout = () => {

  const { authState, onLogout } = useAuth();
  const Stack = createNativeStackNavigator();

  function LogoTitle() {
    return (
      <Image
        style={{ marginTop: 75 }}
        source={require('../assets/logo-questionarium.png')}
      />
    );
  }

  function LogoTitleMin({ title }) {
    return (
      <View style={{ flexDirection: 'row', alignItems: 'center', justifyContent: 'center' }}>
        <Image
          style={{ width: 65, height: 50, marginRight: 10 }}
          source={require('../assets/logo-min-questionarium.png')}
        />
        <Text style={{ color: '#FFF', fontWeight: 'bold', fontSize: 18, textAlign: 'center' }}>
          {title}
        </Text>
      </View>
    );
  }

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={styles.header}>
        {authState.authenticated ? 
          <Stack.Screen name="Resultado" component={ResultadoScreen}
            options={{ headerTitle: () => <LogoTitleMin title='Resultado' />, 
              headerRight: () => <Button onPress={onLogout} title='Sign Out'></Button> }}
          /> : 
          <Stack.Screen name="Login" component={LoginScreen}
            options={{ headerTitle: (props) => <LogoTitle {...props} /> }}
          />

        }
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  header: {
    headerStyle: {
      backgroundColor: '#002436',
    },
    headerTitleAlign: 'center',
    headerTitleStyle: {
      color: '#FFF'
    },
    headerTintColor: '#FFF',
    headerShadowVisible: false
  }
})