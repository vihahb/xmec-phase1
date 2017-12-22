package com.xtelsolution.xmec.model.entity;

import java.io.Serializable;

/**
 * Created by ThanhChung on 04/11/2017.
 */

public class NewsPortsModel implements Serializable {
    private String url;
    private String title;
    private String time;
    private String description;
    private String image;

    @Override
    public String toString() {
        return "NewsPortsModel{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
