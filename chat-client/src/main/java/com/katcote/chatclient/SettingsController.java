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
import java.util.Objects;
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

            Properties props = new Properties();

            try {
                props.load(new FileInputStream("config.ini"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            int WIDTH = Integer.parseInt(props.getProperty("S_WIDTH"));
            int HEIGHT = Integer.parseInt(props.getProperty("S_HEIGHT"));

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, WIDTH, HEIGHT);

            stage.setTitle("Settings");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.getIcons().add(new Image(ClientApplication.iconURL));
            scene.getStylesheets().add(Objects.requireNonNull(getClass()
                    .getResource(ClientApplication.currentTheme)).toExternalForm());
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

    public static void changeTheme(String theme){

        Properties settings = new Properties();

        try {
            settings.load(new FileInputStream("Settings.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        settings.setProperty("CURRENT_THEME", theme);

        try {
            settings.store(new FileOutputStream("Settings.ini"), "Changed a current theme");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setSoftDarkTheme(ActionEvent actionEvent) {
        changeTheme("dark-theme.css");
    }

    public void setBightWhiteTheme(ActionEvent actionEvent) {
        changeTheme("white-theme.css");
    }

    public void setPinkFloydTheme(ActionEvent actionEvent) {
        changeTheme("pink-floyd-theme.css");
    }

    public void setCustomTheme(ActionEvent actionEvent) {
        changeTheme("custom.css");
    }
}
