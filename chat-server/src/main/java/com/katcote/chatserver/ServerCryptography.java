package com.katcote.chatserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.ObjectUtil;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class ServerCryptography {

    public static class StringEncoder extends MessageToMessageEncoder<CharSequence> {

        private final Charset charset;

        public StringEncoder() {
            this(Charset.defaultCharset());
        }

        public StringEncoder(Charset charset) {
            this.charset = ObjectUtil.checkNotNull(charset, "charset");
        }

        @Override
        protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) {
            if (msg.length() == 0) {
                return;
            }

            out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), charset));
        }
    }

    public static class StringDecoder extends MessageToMessageDecoder<ByteBuf> {

        private final Charset charset;

        public StringDecoder() {
            this(Charset.defaultCharset());
        }

        public StringDecoder(Charset charset) {
            this.charset = ObjectUtil.checkNotNull(charset, "charset");
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
            out.add(msg.toString(charset));
        }
    }

}
