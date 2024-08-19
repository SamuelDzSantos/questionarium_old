import { StyleSheet, Image, View, Text } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { HomeScreen, ResultadoScreen } from './pages';


export default function App() {

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
      {/* ScreenOptions: default config for all reactNavigation headers */}
      <Stack.Navigator initialRouteName="Home" screenOptions={{
        headerStyle: {
          backgroundColor: '#002436',
          // backgroundColor: '#FFF',
        },
        headerTitleAlign: 'center',
        headerTitleStyle: {
          color: '#FFF'
        },
        headerTintColor: '#FFF',
        headerShadowVisible: false
      }}>
        <Stack.Screen name="Home" component={HomeScreen}
          options={{ headerTitle: (props) => <LogoTitle {...props} /> }}
        />
        <Stack.Screen name="Resultado" component={ResultadoScreen}
          options={{ headerTitle: () => <LogoTitleMin title='Resultado' /> }}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
