import java.io.*;
import java.net.*;

public class Client {
    private static final String HOST = System.getenv().getOrDefault("CHAT_HOST", "localhost");
    private static final int DEFAULT_PORT = Integer.parseInt(System.getenv().getOrDefault("CHAT_PORT", "1234"));

    public static void main(String[] args) {
        Socket socket = null;
        int port = DEFAULT_PORT;

        // Try connecting starting from DEFAULT_PORT, increment until success
        while (socket == null) {
            try {
                socket = new Socket(HOST, port);
                System.out.println("Connected to server on port " + port);
            } catch (IOException e) {
                System.out.println("Port " + port + " not available, trying next...");
                port++;
                if (port > DEFAULT_PORT + 50) { // safety limit
                    System.out.println("Unable to connect to server after trying 50 ports.");
                    return;
                }
            }
        }

        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Ask for username
            System.out.print("Enter your username: ");
            String username = console.readLine();
            out.println(username); // send username to server

            // Thread for receiving messages
            new Thread(() -> {
                String response;
                try {
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            // Sending messages
            String input;
            while ((input = console.readLine()) != null) {
                if (input.startsWith("@")) {
                    out.println(input); // private message
                } else {
                    out.println(username + ": " + input); // broadcast
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
