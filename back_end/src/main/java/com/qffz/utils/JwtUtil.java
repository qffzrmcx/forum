package com.qffz.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class JwtUtil {
    private static final String DEFAULT_SECRET_BASE64 = "cWZmel9mb3J1bV9kZXZfc2VjcmV0";
    private static final byte[] SECRET = Base64.getDecoder().decode(
            System.getenv().getOrDefault("JWT_SECRET_BASE64", DEFAULT_SECRET_BASE64)
    );
    private static final long EXPIRE_SECONDS = 60L * 60L * 24L * 7L;

    private JwtUtil() {}

    public static String createToken(Long userId, String username, String role) {
        try {
            String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
            String payload = "{\"userId\":" + userId + ",\"username\":\"" + escape(username) + "\",\"role\":\"" + escape(role) + "\",\"exp\":" + (Instant.now().getEpochSecond() + EXPIRE_SECONDS) + "}";
            String unsigned = b64(header.getBytes(StandardCharsets.UTF_8)) + "." + b64(payload.getBytes(StandardCharsets.UTF_8));
            return unsigned + "." + sign(unsigned);
        } catch (Exception e) {
            throw new IllegalStateException("Token生成失败", e);
        }
    }

    public static Map<String, Object> parseToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3 || !sign(parts[0] + "." + parts[1]).equals(parts[2])) {
                return null;
            }
            String json = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            Long exp = longValue(json, "exp");
            if (exp == null || exp < Instant.now().getEpochSecond()) {
                return null;
            }
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("userId", longValue(json, "userId"));
            payload.put("username", stringValue(json, "username"));
            payload.put("role", stringValue(json, "role"));
            payload.put("exp", exp);
            return payload;
        } catch (Exception e) {
            return null;
        }
    }

    private static String sign(String value) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(SECRET, "HmacSHA256"));
        return b64(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    }

    private static String b64(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String escape(String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static Long longValue(String json, String key) {
        String marker = "\"" + key + "\":";
        int start = json.indexOf(marker);
        if (start < 0) return null;
        start += marker.length();
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        return Long.parseLong(json.substring(start, end));
    }

    private static String stringValue(String json, String key) {
        String marker = "\"" + key + "\":\"";
        int start = json.indexOf(marker);
        if (start < 0) return "";
        start += marker.length();
        int end = json.indexOf("\"", start);
        return end < 0 ? "" : json.substring(start, end);
    }
}
