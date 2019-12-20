package com.bidme.model;

import java.io.Serializable;

public class ChatMessageDTO implements Serializable {

    private String chat_id = "";
    private String sender_user_pub_id = "";
    private String receiver_user_pub_id = "";
    private String message = "";
    private String sender_name = "";
    private String date = "";
    private String receiver_name = "";




    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getSender_user_pub_id() {
        return sender_user_pub_id;
    }

    public void setSender_user_pub_id(String sender_user_pub_id) {
        this.sender_user_pub_id = sender_user_pub_id;
    }

    public String getReceiver_user_pub_id() {
        return receiver_user_pub_id;
    }

    public void setReceiver_user_pub_id(String receiver_user_pub_id) {
        this.receiver_user_pub_id = receiver_user_pub_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }
}
