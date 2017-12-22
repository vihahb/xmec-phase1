package com.xtelsolution.xmec.model.entity;

/**
 * Created by vivu on 12/12/17
 * xtel.vn
 */

public class CategoryFragmentObj {

    private int id;
    private String name;

    public CategoryFragmentObj(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryFragmentObj{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
