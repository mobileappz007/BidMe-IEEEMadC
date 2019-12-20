package com.bidme.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MyFavDTO implements Serializable {
    String id = "";
    String pro_pub_id = "";
    String isFavourite = "";
    String title = "";
    String price = "";
    String currency_code = "";
    String image = "";
    String created_at="";

    ArrayList<AuncitonImageDTO> image_cust = new ArrayList();


    public ArrayList<AuncitonImageDTO> getImage_cust() {
        return image_cust;
    }

    public void setImage_cust(ArrayList<AuncitonImageDTO> image_cust) {
        this.image_cust = image_cust;
    }


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPro_pub_id() {
        return pro_pub_id;
    }

    public void setPro_pub_id(String pro_pub_id) {
        this.pro_pub_id = pro_pub_id;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
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
}
