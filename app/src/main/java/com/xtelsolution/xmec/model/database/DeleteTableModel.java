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
public abstract class DeleteTableModel<T extends RealmObject> extends AbsICmd {
    private Realm realm;

    private Class<T> tClass;

    public DeleteTableModel(Class<T> tClass) {
        this.tClass = tClass;

        initRealm();
        run();
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void invoke() {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm bgRealm) {
                bgRealm.delete(tClass);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                DeleteTableModel.this.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(@NonNull Throwable error) {
                realm.close();
                DeleteTableModel.this.onError();
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