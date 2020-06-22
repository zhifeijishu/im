package com.tksflysun.hi.bean;

/**
 * 推送的指令消息
 */
public class PushCmdMsg {
    private int cmdType;
    private String msg;

    public int getCmdType() {
        return cmdType;
    }

    public void setCmdType(int cmdType) {
        this.cmdType = cmdType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
