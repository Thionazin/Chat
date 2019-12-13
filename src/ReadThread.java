import java.io.*;
import java.net.*;
import javafx.application.Platform;

public class ReadThread extends Thread{
    private BufferedReader reader;
    private Socket socket;
    private clientApplication client;

    public ReadThread(Socket socket, clientApplication client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String response = reader.readLine();
                client.addMessage(response);

                // prints the username after displaying the server's message
                if (client.getUserName() != null) {
                    //System.out.print("[" + client.getUserName() + "]: ");
                }
            } catch (IOException ex) {
                client.addMessage("Error reading from server: " + ex.getMessage());
                ex.printStackTrace();
                break;
            }
        }
    }

}
