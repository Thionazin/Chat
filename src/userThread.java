import java.io.*;
import java.net.*;
import java.util.*;

public class userThread extends Thread{
    private Socket socket;
    private serverSide server;
    private PrintWriter writer;
    String name;

    public userThread(Socket socket, serverSide server) {
        this.socket = socket;
        this.server = server;
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            printUsers();

            String userName = reader.readLine();
            name = userName;
            server.addUserName(userName);

            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);

            String clientMessage;

            do {
                try
                {
                    socket.getInputStream();
                }
                catch (Exception e)
                {
                    server.removeUser(userName, this);
                    socket.close();
                    serverMessage = userName + " has timed out.";
                    server.broadcast(serverMessage, this);
                }
                clientMessage = reader.readLine();
                serverMessage = "[" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);

            } while (!clientMessage.equals("bye"));

            server.removeUser(userName, this);
            socket.close();

            serverMessage = userName + " has disconnected.";
            server.broadcast(serverMessage, this);

        } catch (IOException ex) {
            server.broadcast(name + " has disconnected", this);
            System.out.println("Error in UserThread: " + ex.getMessage());
            server.removeUser(name, this);
            ex.printStackTrace();
        }
    }

    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
