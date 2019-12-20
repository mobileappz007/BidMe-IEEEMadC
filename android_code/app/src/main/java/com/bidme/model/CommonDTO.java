package com.bidme.model;

import java.io.Serializable;

public class CommonDTO implements Serializable {
    private String id;
    private String catName;


    public CommonDTO(String id, String catName) {
        this.id = id;
        this.catName = catName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    @Override
    public String toString() {
        return catName.toString();
    }
}
