package com.katcote.chatclient;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

import java.net.URL;

import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static Network network;

    @FXML
    TextField msgField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network((args) -> mainArea.appendText((String) args[0]));
    }

    public void showSettingsMenu(){
        SettingsController settingsController = new SettingsController();
        settingsController.showDialog();
    }

    public void sendMsgAction() {
        network.sendMessage(msgField.getText());
        if (msgField.getText().startsWith("/exit")){
            Platform.exit();
        }
        msgField.clear();
        msgField.requestFocus();
    }

    public static void systemMsgAction(String msg){
        network.sendMessage(msg);
    }

    public void aboutWebAction(){
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