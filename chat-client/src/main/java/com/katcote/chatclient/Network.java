package com.katcote.chatclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Network {

    private SocketChannel sChannel;
    private static String HOST;
    private static int PORT;

    public Network(CallBack onMsgReceivedCallBack){

        Properties props = new Properties();

        try {
            props.load(new FileInputStream("config.ini"));
        } catch (IOException e) {e.printStackTrace();}

        HOST = props.getProperty("HOST");
        PORT = Integer.parseInt(props.getProperty("PORT"));

        Thread t = new Thread(() -> {
            EventLoopGroup workerGroup = new NioEventLoopGroup();

            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                sChannel = socketChannel;
                                socketChannel.pipeline().addLast(
                                        new ClientCryptography.StringDecoder(),
                                        new ClientCryptography.StringEncoder(),
                                        new ClientHandler(onMsgReceivedCallBack));
                            }
                        });

                ChannelFuture future = b.connect(HOST, PORT).sync();
                future.channel().closeFuture().sync();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }

        });
        t.start();

    }

    public void close() {
        sChannel.close();
    }

    public void sendMessage(String str){
        sChannel.writeAndFlush(str);
    }
}
