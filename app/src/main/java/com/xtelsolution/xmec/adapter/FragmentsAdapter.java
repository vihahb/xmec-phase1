package com.xtelsolution.xmec.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xtelsolution.xmec.model.entity.Fragments;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/11/2017
 * Email: leconglongvu@gmail.com
 */
public class FragmentsAdapter extends FragmentStatePagerAdapter {

    private List<Fragments> listData;

    public FragmentsAdapter(FragmentManager fm, List<Fragments> listData) {
        super(fm);
        this.listData = listData;

    }

    @Override
    public Fragment getItem(int position) {
        return listData.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = listData.get(position).getTitle();
        return title != null ? title : super.getPageTitle(position);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}