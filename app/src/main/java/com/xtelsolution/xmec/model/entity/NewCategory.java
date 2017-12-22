package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ThanhChung on 12/6/17.
 */

public class NewCategory extends RESP_Basic {

    @SerializedName("id")
    private int id;
    @SerializedName("categoryName")
    private String categoryName;
    @SerializedName("parentId")
    private int parentId;
    @SerializedName("flag")
    private String flag;

    @Override
    public String toString() {
        return "NewCategory{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", parentId=" + parentId +
                ", flag='" + flag + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
