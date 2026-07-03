package com.qffz.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class PasswordUtil {
    private static final String SALT = "qffz_forum_password_salt";

    private PasswordUtil() {}

    public static String encode(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest((SALT + password).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("密码加密失败", e);
        }
    }

    public static boolean matches(String raw, String encoded) {
        return encode(raw).equals(encoded);
    }
}
