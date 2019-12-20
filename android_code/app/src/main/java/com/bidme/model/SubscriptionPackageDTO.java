package com.bidme.model;

import java.io.Serializable;

public class SubscriptionPackageDTO implements Serializable {
    String package_pub_id="" ;
    String package_name="" ;
    String price ="";
    String curr_pub_id="" ;
    String auction_count ="";
    String total_days ="";
    String status ="";
    String created_at ="";
    String currency_code="";
    String updated_at="" ;

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getPackage_pub_id() {
        return package_pub_id;
    }

    public void setPackage_pub_id(String package_pub_id) {
        this.package_pub_id = package_pub_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurr_pub_id() {
        return curr_pub_id;
    }

    public void setCurr_pub_id(String curr_pub_id) {
        this.curr_pub_id = curr_pub_id;
    }

    public String getAuction_count() {
        return auction_count;
    }

    public void setAuction_count(String auction_count) {
        this.auction_count = auction_count;
    }

    public String getTotal_days() {
        return total_days;
    }

    public void setTotal_days(String total_days) {
        this.total_days = total_days;
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

