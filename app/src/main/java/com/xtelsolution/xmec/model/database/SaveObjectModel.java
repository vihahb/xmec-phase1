package com.xtelsolution.xmec.model.database;

import android.support.annotation.NonNull;

import com.xtelsolution.sdk.callback.AbsICmd;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/13/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class SaveObjectModel<T extends RealmObject> extends AbsICmd {
    private Realm realm;

    private T object;

    public SaveObjectModel(T object) {
        this.object = object;

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
                bgRealm.copyToRealmOrUpdate(object);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                SaveObjectModel.this.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(@NonNull Throwable error) {
                realm.close();
                SaveObjectModel.this.onError();
            }
        });
    }

    @Override
    protected void exception(String message) {
        onError();
    }

    protected abstract void onSuccess();
    protected abstract void onError();
}