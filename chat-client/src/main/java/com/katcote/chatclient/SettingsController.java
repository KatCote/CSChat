package com.katcote.chatclient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    TextField usernameField;

    public void showDialog() {
        try {

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = fxmlLoader.load();
            stage.setTitle("Settings");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image(ClientApplication.iconURL));
            stage.initModality(Modality.WINDOW_MODAL);
            //stage.initOwner();
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(){
        Controller.systemMsgAction("/changename " + usernameField.getText());
        usernameField.clear();
    }

}
