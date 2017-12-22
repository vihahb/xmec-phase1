package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.RatingObject;
import com.xtelsolution.xmec.model.entity.SelfRateObject;

import java.util.List;

/**
 * Created by vivu on 11/28/17
 * xtel.vn
 */

public class RESP_Hospital_Info extends RESP_Basic {
    @Expose
    public int id;
    @Expose
    public String name;
    @Expose
    public String image;
    @Expose
    public String type;
    @Expose
    public boolean status;
    @Expose
    public String description;
    @Expose
    public String phone;
    @Expose
    public String address;
    @Expose
    public String workingTime;
    @Expose
    public double lat;
    @Expose
    public double lng;
    @Expose
    public String medkit;
    @Expose
    public String costs;
    @Expose
    public String services;
    @Expose
    public String languages;
    @Expose
    public String specialist;
    @Expose
    public SelfRateObject selfRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getMedkit() {
        return medkit;
    }

    public void setMedkit(String medkit) {
        this.medkit = medkit;
    }

    public String getCosts() {
        return costs;
    }

    public void setCosts(String costs) {
        this.costs = costs;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getSpecialist() {
        return specialist;
    }

    public void setSpecialist(String specialist) {
        this.specialist = specialist;
    }

    public SelfRateObject getSelfRate() {
        return selfRate;
    }

    public void setSelfRate(SelfRateObject selfRate) {
        this.selfRate = selfRate;
    }

    @Override
    public String toString() {
        return "RESP_Hospital_Info{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", type='" + type + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", workingTime='" + workingTime + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", medkit='" + medkit + '\'' +
                ", costs='" + costs + '\'' +
                ", services='" + services + '\'' +
                ", languages='" + languages + '\'' +
                ", specialist='" + specialist + '\'' +
                ", selfRate=" + selfRate + '}';
    }
}
