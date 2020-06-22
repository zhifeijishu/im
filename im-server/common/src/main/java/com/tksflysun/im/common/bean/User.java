package com.tksflysun.im.common.bean;

public class User {
    private Long userId;// 用户id，自增
    private String password;// 用户密码
    private String salt;// 用户盐值
    private String mobilePhone;// 用户手机号
    private Long srlNo;// 消息序号
    private Long readSrlNo;// 已读消息序号
    private String updateTimeStamp;// 更新时间
    private String addTimeStamp;// 创建时间
    private String inviteCode; // 邀请码
    private UserAttribute attr;// 用户额外属性
    private String token; // 登录token

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public UserAttribute getAttr() {
        return attr;
    }

    public void setAttr(UserAttribute attr) {
        this.attr = attr;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public Long getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(Long srlNo) {
        this.srlNo = srlNo;
    }

    public Long getReadSrlNo() {
        return readSrlNo;
    }

    public void setReadSrlNo(Long readSrlNo) {
        this.readSrlNo = readSrlNo;
    }

    public String getUpdateTimeStamp() {
        return updateTimeStamp;
    }

    public void setUpdateTimeStamp(String updateTimeStamp) {
        this.updateTimeStamp = updateTimeStamp;
    }

    public String getAddTimeStamp() {
        return addTimeStamp;
    }

    public void setAddTimeStamp(String addTimeStamp) {
        this.addTimeStamp = addTimeStamp;
    }
}
