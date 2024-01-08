package com.leopa.livechatter.model;

public class MessageSend {

    private String sendMsg;

    public MessageSend(String sendMsg) {
        this.sendMsg = sendMsg;
    }

    public String getSendMsg() {
        return sendMsg;
    }

    public void setSendMsg(String sendMsg) {
        this.sendMsg = sendMsg;
    }
}
