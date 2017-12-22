package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vivu on 11/22/17.
 */

public class DiseaseObject implements Serializable{

    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String nameAliases;
    @Expose
    private String images;
    @Expose
    private List<String> listImages;
    @Expose
    private String desc;
    @Expose
    private String overview;
    @Expose
    private String reason;
    @Expose
    private String prevent;
    @Expose
    private String treatment;

    public DiseaseObject() {
    }

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

    public String getNameAliases() {
        return nameAliases;
    }

    public void setNameAliases(String nameAliases) {
        this.nameAliases = nameAliases;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public List<String> getListImages() {
        return listImages;
    }

    public void setListImages(List<String> listImages) {
        this.listImages = listImages;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPrevent() {
        return prevent;
    }

    public void setPrevent(String prevent) {
        this.prevent = prevent;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    @Override
    public String toString() {
        return "DiseaseObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nameAliases='" + nameAliases + '\'' +
                ", images='" + images + '\'' +
                ", desc='" + desc + '\'' +
                ", overview='" + overview + '\'' +
                ", reason='" + reason + '\'' +
                ", prevent='" + prevent + '\'' +
                ", treatment='" + treatment + '\'' +
                '}';
    }


}
