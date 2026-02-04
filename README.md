# Java SWT LAN Chat

A simple Java GUI chat application written in Java using **SWT (Simple Widget Toolkit)** and **`java.net.Socket`**.  
It allows multiple hosts on the same local network to chat with each other by connecting to a central server.

## Overview

The project uses a basic client–server model:
- A server listens for incoming connections.
- Clients connect to the server over the local network.
- Messages sent by one client are relayed by the server to the other connected clients.
- A graphical interface built with SWT is used for sending and receiving messages.

## Features

- Java SWT–based graphical user interface  
- Client–server architecture  
- LAN (local network) chat support  
- Real-time message exchange  
- Uses standard Java networking (`Socket` / `ServerSocket`)
- The windows and the GUI elements automatically scale based on your Display

## Requirements

- Java JDK 8 or newer  
- SWT library compatible with your operating system  
- All devices must be connected to the same network

## Usage

1. Start the server (Server.java) on one machine in the network.
2. Launch the client application (ChatConnectionGUI.java) on one or more machines.
3. Connect each client to the server using its IP address and port.
4. Begin chatting with other connected clients.

## Limitations

- No encryption or authentication
- Intended for local networks only
- Designed for learning and experimentation, not production use
- Must allow public and private networks to access the app to work

## Technologies

- Java  
- SWT (Simple Widget Toolkit)  
- `java.net.Socket` and `ServerSocket`
