package com.katcote.chatclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController {

    @FXML
    TextField loginField;

    Network network = new Network(args -> {});

    public void showDialog() {
        try {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = fxmlLoader.load();
            stage.setTitle("Settings");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            //stage.initModality(Modality.WINDOW_MODAL);
            //stage.initOwner(((Node)actionEvent.getSource()).getScene().getWindow());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLoginAction(ActionEvent actionEvent) {
        network.sendMessage("/changename " + loginField.getText());
        loginField.clear();
    }

}
