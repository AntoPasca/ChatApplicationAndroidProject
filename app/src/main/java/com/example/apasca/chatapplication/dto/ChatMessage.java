package com.example.apasca.chatapplication.dto;

import java.util.Date;

public class ChatMessage {
    private String id;
    private MessageType type;
    private String content;
    private String senderUsername;
    private String roomTitle;
    private Date sendTime;
    private LinkPage linkPage;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public LinkPage getLinkPage() {
        return linkPage;
    }

    public void setLinkPage(LinkPage linkPage) {
        this.linkPage = linkPage;
    }

    public enum MessageType {
        CHAT,
        JOIN,
    }
}