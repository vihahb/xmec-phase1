package com.xtelsolution.xmec.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by xtel on 11/17/17.
 */

public class JsoupDrugDetail {

    @Expose
    private String summary; //Tóm tắt thuốc
    @Expose
    private String userManual; //Hướng dẫn sử dụng
    @Expose
    private String drugInfo; //Thông tin dược chất
    @Expose
    private String warning; //cảnh báo
    @Expose
    private String contraindications; //Chống chỉ định
    @Expose
    private String sideEffects; //Tác dụng phụ
    @Expose
    private String note; //Lưu ý
    @Expose
    private String overdose; //Quá liều
    @Expose
    private String preservated; //Bảo quản
    @Expose
    private String interactive; //Tương tác

    public JsoupDrugDetail() {
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUserManual() {
        return userManual;
    }

    public void setUserManual(String userManual) {
        this.userManual = userManual;
    }

    public String getDrugInfo() {
        return drugInfo;
    }

    public void setDrugInfo(String drugInfo) {
        this.drugInfo = drugInfo;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getContraindications() {
        return contraindications;
    }

    public void setContraindications(String contraindications) {
        this.contraindications = contraindications;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public void setSideEffects(String sideEffects) {
        this.sideEffects = sideEffects;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOverdose() {
        return overdose;
    }

    public void setOverdose(String overdose) {
        this.overdose = overdose;
    }

    public String getPreservated() {
        return preservated;
    }

    public void setPreservated(String preservated) {
        this.preservated = preservated;
    }

    public String getInteractive() {
        return interactive;
    }

    public void setInteractive(String interactive) {
        this.interactive = interactive;
    }

    @Override
    public String toString() {
        return "JsoupDrugDetail{" +
                "summary='" + summary + '\'' +
                ", userManual='" + userManual + '\'' +
                ", drugInfo='" + drugInfo + '\'' +
                ", warning='" + warning + '\'' +
                ", contraindications='" + contraindications + '\'' +
                ", sideEffects='" + sideEffects + '\'' +
                ", note='" + note + '\'' +
                ", overdose='" + overdose + '\'' +
                ", preservated='" + preservated + '\'' +
                ", interactive='" + interactive + '\'' +
                '}';
    }
}
