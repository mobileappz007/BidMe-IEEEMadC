package com.bidme.model;


import java.io.Serializable;

public class MyChatsDTO implements Serializable {
    private String chat_id = "";
    private String sender_user_pub_id = "";
    private String receiver_user_pub_id = "";
    private String message = "";
    private String date = "";
    private String user_name = "";
    private String user_image = "";
   private String thread_id="";
   private String id="";


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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
