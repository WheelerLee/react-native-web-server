# react-native-web-server

A lightweight React Native web server to serve app sandbox directories as static files.

[中文文档](./README.zh.md) | [English](./README.md)

## Features

- 🚀 Lightweight and high-performance local web server
- 📁 Support for multiple path prefix mappings
- 🔄 Dynamically start and stop the server
- 📱 Support for iOS and Android platforms
- 🎯 Perfect integration with WebView

> **⚠️ Note:** This library currently only supports React Native's New Architecture. The Old Architecture is not supported. Contributions to add Old Architecture support are welcome via Pull Requests!

## When to Use

This library is ideal for scenarios where you need to serve local files through a web server instead of loading them directly via WebView's `file://` protocol. Here are the key advantages over direct local file loading:

### Advantages over Direct WebView File Loading

1. **Cross-Origin Resource Sharing (CORS) Support**
   - Web content served via HTTP can properly handle CORS policies
   - Allows loading external resources and APIs without CORS restrictions
   - Direct `file://` protocol often blocks cross-origin requests

2. **Network Accessibility**
   - Enables access from other devices on the same local network
   - Perfect for testing and debugging on multiple devices simultaneously
   - Allows sharing content with other apps or services on the network

3. **Standard HTTP Protocol**
   - Uses standard HTTP/HTTPS protocol, ensuring better compatibility with web libraries
   - Many web frameworks and libraries expect HTTP URLs
   - Better support for features like Service Workers, WebSockets, etc.

4. **Dynamic Content Serving**
   - Can serve dynamically generated or modified content
   - Easier to implement routing and path mapping
   - More flexible file serving strategies

5. **Better Security Model**
   - HTTP server can implement proper security headers
   - More control over content delivery and access policies

### Use Cases

- Serving HTML5 games or interactive web content that requires HTTP protocol
- Testing web content on multiple devices simultaneously
- Sharing local content with other apps or services
- Serving content that needs to work with web APIs requiring HTTP context
- Debugging and development scenarios where network access is needed

## Installation

Install using npm or yarn:

```bash
npm install @activatortube/react-native-web-server
# or
yarn add @activatortube/react-native-web-server
```

### iOS Installation

For iOS, you need to install CocoaPods dependencies:

```bash
cd ios && pod install
```

## Usage

### Basic Example

The following example demonstrates how to use `react-native-web-server` in a React Native app to start a local web server and display content in a WebView:

```tsx
import { useEffect, useState } from 'react';
import { View, StyleSheet } from 'react-native';
import WebServer from '@activatortube/react-native-web-server';
import WebView from 'react-native-webview';
import RNFS from 'react-native-fs';

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
```

### Multiple Path Mappings

You can also configure multiple path prefix mappings:

```tsx
WebServer.start([
  {
    prefix: '/',
    path: RNFS.CachesDirectoryPath,
  },
  {
    prefix: '/game',
    path: RNFS.DocumentDirectoryPath + '/game',
  },
]);
```

## API Documentation

### `WebServer.start(handlers: HandlerOptions[]): Promise<number>`

Start the web server.

**Parameters:**
- `handlers`: Array of path handler configurations
  - `prefix`: Request path prefix, root directory is `/`
  - `path`: Folder path to be accessed. For example: if `prefix` is `/game`, then accessing `/game/index.html` will access `path/index.html`

**Returns:**
- `Promise<number>`: Returns the port number where the server is started

**Note:** Can be called multiple times. If already started, it will stop the previously started server and start a new one.

### `WebServer.stop(): void`

Stop the web server.

## Screenshots

![Screenshot 1](./screenshots/Screenshot1.png)

*Accessing the web server directly via WebView within the same app*

![Screenshot 2](./screenshots/ScreenShot2.png)

*Accessing the web server from another device on the local network using IP address and port*

## Requirements

- React >= 18.0.0
- React Native >= 0.72.0

## License

Apache-2.0

## Links

- [GitHub Repository](https://github.com/WheelerLee/react-native-web-server)
