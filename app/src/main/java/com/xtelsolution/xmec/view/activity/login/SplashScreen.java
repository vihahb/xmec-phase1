package com.xtelsolution.xmec.view.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.xtelsolution.sdk.callback.ResponseHandle;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.BasicModel;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.resp.RESP_NewCategory;
import com.xtelsolution.xmec.model.server.NewsPostLoadModel;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {
    private BasicModel basicModel = new BasicModel();
    private static final String TAG = "SplashScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), SlideViewActivity.class);
                startActivity(intent);
                finish();
            }
        }, 400);
    }
}