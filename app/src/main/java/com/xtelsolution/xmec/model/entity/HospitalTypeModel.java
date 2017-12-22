package com.xtelsolution.xmec.model.entity;

/**
 * Created by ThanhChung on 12/2/17.
 */

public class HospitalTypeModel {
    private int id;
    private String name;
    private String key;
    private int icon;
    private int marker;

    public HospitalTypeModel(int id, String name, String key, int marker, int icon) {
        this.id = id;
        this.name = name;
        this.key = key;
        this.icon = icon;
        this.marker = marker;
    }

    @Override
    public String toString() {
        return "HospitalTypeModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMarker
            () {
        return marker;
    }

    public String getKey() {
        return key;
    }

    public int getIcon() {
        return icon;
    }
}
