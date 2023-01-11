package com.katcote.chatclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsController {

    @FXML
    TextField usernameField;

    @FXML
    TextField settingsUsernameField;

    @FXML
    ToggleButton setDefaultUsernameButton;

    @FXML
    CheckBox settingsUsernameCheckbox;

    int checkbox_count = 0;

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
            stage.initOwner(ClientApplication.mainApplication);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setUsername() {
        Controller.systemMsgAction("/changename " + usernameField.getText());
        usernameField.clear();
    }

    public void setCheckboxToSettings() {

        boolean checkbox_select;

        if (checkbox_count == 1){
            checkbox_select = true;
        } else {
            checkbox_select = false;
        }

        if (checkbox_count == 2){
            checkbox_count = 0;
        }

        checkbox_count += 1;

        Properties settings = new Properties();

        try {
            settings.load(new FileInputStream("Settings.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(settings.getProperty("USERNAME_CHECKBOX"));

        settingsUsernameCheckbox.setUserData(checkbox_select);
        settings.setProperty("USERNAME_CHECKBOX", String.valueOf(checkbox_select));

        try {
            settings.store(new FileOutputStream("Settings.ini"), "Changed Username Checkbox");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(settings.getProperty("USERNAME_CHECKBOX"));

    }

    public void setUsernameToSettings() {

        Properties settings = new Properties();
        String newUsername = settingsUsernameField.getText();

        try {
            settings.load(new FileInputStream("Settings.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        settings.setProperty("USERNAME", newUsername);
        settingsUsernameField.clear();

        try {
            settings.store(new FileOutputStream("Settings.ini"), "Changed default Username");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setUsernameToDefault() {

        Properties settings = new Properties();

        try {
            settings.load(new FileInputStream("Settings.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        settings.setProperty("USERNAME", "default");

        try {
            settings.store(new FileOutputStream("Settings.ini"), "Return to default Username");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
