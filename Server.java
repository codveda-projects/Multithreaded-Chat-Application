import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    // Default port from environment variable or fallback
    private static final int DEFAULT_PORT = Integer.parseInt(System.getenv().getOrDefault("CHAT_PORT", "1234"));
    private static Set<ClientHandler> clientHandlers = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        ServerSocket serverSocket = null;

        //bind to default port, if busy increment until available
        while (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("Server started on port " + port);
            } catch (BindException e) {
                System.out.println("Port " + port + " is busy, trying next...");
                port++; // move to next port
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        // Accept clients
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                ClientHandler handler = new ClientHandler(clientSocket, clientHandlers);
                clientHandlers.add(handler);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
