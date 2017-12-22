package com.xtelsolution.xmec.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.reminder.ReminderUtils;

import io.realm.RealmList;

/**
 * Created by lecon on 11/28/2017
 */
public class TimeChangedReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        if ("android.intent.action.TIME_SET".equals(intent.getAction())) {
            getAllHealthRecord();
        }
    }

    private void getAllHealthRecord() {
        new GetListByKeyModel<HealthRecords>(HealthRecords.class, null, 9) {
            @Override
            protected void onSuccess(RealmList<HealthRecords> realmList) {
                if (realmList != null) {
                    changeAlarm(realmList);
                }
            }
        };
    }

    private void changeAlarm(RealmList<HealthRecords> realmList) {
        for (HealthRecords healthRecords : realmList) {
            for (ScheduleDrug scheduleDrug : healthRecords.getScheduleDrugs()) {
                ReminderUtils.startService(context, scheduleDrug.getId());
            }
        }
    }
}