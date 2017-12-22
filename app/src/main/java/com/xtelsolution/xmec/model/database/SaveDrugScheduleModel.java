package com.xtelsolution.xmec.model.database;

import android.support.annotation.NonNull;

import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;

import io.realm.Realm;
import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/13/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class SaveDrugScheduleModel extends AbsICmd {
    private Realm realm;

    private ScheduleDrug scheduleDrug;
    private UserDrugSchedule userDrugSchedule;

    public SaveDrugScheduleModel(UserDrugSchedule userDrugSchedule) {
        this.userDrugSchedule = userDrugSchedule;

        initRealm();
        run();
    }

    private void initRealm() {
        realm = Realm.getDefaultInstance();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void invoke() {
        scheduleDrug = realm.copyFromRealm(realm.where(ScheduleDrug.class).equalTo("id", userDrugSchedule.getScheduleId()).findFirst());

        if (scheduleDrug != null) {
            if (scheduleDrug.getUserDrugSchedules() == null)
                scheduleDrug.setUserDrugSchedules(new RealmList<UserDrugSchedule>());

            scheduleDrug.getUserDrugSchedules().add(userDrugSchedule);

            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm bgRealm) {
                    bgRealm.copyToRealmOrUpdate(scheduleDrug);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    realm.close();
                    SaveDrugScheduleModel.this.onSuccess();
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(@NonNull Throwable error) {
                    realm.close();
                    SaveDrugScheduleModel.this.onError();
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