package com.katcote.chatclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private final CallBack onMsgReceivedCallBack;

    public ClientHandler(CallBack onMsgReceivedCallBack) {
        this.onMsgReceivedCallBack = onMsgReceivedCallBack;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
        if (onMsgReceivedCallBack != null) {
            onMsgReceivedCallBack.callback(msg);
        }
        System.out.println(msg.replaceAll("\n", ""));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        cause.printStackTrace();
    }
}
