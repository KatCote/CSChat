module com.katcote.chatserver {
    requires io.netty.all;
    requires io.netty.buffer;
    requires io.netty.transport;
    requires io.netty.codec;
    requires io.netty.common;


    opens com.katcote.chatserver to javafx.fxml;
    exports com.katcote.chatserver;
}