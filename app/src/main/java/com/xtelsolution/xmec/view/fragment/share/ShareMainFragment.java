package com.xtelsolution.xmec.view.fragment.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.FragmentsAdapter;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.fragment.IFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lecon on 12/1/2017
 */

public class ShareMainFragment extends IFragment {

    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;

    private List<Fragments> list;
    private FragmentsAdapter adapter;

    public static ShareMainFragment newInstance() {
        return new ShareMainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return onCheckCreateView(inflater.inflate(R.layout.fragment_share_main, container, false));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isViewCreated()) {
            unbinder = ButterKnife.bind(this, view);
            initViewpager();
        } else {
            if (checkSession()) {
                adapter = new FragmentsAdapter(getChildFragmentManager(), list);
                viewpager.setAdapter(adapter);
                tablayout.setupWithViewPager(viewpager);

                try {
                    if (adapter != null) {
                        adapter.getItem(0).onActivityResult(HomeActivity.REQUEST_NOTIFICATION, Activity.RESULT_OK, null);
                        adapter.getItem(1).onActivityResult(HomeActivity.REQUEST_NOTIFICATION, Activity.RESULT_OK, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkSession() {
        return SharedUtils.getInstance().getStringValue(Constants.SESSION) != null;
    }

    private void initViewpager() {
        list = new ArrayList<>();
        list.add(new Fragments(SharedFragment.newInstance(), getString(R.string.layout_shared)));
        list.add(new Fragments(BeSharedFragment.newInstance(), getString(R.string.layout_be_shared)));

        adapter = new FragmentsAdapter(getChildFragmentManager(), list);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        viewpager.removeAllViews();
        adapter = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_not_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == HomeActivity.REQUEST_NOTIFICATION) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    if (adapter != null) {
                        adapter.getItem(1).onActivityResult(requestCode, resultCode, data);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}