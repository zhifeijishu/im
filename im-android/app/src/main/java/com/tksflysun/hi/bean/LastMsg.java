package com.tksflysun.hi.bean;

import android.graphics.Bitmap;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 最后一条消息
 */
@DatabaseTable(tableName = "hi_lastmsg")
public class LastMsg implements Serializable {
    @DatabaseField( id=true, columnName = "id", dataType = DataType.STRING)
    private String id;//userId+_+friendId
    @DatabaseField(columnName = "_nickname", dataType = DataType.STRING)
    private String nickName;
    @DatabaseField(columnName = "_friendid", dataType = DataType.LONG)
    private long friendId;
    @DatabaseField(  columnName = "_userid", dataType = DataType.LONG)
    private long userId;
    @DatabaseField(columnName = "_lastmsg", dataType = DataType.STRING)
    private String lastMsg;
    @DatabaseField(columnName = "_time", dataType = DataType.STRING)
    private String time;
    @DatabaseField(columnName = "_headiconurl", dataType = DataType.STRING)
    private String headIconUrl;
    @DatabaseField(columnName = "_unreadcount", dataType = DataType.INTEGER)
    private int unreadCount;
    private Bitmap headIcon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getHeadIconUrl() {
        return headIconUrl;
    }

    public void setHeadIconUrl(String headIconUrl) {
        this.headIconUrl = headIconUrl;
    }

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public Bitmap getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(Bitmap headIcon) {
        this.headIcon = headIcon;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getTime() {
        return time;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public void setTime(String time) {
        this.time = time;
    }
}