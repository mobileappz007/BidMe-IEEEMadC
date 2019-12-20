package com.bidme.model;

import java.io.Serializable;

public class AuctionDTO implements Serializable {
    String pro_pub_id = "";
    String user_pub_id = "";
    String image = "";
    String title = "";
    String cat_id = "";
    String price = "";
    String address = "";
    String latitude = "";
    String longitude = "";
    String description = "";
    String s_date = "";
    String e_date = "";
    String no_of_owner = "";
    String insured = "";
    String created_at = "";
    String updated_at = "";

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getS_date() {
        return s_date;
    }

    public void setS_date(String s_date) {
        this.s_date = s_date;
    }

    public String getE_date() {
        return e_date;
    }

    public void setE_date(String e_date) {
        this.e_date = e_date;
    }

    public String getNo_of_owner() {
        return no_of_owner;
    }

    public void setNo_of_owner(String no_of_owner) {
        this.no_of_owner = no_of_owner;
    }

    public String getInsured() {
        return insured;
    }

    public void setInsured(String insured) {
        this.insured = insured;
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