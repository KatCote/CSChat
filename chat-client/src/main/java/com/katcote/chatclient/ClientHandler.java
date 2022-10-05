package com.katcote.chatclient;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String>{

    private CallBack onMsgReceivedCallBack;

    public ClientHandler(CallBack onMsgReceivedCallBack){
        this.onMsgReceivedCallBack = onMsgReceivedCallBack;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(onMsgReceivedCallBack != null){
            onMsgReceivedCallBack.callback(msg);
        }
        System.out.println(msg);
    }
}
