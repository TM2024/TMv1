package com.timeworx.modules.security.oauth2;

import com.timeworx.common.entity.base.Response;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @Description
 * @Author: ryzhang
 * @Date 2023/2/8 8:51 PM
 */
public class TokenGenerator {
    public static Response<String> generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    private static final char[] HEX_CODE = "0123456789abcdef".toCharArray();

    public static String toHexString(byte[] data) {
        if(data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length*2);
        for ( byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }

    public static Response<String> generateValue(String param) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(param.getBytes());
            byte[] messageDigest = algorithm.digest();
            return new Response<>("0", "success", toHexString(messageDigest));
        } catch (Exception e) {
            return new Response<>("1", "token invalid");
        }
    }
}
