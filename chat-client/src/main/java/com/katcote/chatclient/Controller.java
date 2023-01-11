package com.katcote.chatclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import java.util.Properties;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static Network network;

    public static String settingsUsername;

    @FXML
    TextField msgField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network((args) -> mainArea.appendText((String) args[0]));

        Properties settings = new Properties();

        try {
            settings.load(new FileInputStream("Settings.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        settingsUsername = settings.getProperty("USERNAME");

    }

    public void showSettingsMenu() {
        SettingsController settingsController = new SettingsController();
        settingsController.showDialog();
    }

    public void sendMsgAction() {
        network.sendMessage(msgField.getText());
        if (msgField.getText().startsWith("/exit")) {
            Platform.exit();
        }
        msgField.clear();
        msgField.requestFocus();
    }

    public static void systemMsgAction(String msg) {
        network.sendMessage(msg);
    }



    public void aboutWebAction() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Come back again later", ButtonType.OK);
        alert.setHeaderText("About will be added soon");
        alert.setTitle("CSChat Info");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.showAndWait();
    }

    public static void exitAction() {
        network.sendMessage("/exit");
        network.close();
        Platform.exit();
    }

}
