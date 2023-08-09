package model;

import java.time.LocalDateTime;

public class Message {
    private Long messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private LocalDateTime sentDate;
    private boolean isRead;

    // Constructor
    public Message(Long messageId, int senderId, int receiverId, String content, LocalDateTime sentDate, boolean isRead) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.sentDate = sentDate;
        this.isRead = isRead;
    }


    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", sentDate=" + sentDate +
                ", isRead=" + isRead +
                '}';
    }
}
