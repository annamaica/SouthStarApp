package com.example.maica.southstarapp;

/**
 * Created by Maica on 3/2/2018.
 */

public class UserMessage {
    private String messageID;
    private String user1;
    private String user2;

    public UserMessage(String messageID, String user1, String user2) {
        this.messageID = messageID;
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }
    public UserMessage (){ }
}
