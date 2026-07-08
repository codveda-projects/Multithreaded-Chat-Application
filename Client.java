import java.io.*;
import java.net.*;

public class Client {
    private static final String HOST = System.getenv().getOrDefault("CHAT_HOST", "localhost");
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("CHAT_PORT", "1234"));

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
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
                // If message starts with @username, send as-is (server handles private logic)
                if (input.startsWith("@")) {
                    out.println(input);
                } else {
                    // Normal message with username prefix
                    out.println(username + ": " + input);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
