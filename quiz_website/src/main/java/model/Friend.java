package model;

import java.time.LocalDateTime;

public class Friend {
    public enum Status {
        PENDING, ACCEPTED
    }

    private int id;
    private int user1Id;
    private int user2Id;
    private Status status;

    private LocalDateTime createdDate;
    private LocalDateTime acceptedDate;

    public Friend() {

    }

    public Friend(int id, int user1Id, int user2Id, Status status, LocalDateTime createdDate, LocalDateTime acceptedDate) {
        this.id = id;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.status = status;
        this.createdDate = createdDate;
        this.acceptedDate = acceptedDate;
    }

    public Friend(int id, int user1Id, int user2Id, Status status) {
        this.id = id;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUser1Id() {
        return user1Id;
    }

    public int getUser2Id() {
        return user2Id;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getAcceptedDate() {
        return acceptedDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser1Id(int user1Id) {
        this.user1Id = user1Id;
    }

    public void setUser2Id(int user2Id) {
        this.user2Id = user2Id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setAcceptedDate(LocalDateTime acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "id=" + id +
                ", user1Id=" + user1Id +
                ", user2Id=" + user2Id +
                ", status=" + status +
                ", createdDate=" + createdDate +
                ", acceptedDate=" + acceptedDate +
                '}';
    }
}
