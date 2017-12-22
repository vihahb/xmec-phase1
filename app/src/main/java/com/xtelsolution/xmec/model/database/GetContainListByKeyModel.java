package com.xtelsolution.xmec.model.database;

import com.xtelsolution.sdk.callback.AbsICmd;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by vivh on 12/21/17.
 */

public abstract class GetContainListByKeyModel<T extends RealmObject> extends AbsICmd {
    private Realm realm;

    private Class<T> tClass;
    private String key;
    private String value;

    public GetContainListByKeyModel(Class<T> tClass, String key, String value) {
        this.tClass = tClass;
        this.key = key;
        this.value = value;

        initRealm();
        run();
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void invoke() {
        List<T> list;

        if (key != null)
            list = realm.copyFromRealm(realm.where(tClass).contains(key, value).findAll());
        else
            list = realm.copyFromRealm(realm.where(tClass).findAll());

        realm.close();

        RealmList<T> realmList = new RealmList<>();
        realmList.addAll(list);
        onSuccess(realmList);
    }

    @Override
    protected void exception(String message) {
        onSuccess(null);
    }

    protected abstract void onSuccess(RealmList<T> realmList);

}
