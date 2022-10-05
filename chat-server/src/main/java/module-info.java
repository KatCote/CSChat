module com.katcote.chatserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.all;
    requires io.netty.buffer;
    requires io.netty.transport;
    requires io.netty.codec;


    opens com.katcote.chatserver to javafx.fxml;
    exports com.katcote.chatserver;
}