package com.bidme.model;

import java.io.Serializable;
import java.util.ArrayList;

public class WonBidDTO implements Serializable {

    String won_id = "";
    String owner_user_pub_id = "";
    String won_user_pub_id = "";
    String pro_pub_id = "";
    String title = "";
    String price = "";
    String currency_code = "";
    String image = "";
    String   bid_pub_id= "";
    String          pro_price= "";
    String             created_at= "";
    String       updated_at= "";

    public String getBid_pub_id() {
        return bid_pub_id;
    }

    public void setBid_pub_id(String bid_pub_id) {
        this.bid_pub_id = bid_pub_id;
    }

    public String getPro_price() {
        return pro_price;
    }

    public void setPro_price(String pro_price) {
        this.pro_price = pro_price;
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

    ArrayList<AuncitonImageDTO> image_cust = new ArrayList();


    public String getWon_id() {
        return won_id;
    }

    public void setWon_id(String won_id) {
        this.won_id = won_id;
    }

    public String getOwner_user_pub_id() {
        return owner_user_pub_id;
    }

    public void setOwner_user_pub_id(String owner_user_pub_id) {
        this.owner_user_pub_id = owner_user_pub_id;
    }

    public String getWon_user_pub_id() {
        return won_user_pub_id;
    }

    public void setWon_user_pub_id(String won_user_pub_id) {
        this.won_user_pub_id = won_user_pub_id;
    }

    public String getPro_pub_id() {
        return pro_pub_id;
    }

    public void setPro_pub_id(String pro_pub_id) {
        this.pro_pub_id = pro_pub_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public ArrayList<AuncitonImageDTO> getImage_cust() {
        return image_cust;
    }

    public void setImage_cust(ArrayList<AuncitonImageDTO> image_cust) {
        this.image_cust = image_cust;
    }
}
