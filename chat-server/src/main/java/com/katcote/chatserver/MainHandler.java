package com.katcote.chatserver;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class MainHandler extends SimpleChannelInboundHandler<String> {

    private static final List<Channel> channelsList = new ArrayList<io.netty.channel.Channel>();
    private String clientName;
    private static int newClientIndex = 1;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client is connected: " + ctx);
        channelsList.add(ctx.channel());
        clientName = "Client #" + newClientIndex;
        //sysMessage((clientName + " join\n"));
        newClientIndex++;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        /* if (msg.isBlank()) {
            return;
        }
        if (msg.startsWith("/changename ")) {
            if (msg.endsWith(" ")) {
                ctx.writeAndFlush("Name can't end with space\n");
                return;
            }
            String clientNameBuf = clientName;
            clientName = msg.split("\\s", 2)[1];
            ctx.writeAndFlush("Done\n");
            sysMessage(clientNameBuf + " changed name to " + clientName + "\n");
            return;
        } */
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
