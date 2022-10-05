module com.katcote.chatclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.katcote.chatclient to javafx.fxml;
    exports com.katcote.chatclient;
}