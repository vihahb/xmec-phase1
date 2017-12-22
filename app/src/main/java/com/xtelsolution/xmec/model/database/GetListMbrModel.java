package com.xtelsolution.xmec.model.database;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.xmec.model.entity.Mbr;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/13/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class GetListMbrModel extends AbsICmd {
    private Realm realm;

    public GetListMbrModel() {
        initRealm();
        run();
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void invoke() {
        List<Mbr> list;
        list = realm.copyFromRealm(realm.where(Mbr.class).notEqualTo("shareType", 1).findAll());
        realm.close();

        RealmList<Mbr> realmList = new RealmList<>();
        realmList.addAll(list);
        onSuccess(realmList);
    }

    @Override
    protected void exception(String message) {
        onSuccess(null);
    }

    protected abstract void onSuccess(RealmList<Mbr> realmList);
}