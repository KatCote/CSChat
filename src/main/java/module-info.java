module com.katcote.cschat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.katcote.cschat to javafx.fxml;
    exports com.katcote.cschat;
}