/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author wywang
 */
import java.awt.Insets;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class clientApplication extends Application {
    
    private String address;
    private int portNumber;
    private String userId;
    
    private ReadThread reader;
    private WriteThread writer;
    
    public ObservableList<String> messageList;
    
    public void startUp(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        login();
    }
    
    public void login() {
        Stage loginStage = new Stage();
        loginStage.setTitle("Connect To A Server");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        Scene scene = new Scene(grid, 300, 275);
        
        Text scenetitle = new Text("Welcome");
        scenetitle.setFont(Font.font("Tahoma", 20));
        scenetitle.setTextAlignment(TextAlignment.CENTER);
        grid.add(scenetitle, 0, 0, 2, 1);
        
        Label serverIp = new Label("Server IP");
        grid.add(serverIp, 0, 1);
        
        TextField ipBox = new TextField();
        grid.add(ipBox, 1, 1);
        
        
        Label port = new Label("Port");
        grid.add(port, 0, 2);

        TextField portBox = new TextField();
        grid.add(portBox, 1, 2);
        
        Label username = new Label("Username");
        grid.add(username, 0, 3);

        TextField userBox = new TextField();
        grid.add(userBox, 1, 3);
        
        Button launch = new Button();
        launch.setText("Start Session");
        grid.add(launch, 0, 4, 1, 4);
        
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        
        launch.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println(ipBox.getText());
                System.out.println(portBox.getText());
                if(ipBox.getText().equals("") || portBox.getText().equals("") || userBox.getText().equals(""))
                {
                    actiontarget.setFill(Color.FIREBRICK);
                    actiontarget.setText("Invalid Input!");
                }
                else
                {
                    actiontarget.setText("");
                    try 
                    {
                        address = ipBox.getText();
                        portNumber = Integer.parseInt(portBox.getText());
                        userId = userBox.getText();
                        System.out.println("Stage one finished");
                        setMainScreen();
                        loginStage.close();
                        
                    } catch (Exception e) 
                    {
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Invalid Input!");
                    }
                }
                
            }
        });
        
        loginStage.setScene(scene);
        loginStage.show();
    }
    
    public void setMainScreen () {
        Stage mainStage = new Stage();
        
        mainStage.setTitle("Bungle Chat v1.0.0");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        Scene scene = new Scene(grid, 800, 450);
        connect(address, portNumber);
        System.out.println("Hello!");
        messageList = FXCollections.<String>observableArrayList();
        ListView<String> messages = new ListView<String>(messageList);
        messages.setOrientation(Orientation.VERTICAL);
        messages.setPrefSize((scene.getWidth()*9)/10, (scene.getHeight()*8)/10);
        grid.add(messages, 0, 0);
        TextField textBox = new TextField();
        grid.add(textBox, 0, 1);
        
        Button send = new Button();
        send.setText("Send Message");
        grid.add(send, 0, 2);
        
        
        
        send.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                if(textBox.getText().equals(""))
                {
                    
                }
                else
                {
                    writer.sendMessage(textBox.getText());
                    messageList.add("[You]: " + textBox.getText());
                    textBox.setText("");
                }
            }
        });
        
        mainStage.setScene(scene);
        mainStage.show();
    }
    
    public void connect(String hostname, int port) {
        System.out.println(address);
        System.out.println(portNumber);
        try {
            Socket socket = new Socket(address, portNumber);

            System.out.println("Connected to the chat server");

            reader = new ReadThread(socket, this);
            writer = new WriteThread(socket, this, userId);
            reader.start();
            writer.start();

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }

    }
    
    public String getUserName()
    {
        return userId;
    }
    
    public void setUserName(String username)
    {
        userId = username;
    }
    
    public void addMessage(String message)
    {
        messageList.add(message);
    }
    
    @Override
    public void stop() throws Exception {
        super.stop(); //To change body of generated methods, choose Tools | Templates.
        System.exit(0);
    }
}
