package com.katcote.chatclient;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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

    public void sendMsgAction(ActionEvent actionEvent) {
        network.sendMessage(msgField.getText());
        msgField.clear();
        msgField.requestFocus();
    }

    public void exitAction() {
        network.close();
        Platform.exit();
    }

    /*
    public void setWhiteTheme(){
        Main.cssPath = "white-theme.css";
        System.out.println(Main.cssPath);
    }

    public void setDarkTheme(){
        Main.cssPath = "dark-theme.css";
        System.out.println(Main.cssPath);
    }

 */

}