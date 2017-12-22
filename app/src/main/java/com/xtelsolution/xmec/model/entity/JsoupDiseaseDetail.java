package com.xtelsolution.xmec.model.entity;

/**
 * Created by vivu on 11/22/17.
 */

public class JsoupDiseaseDetail {

    private String title;
    private String content;
    private String descrip_title;

    public JsoupDiseaseDetail() {
    }

    public JsoupDiseaseDetail(String title, String content, String descrip_title) {
        this.title = title;
        this.content = content;
        this.descrip_title = descrip_title;
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

    public String getDescrip_title() {
        return descrip_title;
    }

    public void setDescrip_title(String descrip_title) {
        this.descrip_title = descrip_title;
    }

    @Override
    public String toString() {
        return "JsoupDiseaseDetail{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", descrip_title='" + descrip_title + '\'' +
                '}';
    }
}
