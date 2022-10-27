package com.example.ITBCloggerdemo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "logs")
public class Log {

    public UUID getLogID() {
        return logID;
    }

    @Id
    private UUID logID;
    private String message;
    private int logType;
    private LocalDate createdDate;



    public Log() {
    }



    public Log(String message, int logType, LocalDate createdDate) {
        this.logID = UUID.randomUUID();
        this.message = message;
        this.logType = logType;
        this.createdDate = createdDate;

    }

    public void setLogID(UUID logID) {
        this.logID = logID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
