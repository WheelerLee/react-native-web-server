/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */

import { useEffect, useState } from 'react';
import { Platform, StatusBar, StyleSheet, useColorScheme, View } from 'react-native';
import {
  SafeAreaProvider,
} from 'react-native-safe-area-context';
import WebServer from '@activatortube/react-native-web-server';
import WebView from 'react-native-webview';
import RNFS from 'react-native-fs';

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <SafeAreaProvider>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <AppContent />
    </SafeAreaProvider>
  );
}

function AppContent() {
  const [port, setPort] = useState(0);

  useEffect(() => {
    WebServer.start([{
      prefix: '/',
      path: Platform.OS === 'android' ? RNFS.CachesDirectoryPath : RNFS.DocumentDirectoryPath,
    }]).then((result) => {
      console.log('WebServer started at port', result);
      setPort(result);
    }).catch((error) => {
      console.error('WebServer start failed', error);
    });

    return () => {
      WebServer.stop();
    }
  }, []);

  return (
    <View style={styles.container}>
      <WebView
        source={{ uri: `http://localhost:${port}/` }}
      />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
});

export default App;
