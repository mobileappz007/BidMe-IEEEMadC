package com.bidme.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ViewAllAuctionDTO implements Serializable {

    String id = "";
    String pro_pub_id = "";
    String user_pub_id = "";
    String title = "";
    String brand_id = "";
    String model_id = "";
    String image = "";
    String cat_id = "";
    String sub_cat_id = "";
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
    String currency_code = "";
    String gallery_count = "";
    String favourite = "";
    String Allrating="";
    String rating="";
    String sort_description="";


    UserDTO users;

    String cat_title="";
    String remaining_time="";


    public String getRemaining_time() {
        return remaining_time;
    }

    public void setRemaining_time(String remaining_time) {
        this.remaining_time = remaining_time;
    }

    ArrayList<BidsDTO> bids = new ArrayList();
    ArrayList<GalleryDTO> gallery = new ArrayList();

    WinnersDTO Winner_details=null;


    public WinnersDTO getWinner_details() {
        return Winner_details;
    }

    public void setWinner_details(WinnersDTO winners_details) {
        Winner_details = winners_details;
    }

    public String getSort_description() {
        return sort_description;
    }

    public void setSort_description(String sort_description) {
        this.sort_description = sort_description;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getAllrating() {
        return Allrating;
    }

    public void setAllrating(String allrating) {
        Allrating = allrating;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getGallery_count() {
        return gallery_count;
    }

    public void setGallery_count(String gallery_count) {
        this.gallery_count = gallery_count;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public UserDTO getUsers() {
        return users;
    }

    public void setUsers(UserDTO users) {
        this.users = users;
    }

    public ArrayList<BidsDTO> getBids() {
        return bids;
    }

    public void setBids(ArrayList<BidsDTO> bids) {
        this.bids = bids;
    }

    public ArrayList<GalleryDTO> getGallery() {
        return gallery;
    }

    public void setGallery(ArrayList<GalleryDTO> gallery) {
        this.gallery = gallery;
    }
}
