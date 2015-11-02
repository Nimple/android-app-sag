package de.nimple.util;

import java.security.MessageDigest;

public class Crypto {
    private static byte[] md5(byte[] data) {
        byte messageDigest[] = null;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(data);
            messageDigest = digest.digest();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return messageDigest;
    }

    public static String md5Hex(byte[] s) {
        byte messageDigest[] = md5(s);
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1)
                hexString.append("0");
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String md5Hex(String s) {
        return md5Hex(s.getBytes());
    }
}