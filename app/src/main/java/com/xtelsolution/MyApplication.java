package com.xtelsolution;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.tamir7.contacts.Contacts;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Vulcl on 3/29/2017
 */

public class MyApplication extends Application {
    public static Context context;

    private FirebaseAnalytics mFirebaseAnalytics;

    public static void log(String title, String content) {
        Log.e(title, content);
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        FirebaseApp.initializeApp(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        initRealm();
        Contacts.initialize(this);
//        initFabric();
    }

    private void initRealm() {
        Realm.init(context);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("customer_v1.realm")
                .schemaVersion(1)
                .build();

        Realm.getInstance(config);
    }

    private void initFabric() {
        Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(false)
                .build();
        Fabric.with(fabric);
    }
}