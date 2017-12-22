package com.xtelsolution.xmec.model.entity;

import io.realm.RealmObject;

/**
 * Created by NutIT on 07/11/2017.
 */

public class SearchDrug extends RealmObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}