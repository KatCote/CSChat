package com.katcote.chatclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class ClientApplication extends Application {

    public static String iconURL;
    public static Window mainApplication;
    public static String currentTheme;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Properties props = new Properties();

        try {
            props.load(new FileInputStream("config.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Properties settings = new Properties();

        try {
            settings.load(new FileInputStream("Settings.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentTheme = settings.getProperty("CURRENT_THEME");

        File currentClass = new File(URLDecoder.decode(ClientApplication.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath(), StandardCharsets.UTF_8));

        String classDirectory = currentClass.getParent();
        String urlBase = classDirectory.substring(0, classDirectory.length() - 19);
        iconURL = urlBase.split(":")[0] +
                ":/" + urlBase.substring(2).replaceAll("\\\\", "/") +
                "/chat-client/src/main/resources/com.katcote.chatclient/images/icon.png";

        int WIDTH = Integer.parseInt(props.getProperty("W_WIDTH"));
        int HEIGHT = Integer.parseInt(props.getProperty("W_HEIGHT"));
        String defaultTheme = props.getProperty("DEFAULT_THEME");

        if(currentTheme.equals("") || currentTheme.equals("default")){
            currentTheme = defaultTheme;
        }

        System.out.println(currentTheme);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("client.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        primaryStage.setOnCloseRequest(event -> Controller.exitAction());
        primaryStage.setTitle("CSChat System(R)");
        primaryStage.getIcons().add(new Image(iconURL));
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(currentTheme)).toExternalForm());
        primaryStage.show();

        mainApplication = primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}