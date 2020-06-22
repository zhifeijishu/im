package com.tksflysun.im.common.bean;

public class FriendShip implements AuthPrivicyId {
    private Long friendShipId;// 逻辑主键

    private Long userId;
    private Long friendId;
    private String remarkName;// 备注名称
    private String originName;// 朋友原始名称
    private String addTimeStamp;// 添加时间戳
    private Integer status;// 状态，1有效 2 黑名单 3 删除
    private Integer level;// 关系等级，从1 开始

    public Long getFriendShipId() {
        return friendShipId;
    }

    public void setFriendShipId(Long friendShipId) {
        this.friendShipId = friendShipId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getAddTimeStamp() {
        return addTimeStamp;
    }

    public void setAddTimeStamp(String addTimeStamp) {
        this.addTimeStamp = addTimeStamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public Long getAuthPrivicyId() {
        return userId;
    }

}
