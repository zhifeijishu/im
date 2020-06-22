package com.tksflysun.im.api.util;

import com.tksflysun.im.common.bean.User;

/**
 * 用户信息线程缓存
 * 
 * @author LENOVO
 *
 */
public class UserLocalCache {
    private static final ThreadLocal<User> USERLOCALCACHE_LOCAL = new ThreadLocal<User>();

    public static User getUser() {
        return USERLOCALCACHE_LOCAL.get();
    }

    public static void setUser(User user) {
        USERLOCALCACHE_LOCAL.set(user);
    }

    public static void cleanUser() {
        USERLOCALCACHE_LOCAL.remove();
    }
}
