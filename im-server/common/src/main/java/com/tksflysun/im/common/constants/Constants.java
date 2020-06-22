package com.tksflysun.im.common.constants;

/**
 * 常量
 * 
 * @author LENOVO
 *
 */
public interface Constants {

    static final String USER_INFO_IV_KEY = "Xm@oWksI0197Lks1";

    /**
     * friendship status constants
     * 
     * @author LENOVO
     *
     */
    public interface FRIEND_SHIP_STATUS {
        static final Integer DELETE = 3;
        static final Integer BLACK = 2;
        static final Integer ACTIVE = 1;
    }

    public interface FRIEND_REQ_STATUS {
        static final Integer REJECT = 3;
        static final Integer AGREE = 2;
        static final Integer PENDING = 1;
    }

    public interface ACCOUNT_TYPE {
        int MOBILE = 1;
        int EMAIL = 2;
        int USERNAME = 3;
    }

    public interface LIMIT_PREX {
        String MOBILE = "mobile:";
        String EMAIL = "email:";
        String USERNAME = "username:";
    }

    /**
     * login status
     * 
     * @author LENOVO
     *
     */
    public interface LOGIN_STATUS {
        static final Integer ACTIVE = 1;
        static final Integer INVALID = 2;
    }
}
