package com.tksflysun.im.common.bean;

public class LoginLog {
    private String loginTimeStamp;// 登录时间戳
    private String token;// 登录token
    private Long userId;// 用户id
    private Integer status;// 状态1 是有效，2是无效，每次重新登录的时候把上次登录的置为2

    private Long loginLogId;

    public Long getLoginLogId() {
        return loginLogId;
    }

    public void setLoginLogId(Long loginLogId) {
        this.loginLogId = loginLogId;
    }

    public String getLoginTimeStamp() {
        return loginTimeStamp;
    }

    public void setLoginTimeStamp(String loginTimeStamp) {
        this.loginTimeStamp = loginTimeStamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
