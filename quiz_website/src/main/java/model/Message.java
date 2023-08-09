package model;

import java.time.LocalDateTime;



public class Message {
    public enum MessageType {
        FRIEND_REQUEST, CHALLENGE, NOTE
    }

    private Long messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private LocalDateTime sentDate;
    private boolean isRead;
    private MessageType type;
    private Integer relatedQuizId; // Used for challenge messages.
    private Integer challengeScore; // Used for challenge messages.

    // New getters and setters for these fields.

    // Modify constructor
    public Message(Long messageId, int senderId, int receiverId, String content, LocalDateTime sentDate, boolean isRead, MessageType type, Integer relatedQuizId, Integer challengeScore) {
        // Existing assignments
        this.type = type;
        this.relatedQuizId = relatedQuizId;
        this.challengeScore = challengeScore;
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

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public Integer getRelatedQuizId() {
        return relatedQuizId;
    }

    public void setRelatedQuizId(Integer relatedQuizId) {
        this.relatedQuizId = relatedQuizId;
    }

    public Integer getChallengeScore() {
        return challengeScore;
    }

    public void setChallengeScore(Integer challengeScore) {
        this.challengeScore = challengeScore;
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
                ", type=" + type +
                ", relatedQuizId=" + relatedQuizId +
                ", challengeScore=" + challengeScore +
                '}';
    }
}
