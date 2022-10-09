package com.katcote.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Main extends Application {

    //public static String cssPath = "dark-theme.css";

    @Override
    public void start(Stage primaryStage) throws Exception {

        Properties props = new Properties();

        try {
            props.load(new FileInputStream("config.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int WIDTH = Integer.parseInt(props.getProperty("W_WIDTH"));
        int HEIGHT = Integer.parseInt(props.getProperty("W_HEIGHT"));
        String defaultTheme = props.getProperty("DEFAULT_THEME");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client.fxml"));
        Parent root = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();

        primaryStage.setOnCloseRequest(event -> controller.exitAction());
        primaryStage.setTitle("CSChat System(R)");
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.getIcons().add(new Image("D://IDEA/CSChat/src/icon.png"));
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(defaultTheme)).toExternalForm());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}