package com.qffz.utils;

public class UserContext {
    private static final ThreadLocal<LoginUser> CURRENT = new ThreadLocal<>();

    private UserContext() {}

    public static void set(LoginUser user) { CURRENT.set(user); }
    public static LoginUser get() { return CURRENT.get(); }
    public static Long userId() { return get() == null ? null : get().getUserId(); }
    public static boolean isAdmin() { return get() != null && "ADMIN".equals(get().getRole()); }
    public static void clear() { CURRENT.remove(); }

    public static class LoginUser {
        private final Long userId;
        private final String username;
        private final String role;

        public LoginUser(Long userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }

        public Long getUserId() { return userId; }
        public String getUsername() { return username; }
        public String getRole() { return role; }
    }
}
