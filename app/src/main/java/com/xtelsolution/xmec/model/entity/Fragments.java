package com.xtelsolution.xmec.model.entity;

import android.support.v4.app.Fragment;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/11/2017
 * Email: leconglongvu@gmail.com
 */
public class Fragments {
    private Fragment fragment;
    private String title;

    public Fragments() {
    }

    public Fragments(Fragment fragment) {
        this.fragment = fragment;
    }

    public Fragments(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}