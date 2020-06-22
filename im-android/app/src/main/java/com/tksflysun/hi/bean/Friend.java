package com.tksflysun.hi.bean;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "hi_friend")
public class Friend {
    @DatabaseField(index = true,  columnName = "_friendshipid", dataType = DataType.LONG)
    private long friendShipId;// 逻辑主键
    @DatabaseField(id = true, columnName = "_id", dataType = DataType.LONG)
    private long id;// 逻辑主键
    @DatabaseField(index = true, columnName = "_userid", dataType = DataType.LONG)
    private long userId;
    @DatabaseField(columnName = "_friendid", dataType = DataType.LONG)
    private long friendId;
    @DatabaseField(columnName = "_remarkname", dataType = DataType.STRING)
    private String remarkName;// 备注名称
    @DatabaseField(columnName = "_originname", dataType = DataType.STRING)
    private String originName;// 朋友原始名称
    @DatabaseField(columnName = "_addtimestamp", dataType = DataType.STRING)
    private String addTimeStamp;// 添加时间戳
    @DatabaseField(columnName = "_status", dataType = DataType.INTEGER)
    private int status;// 状态，1有效 2 黑名单 3 删除
    @DatabaseField(columnName = "_level", dataType = DataType.INTEGER)
    private int level;// 关系等级，从1 开始
    private Bitmap headIcon;//头像
    @DatabaseField(columnName = "_headiconurl", dataType = DataType.STRING)
    private String headIconUrl;//头像地址

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getFriendShipId() {
        return friendShipId;
    }

    public void setFriendShipId(long friendShipId) {
        this.friendShipId = friendShipId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getFriendId() {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Bitmap getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(Bitmap headIcon) {
        this.headIcon = headIcon;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }
}
