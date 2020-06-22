package com.tksflysun.im.common.threadlocal;

import com.tksflysun.im.common.bean.User;

public class ServiceContext {

    private static final ThreadLocal<User> LOCALUSER = new ThreadLocal<User>();

    public static void setUser(User user) {
        LOCALUSER.set(user);
    }

    public static User getUser() {
        return LOCALUSER.get();
    }

    public static void remove() {
        LOCALUSER.remove();
    }
}
