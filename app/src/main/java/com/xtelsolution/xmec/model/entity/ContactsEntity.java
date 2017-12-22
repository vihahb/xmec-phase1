package com.xtelsolution.xmec.model.entity;

import io.realm.RealmObject;

/**
 * Created by vivu on 12/9/17
 * xtel.vn
 */

public class ContactsEntity extends RealmObject {

    private String name;
    private String phone_number;

    public ContactsEntity() {
    }

    public ContactsEntity(String name, String phone_number) {
        this.name = name;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    @Override
    public String toString() {
        return "ContactsEntity{" +
                "name='" + name + '\'' +
                ", phone_number='" + phone_number + '\'' + '}';
    }
}
