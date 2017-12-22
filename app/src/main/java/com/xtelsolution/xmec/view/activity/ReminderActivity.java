package com.xtelsolution.xmec.view.activity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.reminder.ReminderUtils;
import com.xtelsolution.xmec.view.activity.schedule.DetailScheduleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReminderActivity extends AppCompatActivity {

    @BindView(R.id.txt_name)
    AppCompatTextView txtName;
    @BindView(R.id.txt_time)
    AppCompatTextView txtTime;
    @BindView(R.id.layout_1)
    LinearLayout layout1;
    @BindView(R.id.rb_5)
    RadioButton rb5;
    @BindView(R.id.rb_10)
    RadioButton rb10;
    @BindView(R.id.layout_2)
    LinearLayout layout2;
    @BindView(R.id.layout_3)
    LinearLayout layout3;
    @BindView(R.id.layout_4)
    LinearLayout layout4;

    private Ringtone ringtoneAlarm;
    private ScheduleDrug scheduleDrug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reminder);
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this);

        checkData();
    }

    private void checkData() {
        int id = -1;

        try {
            id = getIntent().getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id != -1) {
            getSchedule(id);
        } else {
            finish();
        }
    }

    private void getSchedule(int id) {
        new GetObjectByKeyModel<ScheduleDrug>(ScheduleDrug.class, "id", id) {
            @Override
            protected void onSuccess(ScheduleDrug object) {
                if (object != null) {
                    scheduleDrug = object;
                    initData();
                    startAlarm();
                } else {
                    finish();
                }
            }
        };
    }

    private void initData() {
        txtName.setText(scheduleDrug.getName());
        txtTime.setText((TimeUtils.getTtime() + " " + getString(R.string.layout_alarm_minute)));
    }

    private void startAlarm() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        ringtoneAlarm = RingtoneManager.getRingtone(this, uri);
        ringtoneAlarm.play();
    }

    private void closeAlarm() {
        if (ringtoneAlarm != null && ringtoneAlarm.isPlaying())
            ringtoneAlarm.stop();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finishAffinity();
        }
    }

    private int getRepeatSchedule() {
        if (rb5.isChecked())
            return 5;
        else if (rb10.isChecked())
            return 10;
        else
            return 15;
    }

    private void onViewSchedule() {
        Intent intent = new Intent(this, DetailScheduleActivity.class);
        intent.putExtra(Constants.OBJECT, scheduleDrug.getId());
        intent.putExtra(Constants.OBJECT_2, true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick({R.id.btn_repeat, R.id.btn_detail, R.id.btn_close, R.id.btn_cancel, R.id.btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_repeat:
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_detail:
                closeAlarm();
                onViewSchedule();
                break;
            case R.id.btn_close:
                closeAlarm();
                break;
            case R.id.btn_cancel:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
                break;
            case R.id.btn_done:
                ReminderUtils.startService(this, scheduleDrug.getId(), getRepeatSchedule());
                closeAlarm();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeAlarm();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        closeAlarm();
    }
}