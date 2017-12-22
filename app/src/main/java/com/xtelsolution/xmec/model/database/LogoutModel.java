package com.xtelsolution.xmec.model.database;

import android.support.annotation.NonNull;

import com.facebook.accountkit.AccountKit;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.utils.SharedUtils;

import io.realm.Realm;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class LogoutModel extends AbsICmd {
    private Realm realm;

    public LogoutModel() {
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
                bgRealm.deleteAll();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                realm.close();
                SharedUtils.getInstance().clearData();
                AccountKit.logOut();
                LogoutModel.this.onSuccess(true);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(@NonNull Throwable error) {
                realm.close();
                LogoutModel.this.onSuccess(false);
            }
        });
    }

    @Override
    protected void exception(String message) {
        onSuccess(false);
    }

    protected abstract void onSuccess(boolean isSuccess);
}