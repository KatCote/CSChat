package com.katcote.chatclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Network network;

    @FXML
    TextField msgField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network((args) -> {
            mainArea.appendText((String) args[0]);
        });
    }

    public void showSettingsMenu(ActionEvent actionEvent){
        SettingsController settingsController = new SettingsController();
        settingsController.showDialog(actionEvent);
    }

    public void sendMsgAction(ActionEvent actionEvent) {
        network.sendMessage(msgField.getText());
        if (msgField.getText().startsWith("/exit")){
            Platform.exit();
        }
        msgField.clear();
        msgField.requestFocus();
    }

    public void aboutWebAction(ActionEvent actionEvent){

    }

    public void exitAction() {
        network.sendMessage("/exit");
        network.close();
        Platform.exit();
    }

}