package com.bidme.model;

import java.io.Serializable;

public class MyAdsDTO implements Serializable {

    String productname;
    String productduration;
    String productprice;
    int productimage;

    public MyAdsDTO(String productname, String productduration, String productprice, int productimage) {
        this.productname = productname;
        this.productduration = productduration;
        this.productprice = productprice;
        this.productimage = productimage;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductduration() {
        return productduration;
    }

    public void setProductduration(String productduration) {
        this.productduration = productduration;
    }

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public int getProductimage() {
        return productimage;
    }

    public void setProductimage(int productimage) {
        this.productimage = productimage;
    }
}
