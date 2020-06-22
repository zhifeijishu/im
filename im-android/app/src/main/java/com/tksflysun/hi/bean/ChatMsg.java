package com.tksflysun.hi.bean;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 展示聊天消息的模型
 */
@DatabaseTable(tableName = "hi_chatmsg")
public class ChatMsg {
    @DatabaseField(columnName = "_msg", dataType = DataType.STRING)
    private String msg;// 消息内容
    @DatabaseField(columnName = "_belongtype", dataType = DataType.INTEGER)
    private int belongType;// 消息类型，1 对方消息，2 我的消息
    @DatabaseField(columnName = "_date", dataType = DataType.STRING)
    private String date;// 日期
    @DatabaseField(columnName = "_state", dataType = DataType.INTEGER)
    private int state; //消息发送状态   0发送中  1发送成功   -1 发送失败
    @DatabaseField(columnName = "_msgtype", dataType = DataType.INTEGER)
    private int msgType;//消息类型，语音，文字，图片
    @DatabaseField(id = true, columnName = "id", dataType = DataType.STRING)
    private String id;//消息ID
    @DatabaseField(index = true, columnName = "_fromuserid", dataType =
            DataType.LONG)
    private long fromUserId;
    @DatabaseField(index = true, columnName = "_touserid", dataType =
            DataType.LONG)
    private long toUserId;
    @DatabaseField(index = true, columnName = "_userid", dataType =
            DataType.LONG)
    private long userId;
    @DatabaseField(index = true, columnName = "_friendid", dataType =
            DataType.LONG)
    private long friendId;

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

    public long getFriendId() {
        return friendId;
    }

    public void setFriendId(long friendId) {
        this.friendId = friendId;
    }

    public long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public long getToUserId() {
        return toUserId;
    }

    public void setToUserId(long toUserId) {
        this.toUserId = toUserId;
    }

    public int getBelongType() {
        return belongType;
    }

    public void setBelongType(int belongType) {
        this.belongType = belongType;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public ChatMsg() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
