package com.xtelsolution.xmec.reminder.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.reminder.ReminderUtils;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/9/2017
 * Email: leconglongvu@gmail.com
 */
public class ReminderService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = getIntData(Constants.OBJECT, intent);
        int repeat = getIntData(Constants.OBJECT_2, intent);

        if (id != -1) {
            getSchedule(id, repeat);
        }

        return START_STICKY;
    }

    private void getSchedule(int id, final int repeat) {

        new GetObjectByKeyModel<ScheduleDrug>(ScheduleDrug.class, "id", id) {
            @Override
            protected void onSuccess(ScheduleDrug object) {
                if (object != null) {
                    ReminderUtils.startReminderSchedule(getApplicationContext(), object, repeat);
                }
            }
        };
    }

    private int getIntData(String key, Intent data) {
        try {
            return data.getIntExtra(key, -1);
        } catch (Exception e) {
            return -1;
        }
    }
}