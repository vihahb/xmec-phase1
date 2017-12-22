package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class UserDrugImages extends RealmObject implements Serializable {
    @Expose
    @PrimaryKey
    private int id;
    @Expose
    private String urlImage;
    @Expose
    private int userDrugId;

    private String imagePath;

    public UserDrugImages() {
        id = -1;
        userDrugId = -1;
    }

    public UserDrugImages(int id, String urlImage, int userDrugId, String imagePath) {
        this.id = id;
        this.urlImage = urlImage;
        this.userDrugId = userDrugId;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getUserDrugId() {
        return userDrugId;
    }

    public void setUserDrugId(int userDrugId) {
        this.userDrugId = userDrugId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
