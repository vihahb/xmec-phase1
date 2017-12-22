package com.xtelsolution.xmec.view.activity.detailhr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.FragmentsAdapter;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.presenter.detailhr.DetailHrPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.hr.UpdateHrActivity;
import com.xtelsolution.xmec.view.activity.inf.detailhr.IDetailHrView;
import com.xtelsolution.xmec.view.fragment.detailhr.DrugHrFragment;
import com.xtelsolution.xmec.view.fragment.detailhr.OverviewHrFragment;
import com.xtelsolution.xmec.view.fragment.detailhr.ReminderHrFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailHrActivity extends BasicActivity implements IDetailHrView {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;

    private ActionBar actionBar;
    private FragmentsAdapter fragmentsAdapter;
    private HealthRecords healthRecords;
    private MenuItem menuItem;

    private boolean isViewOnly = false;
    private final int REQUEST_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hr);
        ButterKnife.bind(this);

        getData();
    }

    private void getData() {
        int hrId = getIntExtra(Constants.OBJECT);

        if (hrId == -1) {
            showErrorGetData(null);
            return;
        }

        try {
            isViewOnly = getIntent().getBooleanExtra(Constants.OBJECT_2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        DetailHrPresenter presenter = new DetailHrPresenter(this);
        presenter.getHealthRecord(hrId);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(healthRecords.getName());
    }

    @SuppressWarnings("ConstantConditions")
    private void initViewpager() {
        String[] tabtext = getResources().getStringArray(R.array.detail_hr_tab);
        List<Fragments> fragmentsList = new ArrayList<>();

        fragmentsList.add(new Fragments(OverviewHrFragment.newInstance()));
        fragmentsList.add(new Fragments(DrugHrFragment.newInstance()));
        fragmentsList.add(new Fragments(ReminderHrFragment.newInstance()));

        fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager(), fragmentsList);
        viewpager.setAdapter(fragmentsAdapter);
        viewpager.setOffscreenPageLimit(3);

        tablayout.setupWithViewPager(viewpager);
        tablayout.getTabAt(0).setText(tabtext[0]).setIcon(R.drawable.icon_overview);
        tablayout.getTabAt(1).setText(tabtext[1]).setIcon(R.drawable.icon_drug);
        tablayout.getTabAt(2).setText(tabtext[2]).setIcon(R.drawable.icon_noti);
    }

    private void initViewpagerChange() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        actionBar.setTitle("Danh sách thuốc");
                        showMenu(false);
                        break;
                    case 2:
                        actionBar.setTitle("Lịch uống thuốc");
                        showMenu(false);
                        break;
                    default:
                        actionBar.setTitle(healthRecords.getName());
                        showMenu(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showMenu(boolean isShow) {
        if (menuItem != null && healthRecords.getEndDateLong() <= 0)
            menuItem.setVisible(isShow);
    }

    public HealthRecords getHealthRecords() {
        return healthRecords;
    }

    public int getHealthRecordsID() {
        return healthRecords.getId();
    }

    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, healthRecords.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onGetHrSuccess(HealthRecords healthRecords) {
        if (healthRecords != null && healthRecords.isValid()) {
            this.healthRecords = healthRecords;
            if (isViewOnly) {
                this.healthRecords.setEndDateLong(1);
            }

            initToolbar();
            initViewpager();
            initViewpagerChange();

            if (healthRecords.getEndDateLong() > 0) {
                if (menuItem != null)
                    menuItem.setVisible(false);
            }
        } else {
            showErrorGetData(null);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_hr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menuItem = menu.findItem(R.id.action_edit);

        if (healthRecords != null && healthRecords.getEndDateLong() > 0) {
            menuItem.setVisible(false);
            return super.onPrepareOptionsMenu(menu);
        }

        FrameLayout rootView1 = (FrameLayout) menuItem.getActionView();
        TextView textView1 = rootView1.findViewById(R.id.txt_title);
        textView1.setText(getString(R.string.menu_hr_edit));

        rootView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
            case R.id.action_edit:
                startActivityForResult(UpdateHrActivity.class, Constants.OBJECT, healthRecords.getId(), REQUEST_UPDATE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_UPDATE:
                if (resultCode == RESULT_OK) {
                    ((OverviewHrFragment) fragmentsAdapter.getItem(0)).update();
                }
                break;
        }
    }
}