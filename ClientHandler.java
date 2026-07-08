import java.io.*;
import java.net.*;
import java.util.Set;

public class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Set<ClientHandler> clientHandlers;
    private String username;

    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // First message from client is the username
            this.username = in.readLine();
            broadcast("User " + username + " has joined the chat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                handleMessage(message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected: " + socket);
        } finally {
            try { socket.close(); } catch (IOException e) { e.printStackTrace(); }
            clientHandlers.remove(this);
            broadcast("User " + username + " has left the chat.");
        }
    }

    private void handleMessage(String message) {
        // Check if message is private (starts with @username)
        if (message.startsWith("@")) {
            int spaceIndex = message.indexOf(" ");
            if (spaceIndex != -1) {
                String targetUser = message.substring(1, spaceIndex);
                String privateMsg = message.substring(spaceIndex + 1);

                boolean found = false;
                for (ClientHandler handler : clientHandlers) {
                    if (handler.username.equalsIgnoreCase(targetUser)) {
                        handler.out.println("(Private) " + username + ": " + privateMsg);
                        this.out.println("(Private to " + targetUser + ") " + privateMsg);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    this.out.println("User " + targetUser + " not found.");
                }
            }
        } else {
            // Normal broadcast
            broadcast(message);
        }
    }

    private void broadcast(String message) {
        for (ClientHandler handler : clientHandlers) {
            handler.out.println(message);
        }
    }
}
