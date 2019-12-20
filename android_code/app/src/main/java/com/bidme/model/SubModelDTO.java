package com.bidme.model;

import java.io.Serializable;

public class SubModelDTO implements Serializable {


    String model_id = "";
    String cat_id = "";
    String sub_cat_id = "";
    String brand_id = "";
    String model_name = "";

    public SubModelDTO(String model_name) {
        this.model_name = model_name;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
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

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    @Override
    public String toString() {
        return model_name.toString();
    }
}
