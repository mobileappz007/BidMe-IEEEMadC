package com.bidme.model;

import java.io.Serializable;

public class BidsDTO implements Serializable {

    String  user_pub_id="";
    String  bid_pub_id="";
    String  pro_pub_id="";
    String  price="";
    String  created_at="";
    String  name="";
    String  image="";
    String  currency_code="";
    String iswin="";
    String status="";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIswin() {
        return iswin;
    }

    public void setIswin(String iswin) {
        this.iswin = iswin;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getUser_pub_id() {
        return user_pub_id;
    }

    public void setUser_pub_id(String user_pub_id) {
        this.user_pub_id = user_pub_id;
    }

    public String getBid_pub_id() {
        return bid_pub_id;
    }

    public void setBid_pub_id(String bid_pub_id) {
        this.bid_pub_id = bid_pub_id;
    }

    public String getPro_pub_id() {
        return pro_pub_id;
    }

    public void setPro_pub_id(String pro_pub_id) {
        this.pro_pub_id = pro_pub_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
