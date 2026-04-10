# Java SWT LAN Chat

A simple Java GUI chat application built using **SWT (Simple Widget Toolkit)** and **Java Sockets**.  
It allows multiple users on the same network to connect to a server and chat in real time.

## Features

- GUI-based chat client using SWT
- Server-client architecture using `java.net.Socket`
- Multiple clients can connect to the same server
- Real-time message exchange
- Username-based identification
- Automatic scaling for different resolution monitors

## Project Structure

```
.
├── ChatGUI.java                    # Main client GUI for connecting and chatting
├── ServerGUI.java                  # GUI for starting and managing the server
├── ThServer.java                   # Server thread handling incoming connections
├── ThClientManager.java            # Manages connected clients
├── ThClientMessageListener.java    # Listens for messages from server
├── DeprecatedChatConnectionGUI.java # Old/unused connection GUI (deprecated)
```

## Requirements

- Java JDK 8 or higher
- SWT library (must be added to your project classpath)

## How It Works

### Server
1. Start the server using `ServerGUI`
2. Choose a port to listen on
3. The server waits for incoming client connections

> **Important (Windows / macOS / Linux)**  
> You may need to run the server with **administrator/root privileges** the first time, so Java is allowed to access **private/public networks** (firewall permission prompt).

### Client
1. Launch `ChatGUI`
2. Enter:
   - Username
   - Server IP address (e.g., `192.168.x.x`)
   - Port number
3. Connect to the server
4. Start chatting with other connected users

## Network Notes

- All clients must be on the **same local network (LAN)**
- Ensure:
  - The server machine's firewall allows the selected port
  - Clients use the correct local IP address of the server

## Limitations / Known Issues

This project is intentionally simple and has several limitations:

- **No data encryption** → messages are sent in plain text (not secure)
- **No authentication system** → anyone on the network can connect
- **No message persistence** → messages are not saved
- **LAN only** → does not support internet communication out of the box

## ▶️ Running the Project

### Compile
```bash
javac *.java
```

### Run Server
```bash
java ServerGUI
```

### Run Client
```bash
java ChatGUI
```

## Notes

- `DeprecatedChatConnectionGUI.java` is no longer used and can be ignored or removed
- This project is intended for learning/demo purposes, not production use

---

## 📄 License

This project is open-source and free to use.
