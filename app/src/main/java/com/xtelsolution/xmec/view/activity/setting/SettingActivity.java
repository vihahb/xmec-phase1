package com.xtelsolution.xmec.view.activity.setting;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.OnItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.SettingObject;
import com.xtelsolution.xmec.presenter.setting.SettingPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.setting.ISettingView;
import com.xtelsolution.xmec.view.activity.webview.WebviewActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BasicActivity implements ISettingView, OnItemClickListener<SettingObject> {


    @BindView(R.id.rcl_setting)
    RecyclerView rclSetting;
    private SettingPresenter presenter;

    private AdapterSetting adapterSetting;
    private static final int RATE_APP_IN_STORE = 2;
    private static final int FEEDBACK_APP_IN_STORE = 1;
    private static final int PRIVACY_USED = 0;
    private static final int REPORT_AND_HELP = 3;
    private static final int ABOUT_US = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initToolbar(R.id.toolbar, "Cài đặt");
        presenter = new SettingPresenter(this);
        initialisingRecyclerView();
    }

    private void initialisingRecyclerView() {
        rclSetting.setLayoutManager(new LinearLayoutManager(this));
        adapterSetting = new AdapterSetting(this, this);
        rclSetting.setAdapter(adapterSetting);
        presenter.initializingList();
    }

    @Override
    public void initializedSetting(List<SettingObject> settingList) {
        adapterSetting.synchronizedCollection(settingList);
    }

    @Override
    public void onClick(int position, SettingObject item) {

        switch (position) {
            case RATE_APP_IN_STORE:
                showMyAppInStore();
                break;
            case FEEDBACK_APP_IN_STORE:
                showMyAppInStore();
                break;
            case PRIVACY_USED:
                startActivity(WebviewActivity.class, Constants.URL_INTENT, "http://xtelsolutions.com");
                break;
            case ABOUT_US:
                startActivity(WebviewActivity.class, Constants.URL_INTENT, "http://xtelsolutions.com");
                break;
        }

    }

    private void showMyAppInStore() {
        Uri uri = Uri.parse("market://details?id=" + MyApplication.context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + MyApplication.context.getPackageName())));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
