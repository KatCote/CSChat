package com.katcote.chatclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.ObjectUtil;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

public class ClientCryptography {

    public static class StringEncoder extends MessageToMessageEncoder<CharSequence> {

        private final Charset charset;

        /**
         * Creates a new instance with the current system character set.
         */
        public StringEncoder() {
            this(Charset.defaultCharset());
        }

        /**
         * Creates a new instance with the specified character set.
         */
        public StringEncoder(Charset charset) {
            this.charset = ObjectUtil.checkNotNull(charset, "charset");
        }

        @Override
        protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
            if (msg.length() == 0) {
                return;
            }

            out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), charset));
        }
    }

    public static class StringDecoder extends MessageToMessageDecoder<ByteBuf> {

        // TODO Use CharsetDecoder instead.
        private final Charset charset;

        /**
         * Creates a new instance with the current system character set.
         */
        public StringDecoder() {
            this(Charset.defaultCharset());
        }

        /**
         * Creates a new instance with the specified character set.
         */
        public StringDecoder(Charset charset) {
            this.charset = ObjectUtil.checkNotNull(charset, "charset");
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
            out.add(msg.toString(charset));
        }
    }

}
