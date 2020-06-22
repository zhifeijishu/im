package com.tksflysun.im.process.bean;

public class Msg {
    private Long id;
    private String msgId;
    private Long userId;// 消息所属人Id
    private int type;// 1 单聊 2 群聊
    private String content;// 单聊或者群聊的base64编码
    private String addTime;// 入库时间
    private Long srlNo;// 消息序号

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public Long getSrlNo() {
        return srlNo;
    }

    public void setSrlNo(Long srlNo) {
        this.srlNo = srlNo;
    }
}
