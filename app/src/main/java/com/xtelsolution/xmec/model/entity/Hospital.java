package com.xtelsolution.xmec.model.entity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/24/2017
 * Email: leconglongvu@gmail.com
 */
public class Hospital implements Serializable {
    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private String address;
    @Expose
    private Integer rating;
    @Expose
    private Integer totalDoctor;
    @Expose
    private String tag;
    @Expose
    private String image;
    @Expose
    private Double lat;
    @Expose
    private Double lng;
    @Expose
    private String type;

    public Hospital() {
        id = -1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getTotalDoctor() {
        return totalDoctor;
    }

    public void setTotalDoctor(Integer totalDoctor) {
        this.totalDoctor = totalDoctor;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return name;
    }

    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }
}