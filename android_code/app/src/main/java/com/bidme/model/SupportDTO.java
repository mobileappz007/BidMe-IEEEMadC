package com.bidme.model;

import java.io.Serializable;

public class SupportDTO implements Serializable {
    String support_pub_id = "";
    String user_pub_id = "";
    String title = "";
    String description = "";
    String status = "";
    String created_at = "";
    String updated_at = "";


    public String getSupport_pub_id() {
        return support_pub_id;
    }

    public void setSupport_pub_id(String support_pub_id) {
        this.support_pub_id = support_pub_id;
    }

    public String getUser_pub_id() {
        return user_pub_id;
    }

    public void setUser_pub_id(String user_pub_id) {
        this.user_pub_id = user_pub_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
