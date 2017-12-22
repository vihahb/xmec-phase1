package com.xtelsolution.xmec.model.database;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/13/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class GetSharedMbrModel extends AbsICmd {
    private Realm realm;

    private int mbrID;

    public GetSharedMbrModel(int mbrID) {
        this.mbrID = mbrID;

        initRealm();
        run();
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void invoke() {
        Mbr mbr = realm.copyFromRealm(realm.where(Mbr.class).equalTo("mrbId", mbrID).findFirst());
        realm.close();

        if (mbr != null) {
            for (ShareAccounts shareAccounts : mbr.getShareAccounts()) {
                if (shareAccounts.getShareType() != 1) {
                    mbr.getShareAccounts().remove(shareAccounts);
                }
            }
        }

        onSuccess(mbr);
    }

    @Override
    protected void exception(String message) {
        onSuccess(null);
    }

    protected abstract void onSuccess(Mbr object);
}