package com.bidme.model;

import java.io.Serializable;

public class AuncitonImageDTO implements Serializable {
    
              private String  image="";
    private String     id="";


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
