package com.katcote.chatserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ServerApplication extends Application {

    public static String MOTD = "CSChat System v1.3.3 Beta";

    @Override
    public void start(Stage stage) throws IOException {

        File currentClass = new File(URLDecoder.decode(ServerController.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath(), StandardCharsets.UTF_8));

        String classDirectory = currentClass.getParent();
        String urlBase = classDirectory.substring(0, classDirectory.length()-26);
        String fxmlURLStr = "file:/" +  urlBase.split(":")[0] +
                ":" + urlBase.substring(2).replaceAll("\\\\", "/") +
                "/CSChat/chat-server/target/classes/com.katcote.chatserver/server.fxml";

        System.out.println(fxmlURLStr);

        URL fxmlURL = new URL(fxmlURLStr);

        System.out.println(getClass().getResource(fxmlURLStr));
        System.out.println(fxmlURL);

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(fxmlURL);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("CSChat System (R) Server");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {

        //launch(args);

        new Console.serverConsole().start();

        Properties props = new Properties();

        try {
            props.load(new FileInputStream("config.ini"));
        } catch (
                IOException e) {
            e.printStackTrace();
        }

        int PORT = Integer.parseInt(props.getProperty("PORT"));

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new ServerCryptography.StringDecoder(),
                                    new ServerCryptography.StringEncoder(),
                                    new MainHandler());
                        }
                    });
            ChannelFuture future = b.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}