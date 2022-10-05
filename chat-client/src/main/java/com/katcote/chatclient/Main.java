package com.katcote.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Properties props = new Properties();

        try {
            props.load(new FileInputStream("config.ini"));
        } catch (IOException e) {e.printStackTrace();}

        int WIDTH = Integer.parseInt(props.getProperty("W_WIDTH"));
        int HEIGHT = Integer.parseInt(props.getProperty("W_HEIGHT"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        primaryStage.setOnCloseRequest(event -> controller.exitAction());
        primaryStage.setTitle("CSChat Client");
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}