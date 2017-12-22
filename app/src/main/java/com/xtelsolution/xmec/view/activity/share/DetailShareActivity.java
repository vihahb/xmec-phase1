package com.xtelsolution.xmec.view.activity.share;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.FragmentsAdapter;
import com.xtelsolution.xmec.adapter.hr.HealthRecordsAdapter;
import com.xtelsolution.xmec.adapter.mbr.MbrsAdapter;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.presenter.share.DetailSharePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.detailhr.DetailHrActivity;
import com.xtelsolution.xmec.view.activity.inf.share.IDetailShareView;
import com.xtelsolution.xmec.view.fragment.share.SharedItemFragment;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailShareActivity extends BasicActivity implements IDetailShareView {
    private DetailSharePresenter presenter;

    @BindView(R.id.img_avatar)
    RoundImage imgAvatar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_share);
        ButterKnife.bind(this);

        presenter = new DetailSharePresenter(this);

        initToolbar();
        getData();
    }

    private void initToolbar() {
        initToolbar(R.id.toolbar, null);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void getData() {
        int id = getIntExtra(Constants.OBJECT);
        String creatorName;

        try {
            creatorName = getIntent().getStringExtra(Constants.OBJECT_2);
        } catch (Exception e) {
            creatorName = "Chi tiết y bạ";
        }

        toolbarTitle.setText(creatorName);

        if (id != -1) {
            presenter.getMbr(id);
        } else {
            showErrorGetData(null);
        }
    }

    private void setData(Mbr mbr) {
        WidgetUtils.setImageURL(imgAvatar, mbr.getCreatorAvatar(), R.mipmap.ic_small_avatar_default);
    }

    private void initViewpager(Mbr mbr) {
        List<Fragments> list = new ArrayList<>();
        list.add(new Fragments(SharedItemFragment.newInstance(mbr.getMrbId())));
        FragmentsAdapter adapter = new FragmentsAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);
    }

    /**
     * Khởi tạo danh sách Sổ khám bệnh
     */
    private void initRecyclerview(Mbr mbr) {
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        HealthRecordsAdapter healthAdapter = new HealthRecordsAdapter();
        recyclerview.setAdapter(healthAdapter);

        healthAdapter.setListData(mbr.getHealthRecords());
        healthAdapter.setViewOnly(true);

        healthAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (position >= 0) {
                    HealthRecords healthRecords = (HealthRecords) object;
                    Intent intent = new Intent(DetailShareActivity.this, DetailHrActivity.class);
                    intent.putExtra(Constants.OBJECT, healthRecords.getId());
                    intent.putExtra(Constants.OBJECT_2, true);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onGetMbrSuccess(Mbr mbr) {
        closeProgressBar();

        if (mbr == null) {
            showErrorGetData(null);
        } else {
            setData(mbr);
            initViewpager(mbr);
            initRecyclerview(mbr);
        }
    }

    @Override
    public void onErrorInternet() {
        showErrorGetData(getString(R.string.error_no_internet_try_later));
    }

    @Override
    public void showProgressBar() {
        showProgressBar(false, false, getString(R.string.doing_load_data));
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}