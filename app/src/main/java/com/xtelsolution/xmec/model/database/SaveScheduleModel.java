package com.xtelsolution.xmec.model.database;

import android.support.annotation.NonNull;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/13/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class SaveScheduleModel extends AbsICmd {
    private Realm realm;

    private HealthRecords healthRecords;
    private ScheduleDrug scheduleDrug;

    public SaveScheduleModel(ScheduleDrug scheduleDrug) {
        this.scheduleDrug = scheduleDrug;

        initRealm();
        run();
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void invoke() {
        healthRecords = realm.copyFromRealm(realm.where(HealthRecords.class).equalTo("id", scheduleDrug.getHealthRecordId()).findFirst());

        if (healthRecords != null) {
            if (healthRecords.getScheduleDrugs() == null)
                healthRecords.setScheduleDrugs(new RealmList<ScheduleDrug>());

            healthRecords.getScheduleDrugs().add(scheduleDrug);

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(healthRecords);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    realm.close();
                    SaveScheduleModel.this.onSuccess();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(@NonNull Throwable error) {
                    realm.close();
                    SaveScheduleModel.this.onError();
                }
            });
        } else {
            realm.close();
            onError();
        }
    }

    @Override
    protected void exception(String message) {
        onError();
    }

    protected abstract void onSuccess();
    protected abstract void onError();
}