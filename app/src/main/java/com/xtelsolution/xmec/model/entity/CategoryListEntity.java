package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vivu on 12/13/17
 * xtel.vn
 */

public class CategoryListEntity implements Serializable {


    @Expose
    public int id;
    @Expose
    public String title;
    @Expose
    public String image;
    @Expose
    public String link;
    @Expose
    public String description;
    @Expose
    public int categoryId;
    @Expose
    public String postDate;
    @Expose
    public Long postDateLong;

    public CategoryListEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public Long getPostDateLong() {
        return postDateLong;
    }

    public void setPostDateLong(Long postDateLong) {
        this.postDateLong = postDateLong;
    }

    @Override
    public String toString() {
        return "CategoryListEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", postDate='" + postDate + '\'' +
                ", postDateLong='" + postDateLong + '\'' +
                '}';
    }
}
