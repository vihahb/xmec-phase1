package com.xtelsolution.xmec.adapter.news;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xtelsolution.xmec.model.entity.Fragments;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

/**
 * Created by ThanhChung on 04/11/2017.
 */

public class NewsCategoryTabAdapter extends FragmentPagerAdapter {

    List<Fragments> fragmentsList = new ArrayList<>();

    public NewsCategoryTabAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position).getFragment();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

//    public void addFragment(Fragment fragment, String title) {
//        mFragmentList.add(fragment);
//        mFragmentTitleList.add(title);
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentsList.get(position).getTitle();
    }

    public void synchronideCollection(List<Fragments> fragmentsList) {
        this.fragmentsList.addAll(fragmentsList);
        notifyDataSetChanged();
    }
}
