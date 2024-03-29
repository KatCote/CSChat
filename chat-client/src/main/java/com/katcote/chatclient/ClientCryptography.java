package com.katcote.chatclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.internal.ObjectUtil;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
import java.util.Locale;

public class ClientCryptography {

    private static final String password = "i1m@qu2d=odz=rH9R&gMUY70e=cc#8vd";
    private static final String salt = "u3=%-BS+i068r62^q|0P!AF6taTn?zAc";

    public static class StringEncoder extends MessageToMessageEncoder<CharSequence> {

        private static SecretKey getKeyFromPassword()
                throws NoSuchAlgorithmException, InvalidKeySpecException {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(
                    ClientCryptography.password.toCharArray(),
                    ClientCryptography.salt.getBytes(), 65536, 256);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        }

        private synchronized static String encrypt(String input) throws NoSuchPaddingException, NoSuchAlgorithmException,
                InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

            String algorithm = "AES";
            SecretKey key = getKeyFromPassword();

            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        }

        private final Charset charset;

        public StringEncoder() {
            this(Charset.defaultCharset());
        }

        public StringEncoder(Charset charset) {
            this.charset = ObjectUtil.checkNotNull(charset, "charset");
        }

        @Override
        protected synchronized void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {

            if (msg.toString().isBlank()) {
                return;
            }

            if (msg.toString().endsWith(" ")) {
                while (msg.toString().endsWith(" ")) {
                    msg = msg.toString().substring(0, msg.length() - 1);
                }
            }

            if (msg.toString().startsWith(" ")) {
                while (msg.toString().startsWith(" ")) {
                    msg = msg.toString().substring(1);
                }
            }

            if (serverCommand(msg.toString().toLowerCase(Locale.ROOT), "/changename ") ||
                    serverCommand(msg.toString().toLowerCase(Locale.ROOT), "/exit") ||
                    serverCommand(msg.toString().toLowerCase(Locale.ROOT), "/motd") ||
                    serverCommand(msg.toString().toLowerCase(Locale.ROOT), "/help") ||
                    serverCommand(msg.toString(), "[CLIENT_MSG]Username ") ) {
                out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), charset));
                return;
            }

            out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(encrypt((String) msg)), charset));
        }

        private boolean serverCommand(CharSequence msg, String command) {
            return msg.toString().startsWith(command);
        }
    }

    public static class StringDecoder extends MessageToMessageDecoder<ByteBuf> {

        private static SecretKey getKeyFromPassword()
                throws NoSuchAlgorithmException, InvalidKeySpecException {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(
                    ClientCryptography.password.toCharArray(),
                    ClientCryptography.salt.getBytes(), 65536, 256);
            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        }

        private synchronized static String decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException,
                BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException, InvalidKeyException {

            String algorithm = "AES";
            SecretKey key = getKeyFromPassword();

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
        protected synchronized void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
            if (msg.toString(charset).startsWith("[SERVER_MSG]")) {

                if (msg.toString(charset).equals("[SERVER_MSG]Server closed")) {
                    System.out.println("[Server closed]");
                    wait(1000);
                    Controller.exitAction();
                    return;
                }

                if(msg.toString(charset).equals("[SERVER_MSG]Wait_for_username")){
                    Controller.systemMsgAction("[CLIENT_MSG]Username " + Controller.settingsUsername);
                    return;
                }

                String msg_decoded = msg.toString(charset).substring(12);

                while (msg_decoded.contains("[SERVER_MSG]")) {
                    msg_decoded = msg_decoded.replace("[SERVER_MSG]", "");
                }

                out.add("\n" + msg_decoded + "\n");
                return;
            }
            try {
                out.add(msg.toString(charset).replace("\n", "").split(": ")[0] + ": " +
                        decrypt(msg.toString(charset).replace("\n", "").split(": ")[1]) + "\n");

            } catch (DecoderException | ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

}
