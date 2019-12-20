package com.bidme.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MyBidDTO implements Serializable {


    String bid_pub_id = "";
    String pro_pub_id = "";
    String user_pub_id = "";
    String price = "";
    String created_at = "";
    String updated_at = "";
    String status = "";
    String title = "";

    String currency_code = "";
    String image = "";
    String auction_price="";
    ArrayList<AuncitonImageDTO> image_cust = new ArrayList();




    public ArrayList<AuncitonImageDTO> getImage_cust() {
        return image_cust;
    }

    public void setImage_cust(ArrayList<AuncitonImageDTO> image_cust) {
        this.image_cust = image_cust;
    }

    public String getAuction_price() {
        return auction_price;
    }

    public void setAuction_price(String auction_price) {
        this.auction_price = auction_price;
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

    public String getUser_pub_id() {
        return user_pub_id;
    }

    public void setUser_pub_id(String user_pub_id) {
        this.user_pub_id = user_pub_id;
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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
