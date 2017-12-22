package com.xtelsolution.xmec.model.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.xtelsolution.sdk.callback.AbsICmd;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by vivu on 11/24/17.
 */

public abstract class SaveContactModel <T extends RealmObject> extends AbsICmd {
    private static final String TAG = "SaveContactModel";
    private Realm realm;

    private RealmList<T> realmList;

    public SaveContactModel(RealmList<T> realmList) {
        this.realmList = realmList;

        initRealm();
        run();
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void invoke() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm bgRealm) {
                bgRealm.copyToRealmOrUpdate(realmList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                SaveContactModel.this.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(@NonNull Throwable error) {
                Log.e(TAG, "onError: " + error);
                realm.close();
                SaveContactModel.this.onError(error.toString());
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError(message);
    }

    protected abstract void onSuccess();
    protected abstract void onError(String message);
}
