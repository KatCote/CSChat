package com.katcote.chatserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ServerController extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        File currentClass = new File(URLDecoder.decode(ServerController.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath(), StandardCharsets.UTF_8));

        String classDirectory = currentClass.getParent();
        String urlBase = classDirectory.substring(0, classDirectory.length() - 26);
        String fxmlURLStr = "file:/" + urlBase.split(":")[0] +
                ":" + urlBase.substring(2).replaceAll("\\\\", "/") +
                "/CSChat/chat-server/target/classes/com.katcote.chatserver/server.fxml";

        System.out.println(fxmlURLStr);

        URL fxmlURL = new URL(fxmlURLStr);

        System.out.println(fxmlURL);

        FXMLLoader fxmlLoader = new FXMLLoader();
        //fxmlLoader.setLocation(fxmlURL);

        Parent root = fxmlLoader.load(Objects.requireNonNull(getClass().getResource(fxmlURLStr)));
        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("CSChat System (R) Server");
        stage.setScene(scene);
        stage.show();

    }

    public static void launchGUI(String[] args) {
        launch(args);
    }

}
