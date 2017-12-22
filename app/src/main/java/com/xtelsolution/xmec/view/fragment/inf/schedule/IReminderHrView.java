package com.xtelsolution.xmec.view.fragment.inf.schedule;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/7/2017
 * Email: leconglongvu@gmail.com
 */
public interface IReminderHrView extends IBasicView {

    void onUpdateSuccess(ScheduleDrug scheduleDrug);
    void onUpdateTimeSuccess(ScheduleDrug scheduleDrug);
    void onUpdateTimeError(ScheduleDrug scheduleDrug);
    void onDeleteSuccess(int position);
    void onGetUserSchedule(ScheduleDrug scheduleDrug, int type);

    void showProgressBar(int type);
    RecyclerView getRecyclerview();
    Fragment getFragment();
}