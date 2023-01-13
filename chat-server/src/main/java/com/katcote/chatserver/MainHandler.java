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
    private static final List<String> userIPList = new ArrayList<>();
    private String clientName;
    private static int newClientIndex = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Client is connected: " + ctx);
        System.out.print("\n>>>");
        ctx.writeAndFlush("[SERVER_MSG]Wait_for_username");
        clientName = "Client #" + newClientIndex;
        channelsList.add(ctx.channel());
        userNameList.add(clientName);
        userIPList.add(String.valueOf(ctx)
                .substring(0, String.valueOf(ctx).length()-2)
                .split("/")[2]);
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
                System.out.print("\n>>>");
                sysMessage(clientNameBuf + " changed name to " + clientName + "\n");

                int bufIndex = channelsList.indexOf(ctx.channel());

                userNameList.remove(bufIndex);
                userNameList.add(bufIndex, clientName);

                return;
            }
            case "/exit" -> {
                System.out.println(clientName + " left");
                System.out.print("\n>>>");
                sysMessage(clientName + " left\n");
                userNameList.remove(channelsList.indexOf(ctx.channel()));
                userIPList.remove(channelsList.indexOf(ctx.channel()));
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
                System.out.print("\n>>>");
                ServerApplication.MOTD = msg.substring(6);
                sysMessage("New MOTD: " + ServerApplication.MOTD + "\n");
                return;
            }
            case "/help" -> {
                ctx.writeAndFlush("[SERVER_MSG]" +
                        "/changename - Change client name to NotNull (or space at all) " +
                        "for all clients and on a server.\n" +
                        "/exit - Exit chat from both sides.\n" +
                        "/motd - Set MOTD for new clients\n" +
                        "/help - Show this page\n"
                );
                return;
            }
        }
        if(msg.startsWith("[CLIENT_MSG]Username ")){
            if (!(msg.substring(21).toLowerCase(Locale.ROOT).equals("default"))){
                clientName = msg.substring(21);
                int bufIndex = channelsList.indexOf(ctx.channel());
                userNameList.remove(bufIndex);
                userNameList.add(bufIndex, clientName);
            }
            ctx.writeAndFlush("[SERVER_MSG]" + ServerApplication.MOTD + "\n");
            ctx.writeAndFlush("\n");
            sysMessage(clientName + " join\n");
            return;
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

    public static List cListIP() {
        return userIPList;
    }

    public void broadcastMessage(String msg, String clientName) {
        System.out.printf("%s: %s [%s]%n", new java.util.Date(), msg, clientName);
        System.out.print("\n>>>");
        String out = String.format("[%s]: %s\n", clientName, msg);
        for (io.netty.channel.Channel c : channelsList) {
            c.writeAndFlush(out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println(("Client " + clientName + " is closed."));
        System.out.print("\n>>>");
        sysMessage(clientName + " left\n");
        userNameList.remove(channelsList.indexOf(ctx.channel()));
        channelsList.remove(ctx.channel());
        ctx.close();
    }

}
