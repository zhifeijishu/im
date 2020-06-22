package com.tksflysun.hi.bean.req;

import com.tksflysun.hi.bean.UserAttribute;

public class RegisterReq {
    private UserAttribute attr;
    public RegisterReq(String mobilePhone, String password, String inviteCode, String confirmPassword, String nickName) {
        super();
        this.mobilePhone = mobilePhone;
        this.password = password;
        this.inviteCode = inviteCode;
        this.confirmPassword = confirmPassword; 
        attr=new UserAttribute();attr.setNickName(nickName);
    }

    public RegisterReq() {
        super();
    }

    private String mobilePhone;
    private String password;
    private String inviteCode;
    private transient String confirmPassword;

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public UserAttribute getAttr() {
        return attr;
    }

    public void setAttr(UserAttribute attr) {
        this.attr = attr;
    }
}
