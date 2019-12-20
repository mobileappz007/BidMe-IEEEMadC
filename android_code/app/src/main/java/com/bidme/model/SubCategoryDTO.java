package com.bidme.model;

import java.io.Serializable;

public class SubCategoryDTO implements Serializable {


    String cat_id = "";
    String sub_cat_id = "";
    String sub_cat_title = "";
    String image = "";
    String status = "";
    String created_at = "";
    String updated_at = "";

    public SubCategoryDTO(String sub_cat_title) {
        this.sub_cat_title = sub_cat_title;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getSub_cat_title() {
        return sub_cat_title;
    }

    public void setSub_cat_title(String sub_cat_title) {
        this.sub_cat_title = sub_cat_title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public String toString() {
        return sub_cat_title.toString();
    }
}
