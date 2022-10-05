module com.katcote.chatclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.netty.transport;
    requires io.netty.buffer;
    requires io.netty.common;
    requires io.netty.all;
    requires io.netty.handler;
    requires io.netty.codec;

    opens com.katcote.chatclient to javafx.fxml;
    exports com.katcote.chatclient;
}