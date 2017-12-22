package com.xtelsolution.xmec.model.entity;

/**
 * Created by xtel on 11/17/17.
 */

public class InfoDrug {

    private String title;
    private String content;

    public InfoDrug() {
    }

    public InfoDrug(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "InfoDrug{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
