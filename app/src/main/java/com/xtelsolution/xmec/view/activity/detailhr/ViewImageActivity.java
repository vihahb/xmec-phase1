package com.xtelsolution.xmec.view.activity.detailhr;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.detailhr.RvImageAdapter;
import com.xtelsolution.xmec.adapter.detailhr.VpImageAdapter;
import com.xtelsolution.xmec.model.entity.RESP_Image;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.fragment.detailhr.ImageFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewImageActivity extends BasicActivity {

    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private RvImageAdapter rvImageAdapter;

    private RESP_Image resp_image;

    private boolean isShow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ButterKnife.bind(this);

        getData();
    }

    private void getData() {
        int pos = 0;

        try {
            resp_image = (RESP_Image) getIntent().getSerializableExtra(Constants.OBJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            pos = getIntent().getIntExtra(Constants.OBJECT_2, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resp_image == null) {
            showErrorGetData(null);
            return;
        }

        initToolbar(R.id.toolbar, "Danh sách ảnh");
        initViewPager();
        initRecyclerView();
        initActionListener(pos);
    }

    private void initViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();

        for (Object object : resp_image.getData()) {
            fragmentList.add(ImageFragment.newInstance(object));
        }

        VpImageAdapter vpImageAdapter = new VpImageAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setAdapter(vpImageAdapter);
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        rvImageAdapter = new RvImageAdapter();
        rvImageAdapter.setListData(resp_image.getData());

        recyclerview.setAdapter(rvImageAdapter);
    }

    private void initActionListener(int pos) {
        rvImageAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                viewpager.setCurrentItem(position);
            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                rvImageAdapter.setSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewpager.setCurrentItem(pos);
    }

    public void showOrHideView() {

        if (isShow) {
            WidgetUtils.slideView(appBar, -appBar.getHeight());
            WidgetUtils.slideView(recyclerview, recyclerview.getHeight());
        } else {
            WidgetUtils.slideView(appBar, 0);
            WidgetUtils.slideView(recyclerview, 0);
        }

        isShow = !isShow;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}