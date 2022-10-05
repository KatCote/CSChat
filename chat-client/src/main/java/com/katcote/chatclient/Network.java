package com.katcote.chatclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Network {

    private SocketChannel sChannel;

    private static final String HOST = "localhost";
    private static final int PORT = 8189;

    public Network(CallBack onMsgReceivedCallBack){

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
                                        new StringDecoder(),
                                        new StringEncoder(),
                                        new ClientHandler(onMsgReceivedCallBack));
                            }
                        });

                ChannelFuture future = b.connect(HOST, PORT).sync();

                future.channel().close();
                future.channel().closeFuture().sync();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }

        });

        t.setDaemon(true);
        t.start();

    }

    public void sendMessage(String str){
        sChannel.writeAndFlush(str);
    }

}
