package com.bidme.model;

import java.io.Serializable;
import java.util.ArrayList;

public class AutionAllDTO implements Serializable {


    String pro_pub_id ;
    String user_pub_id ;
    String title ;
    String cat_id ;
    String price;
    String address;
    String latitude ;
    String longitude ;
    String description;
    String s_date;
    String e_date;
    String no_of_owner;
    String insured;
    String created_at;
    String updated_at;
    String image;
    String currency_code;
    String cat_title;
    String status;
    String sort_description="";
    String remaining_time="";
    String id;
    String brand_id;
    String model_id;
    String sub_cat_id;
    String product_image;
    String cat_image;

    public String getRemaining_time() {
        return remaining_time;
    }

    public void setRemaining_time(String remaining_time) {
        this.remaining_time = remaining_time;
    }

    public String getSort_description() {
        return sort_description;
    }

    public void setSort_description(String sort_description) {
        this.sort_description = sort_description;
    }

    ArrayList<ImageDTO>image_cust=new ArrayList<>();

    public ArrayList<ImageDTO> getImage_cust() {
        return image_cust;
    }

    public void setImage_cust(ArrayList<ImageDTO> image_cust) {
        this.image_cust = image_cust;
    }




    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getCat_image() {
        return cat_image;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
