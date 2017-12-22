package com.xtelsolution.xmec.reminder.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.reminder.ReminderUtils;
import com.xtelsolution.xmec.reminder.WindowAlarm;
import com.xtelsolution.xmec.view.activity.ReminderActivity;
import com.xtelsolution.xmec.view.activity.schedule.DetailScheduleActivity;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/17/2017
 * Email: leconglongvu@gmail.com
 */
public class ReminderReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        int id = -1;
        long time = -1;

        try {
            id = intent.getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            time = intent.getLongExtra(Constants.OBJECT_2, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id != -1) {
            long checkTime = (System.currentTimeMillis() - time) / 1000;
            if ((checkTime / 60) < 5)
                getSchedule(id);

            ReminderUtils.startService(context, id);
        }
    }

    private void getSchedule(final int id) {

        new GetObjectByKeyModel<ScheduleDrug>(ScheduleDrug.class, "id", id) {
            @Override
            protected void onSuccess(ScheduleDrug object) {
                if (object != null)
                    reminderSchedule(object);
                else
                    ReminderUtils.cancelReminderSchedule(context, id);
            }
        };
    }

    private void reminderSchedule(final ScheduleDrug scheduleDrug) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {

                Intent intent = new Intent(context, ReminderActivity.class);
                intent.putExtra(Constants.OBJECT, scheduleDrug.getId());
                context.startActivity(intent);

                return;
            }
        }

        new WindowAlarm(context, scheduleDrug) {

            @Override
            protected void onRepeatSchedule(int repeat) {
                ReminderUtils.startService(context, scheduleDrug.getId(), repeat);
            }

            @Override
            protected void onViewSchedule() {
                Intent intent = new Intent(context, DetailScheduleActivity.class);
                intent.putExtra(Constants.OBJECT, scheduleDrug.getId());
                intent.putExtra(Constants.OBJECT_2, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        };
    }
}