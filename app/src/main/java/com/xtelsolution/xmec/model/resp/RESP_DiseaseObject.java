package com.xtelsolution.xmec.model.resp;

import com.google.gson.annotations.Expose;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.model.entity.RESP_Basic;

/**
 * Created by vivu on 11/23/17.
 */

public class RESP_DiseaseObject extends RESP_Basic {

    @Expose
    private int diseaseId;
    @Expose
    private String name;
    @Expose
    private String nameAliases;
    @Expose
    private String images;
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

    public RESP_DiseaseObject() {
    }

    public int getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(int diseaseId) {
        this.diseaseId = diseaseId;
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
        return "RESP_DiseaseObject{" +
                "diseaseId=" + diseaseId +
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

    public DiseaseObject getDiseaseObject(){
        DiseaseObject object = new DiseaseObject();
        object.setName(name);
        object.setNameAliases(nameAliases);
        object.setImages(images);
        object.setDesc(desc);
        object.setOverview(overview);
        object.setReason(reason);
        object.setPrevent(prevent);
        object.setTreatment(treatment);
        return object;
    }
}
