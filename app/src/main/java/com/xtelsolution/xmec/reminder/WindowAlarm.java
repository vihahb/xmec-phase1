package com.xtelsolution.xmec.reminder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;

/**
 * Author: Lê Công Long Vũ
 * Date: 9/28/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class WindowAlarm {

    private Context context;

    private View rootView;

    private Ringtone ringtoneAlarm;

    private ScheduleDrug scheduleDrug;

    private WindowManager windowManager;

    private LinearLayout layout1;
    private LinearLayout layout2;
    private LinearLayout layout3;
    private LinearLayout layout4;

    private RadioButton rb5;
    private RadioButton rb10;

    protected WindowAlarm(Context context, ScheduleDrug scheduleDrug) {
        this.context = context;
        this.scheduleDrug = scheduleDrug;

        initView();
        setActionListener();
        show();
    }

    @SuppressLint("InflateParams")
    private void initView() {
        LayoutInflater layoutInflater = LayoutInflater.from(context.getApplicationContext());
        rootView = layoutInflater.inflate(R.layout.dialog_reminder, null);

        layout1 = rootView.findViewById(R.id.layout_1);
        layout2 = rootView.findViewById(R.id.layout_2);
        layout3 = rootView.findViewById(R.id.layout_3);
        layout4 = rootView.findViewById(R.id.layout_4);

        AppCompatTextView txtName = rootView.findViewById(R.id.txt_name);
        AppCompatTextView txtTime = rootView.findViewById(R.id.txt_time);

        rb5 = rootView.findViewById(R.id.rb_5);
        rb10 = rootView.findViewById(R.id.rb_10);

        txtName.setText(scheduleDrug.getName());
        txtTime.setText((TimeUtils.getTtime() + " " + context.getString(R.string.layout_alarm_minute)));
    }

    private void setActionListener() {
        Button btnRepeat = rootView.findViewById(R.id.btn_repeat);
        Button btnDetail = rootView.findViewById(R.id.btn_detail);
        Button btnClose = rootView.findViewById(R.id.btn_close);
        Button btnCancel = rootView.findViewById(R.id.btn_cancel);
        Button btnDone = rootView.findViewById(R.id.btn_done);

        btnRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                layout3.setVisibility(View.GONE);
                layout4.setVisibility(View.VISIBLE);
            }
        });

        btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAlarm();
                onViewSchedule();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAlarm();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                layout3.setVisibility(View.VISIBLE);
                layout4.setVisibility(View.GONE);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAlarm();
                onRepeatSchedule(getRepeatSchedule());
            }
        });
    }

    public void show() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, //Disables status bar
                PixelFormat.TRANSPARENT); //Transparent

//        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
//                // Shrink the window to wrap the content rather than filling the screen
//                WindowManager.LayoutParams.MATCH_PARENT,
//                WindowManager.LayoutParams.MATCH_PARENT,
//                // Display it on top of other application windows, but only for the current user
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
//                // Don't let it grab the input focus
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//                // Make the underlying application window visible through any transparent parts
//                PixelFormat.TRANSLUCENT);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager != null) {
            windowManager.addView(rootView, params);
            startAlarm();
        }
    }

    private void startAlarm() {
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        ringtoneAlarm = RingtoneManager.getRingtone(context, uri);
        ringtoneAlarm.play();
    }

    private void closeAlarm() {
        if (ringtoneAlarm != null && ringtoneAlarm.isPlaying())
            ringtoneAlarm.stop();

        windowManager.removeView(rootView);
    }

    private int getRepeatSchedule() {
        if (rb5.isChecked())
            return 5;
        else if (rb10.isChecked())
            return 10;
        else
            return 15;
    }

    protected abstract void onRepeatSchedule(int repeat);
    protected abstract void onViewSchedule();
}