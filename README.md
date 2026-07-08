# Multithreaded Chat Application

## Overview
A console-based chat application built in **Java** that allows multiple users to communicate in real time.  
The server uses **multithreading** to handle multiple clients simultaneously, and messages are broadcast to all connected clients.

This project demonstrates:
- Java Socket programming (TCP/IP networking)
- Concurrency with threads
- Broadcast messaging across multiple clients
- Secure configuration using environment variables

---

## Features
- Console-based client and server
- Multiple clients can connect at once
- Real-time message broadcasting
- Graceful handling of client disconnects
- Configurable host and port via environment variables

---

## Project Structure
MultithreadedChatApplication/
├── Server.java        # Server entry point
├── Client.java        # Client entry point
├── ClientHandler.java # Threaded client manager
└──.gitignore        
└──Screenshot


---

## Setup & Run

### 1. Clone the repository
```bash
git clone https://github.com/codveda-projects/Multithreaded-Chat-Application.git
cd Multithreaded-Chat-Application

##2.Compile classes
javac Server.java Client.java ClientHandler.java

##3.Run the server 
java Server

##4.Run multiple clients