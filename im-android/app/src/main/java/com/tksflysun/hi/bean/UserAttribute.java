package com.tksflysun.hi.bean;

/**
 * 
 * @author lpf 用户属性表，存储用户可以自己手动修改的数据
 *
 */
public class UserAttribute {
    private Long userId;// 用户id
    private String idCard;// 身份证号
    private String birthDay;// 生日
    private Integer sex;// 性别
    private String realName;// 真是姓名
    private String headIcon;// 用户头像
    private String nickName;// 用户昵称
    private String email;// 用户邮箱

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
