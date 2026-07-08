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
                out.println(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
