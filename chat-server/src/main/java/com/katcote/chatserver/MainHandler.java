package com.katcote.chatserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channelsList = new ArrayList<io.netty.channel.Channel>();
    private String clientName;
    private static int newClientIndex = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client is connected: " + ctx);
        ctx.writeAndFlush("[SERVER_MSG]" + ServerApplication.MOTD + "\n");
        clientName = "Client #" + newClientIndex;
        sysMessage("[SERVER_MSG]" + clientName + " join\n");
        channelsList.add(ctx.channel());
        newClientIndex++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        switch (msg.split(" ")[0].toLowerCase(Locale.ROOT)) {
            case "/changename" -> {
                String clientNameBuf = clientName;
                String preClientName = msg.split("\\s", 2)[1];

                if (preClientName.startsWith(" ")) {
                    while (preClientName.startsWith(" ")) {
                        preClientName = preClientName.substring(1);
                    }
                }

                clientName = preClientName;

                System.out.println(clientNameBuf + " -> " + clientName);
                sysMessage("[SERVER_MSG]" + clientNameBuf + " changed name to " + clientName + "\n");
                return;
            }
            case "/exit" -> {
                System.out.println(clientName + " left");
                sysMessage("[SERVER_MSG]" + clientName + " left\n");
                ctx.close();
                return;
            }
            case "/motd" -> {
                System.out.println("MOTD changed to " +  msg.substring(6));
                ServerApplication.MOTD = msg.substring(6);
                sysMessage("[SERVER_MSG]" + "New MOTD: " + ServerApplication.MOTD + "\n");
                return;
            }
        }
        broadcastMessage(msg, clientName);
    }

    public void sysMessage(String msg) {
        for (io.netty.channel.Channel c : channelsList) {
            c.writeAndFlush(msg);
        }
    }

    public void broadcastMessage(String msg, String clientName) {
        System.out.println(String.format("%s: %s [%s]", new java.util.Date(), msg, clientName));
        String out = String.format("[%s]: %s\n", clientName, msg);
        for (io.netty.channel.Channel c : channelsList) {
            c.writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(("Client " + clientName + " is closed."));
        sysMessage(clientName + " left\n");
        channelsList.remove(ctx.channel());
        ctx.close();
    }

}
