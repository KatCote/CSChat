package com.katcote.chatserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channelsList = new ArrayList<>();
    private static final List<String> userNameList = new ArrayList<>();
    private String clientName;
    private static int newClientIndex = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client is connected: " + ctx);
        ctx.writeAndFlush("[SERVER_MSG]" + ServerApplication.MOTD + "\n");
        clientName = "Client #" + newClientIndex;
        sysMessage(clientName + " join\n");
        channelsList.add(ctx.channel());
        userNameList.add(clientName);
        newClientIndex++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
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

                System.out.println("USERNAME: " + clientNameBuf + " -> " + clientName);
                sysMessage(clientNameBuf + " changed name to " + clientName + "\n");

                int bufIndex = channelsList.indexOf(ctx.channel());

                userNameList.remove(bufIndex);
                userNameList.add(bufIndex, clientName);

                return;
            }
            case "/exit" -> {
                System.out.println(clientName + " left");
                sysMessage(clientName + " left\n");
                userNameList.remove(channelsList.indexOf(ctx.channel()));
                channelsList.remove(ctx.channel());
                ctx.close();
                return;
            }
            case "/motd" -> {
                if (msg.substring(5).isBlank()) {
                    ctx.writeAndFlush("[SERVER_MSG]" + ServerApplication.MOTD + "\n");
                    return;
                }
                System.out.println("MOTD: " + ServerApplication.MOTD + " -> " + msg.substring(6));
                ServerApplication.MOTD = msg.substring(6);
                sysMessage("New MOTD: " + ServerApplication.MOTD + "\n");
                return;
            }
        }
        broadcastMessage(msg, clientName);
    }

    public static void sysMessage(String msg) {
        for (io.netty.channel.Channel c : channelsList) {
            c.writeAndFlush("[SERVER_MSG]" + msg);
        }
    }

    public static void stopServer() {
        for (io.netty.channel.Channel c : channelsList) {
            c.writeAndFlush("[SERVER_MSG]" + "Server closed");
        }
    }

    public static List cList() {
        return userNameList;
    }

    public void broadcastMessage(String msg, String clientName) {
        System.out.printf("%s: %s [%s]%n", new java.util.Date(), msg, clientName);
        String out = String.format("[%s]: %s\n", clientName, msg);
        for (io.netty.channel.Channel c : channelsList) {
            c.writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(("Client " + clientName + " is closed."));
        sysMessage(clientName + " left\n");
        userNameList.remove(channelsList.indexOf(ctx.channel()));
        channelsList.remove(ctx.channel());
        ctx.close();
    }

}
