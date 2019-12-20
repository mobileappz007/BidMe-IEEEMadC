package com.bidme.model;

import java.io.Serializable;

public class AdvertiseDTO implements Serializable {
    String image = "";
    String category = "";
    String title = "";
    String price = "";
    String created_at = "";
    String add_pro_id = "";
    String status = "";
    String description = "";

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getAdd_pro_id() {
        return add_pro_id;
    }

    public void setAdd_pro_id(String add_pro_id) {
        this.add_pro_id = add_pro_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
