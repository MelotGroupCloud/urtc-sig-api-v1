package com.melotyun;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.*;

import java.util.Arrays;
import java.util.zip.Deflater;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;

public class RtcTokenBuilder {
    final private String appId;
    final private String secret;

    public RtcTokenBuilder(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
    }
    
    public String buildToken(String roomId, String userId, long expire) {
    	String nonce = String.valueOf(Math.random() * 1000000);
        long currTime = System.currentTimeMillis() / 1000;

        JSONObject sigDoc = new JSONObject();
        sigDoc.put("ver", "1.0");
        sigDoc.put("nonce", nonce);
        sigDoc.put("time", currTime);
        sigDoc.put("expire", expire);

        String sig = hmacsha256(roomId, userId, nonce, currTime, expire);
        if (sig.length() == 0) {
            return "";
        }
        sigDoc.put("signature", sig);
        Deflater compressor = new Deflater();
        compressor.setInput(sigDoc.toString().getBytes(StandardCharsets.UTF_8));
        compressor.finish();
        byte[] compressedBytes = new byte[2048];
        int compressedBytesLength = compressor.deflate(compressedBytes);
        compressor.end();
        return (new String(Base64URL.base64EncodeUrl(Arrays.copyOfRange(compressedBytes,
                0, compressedBytesLength)))).replaceAll("\\s*", "");
    }

    private String hmacsha256(String roomId, String userId, String nonce, long currTime, long expire) {
        String contentToBeSigned = this.appId + ":" + roomId + ":" + userId + ":" + nonce + ":" + currTime + ":" + expire;
        try {
            byte[] byteKey = this.secret.getBytes(StandardCharsets.UTF_8);
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, "HmacSHA256");
            hmac.init(keySpec);
            byte[] byteSig = hmac.doFinal(contentToBeSigned.getBytes(StandardCharsets.UTF_8));
            return (Base64.getEncoder().encodeToString(byteSig)).replaceAll("\\s*", "");
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            return "";
        }
    }
}