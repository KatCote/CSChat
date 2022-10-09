package com.katcote.chatclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.ObjectUtil;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;

public class ClientCryptography {

    public static class StringEncoder extends MessageToMessageEncoder<CharSequence> {

        private static SecretKey getKeyFromPassword(String password, String salt)
                throws NoSuchAlgorithmException, InvalidKeySpecException {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return secret;
        }

        private static String encrypt(String input) throws NoSuchPaddingException,
                NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
                IllegalBlockSizeException, InvalidKeySpecException {

            String algorithm = "AES";
            SecretKey key = getKeyFromPassword("bY87T*68gG*9b((bn#j8", "QWERTY");

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        }

        private final Charset charset;

        public StringEncoder() throws NoSuchAlgorithmException, InvalidKeySpecException {
            this(Charset.defaultCharset());
        }

        public StringEncoder(Charset charset) throws NoSuchAlgorithmException, InvalidKeySpecException {
            this.charset = ObjectUtil.checkNotNull(charset, "charset");
        }

        @Override
        protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
            if (msg.length() == 0) {
                return;
            }

            out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(encrypt((String) msg)), charset));
        }
    }

    public static class StringDecoder extends MessageToMessageDecoder<ByteBuf> {

        private static SecretKey getKeyFromPassword(String password, String salt)
                throws NoSuchAlgorithmException, InvalidKeySpecException {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return secret;
        }

        private static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
                BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, InvalidKeyException {

            String algorithm = "AES";
            SecretKey key = getKeyFromPassword("bY87T*68gG*9b((bn#j8", "QWERTY");

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
            return new String(plainText);
        }

        private final Charset charset;

        public StringDecoder() {
            this(Charset.defaultCharset());
        }

        public StringDecoder(Charset charset) {
            this.charset = ObjectUtil.checkNotNull(charset, "charset");
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
            out.add(msg.toString(charset).replace("\n", "").split(" ")[0] +
                    msg.toString(charset).replace("\n", "").split(" ")[1] + " " +
                    decrypt(msg.toString(charset).replace("\n", "").split(" ")[2]) + "\n");
        }
    }

}
