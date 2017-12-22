package com.xtelsolution.xmec.model.entity;

/**
 * Created by vivu on 12/12/17
 * xtel.vn
 */

public class SettingObject {

    private String name;
    private int resource;
    private int type;

    public SettingObject(String name, int resource, int type) {
        this.name = name;
        this.resource = resource;
        this.type = type;
    }

    public SettingObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SettingObject{" +
                "name='" + name + '\'' +
                ", resource=" + resource +
                ", type=" + type +
                '}';
    }
}
