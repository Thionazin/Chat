import java.net.*;
import java.io.*;
import java.util.Scanner;

public class clientSide{
    private String hostname;
    private int port;
    private String userName;

    public clientSide(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void execute() {
        /*
        try {
            Socket socket = new Socket(hostname, port);

            System.out.println("Connected to the chat server");

            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
           */
    }

    void setUserName(String userName) {
        this.userName = userName;
    }

    String getUserName() {
        return this.userName;
    }


    public static void main(String[] args) {
        /*
        Scanner input = new Scanner(System.in);

        System.out.println("Please input server ip:");
        String hostName = input.nextLine();
        System.out.println("Please input port:");
        int port = Integer.parseInt(input.nextLine());


        clientSide client = new clientSide(hostName, port);
        client.execute();
        */
        clientApplication client = new clientApplication();
        client.startUp(args);
    }
}
