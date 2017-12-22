package com.xtelsolution.xmec.view.fragment.inf.mbr;

import android.support.v4.app.Fragment;

import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/19/2017
 * Email: leconglongvu@gmail.com
 */
public interface IMbrView extends IBasicView {
    /*
    * Succcess
    * */
    void onGetSuccess(List<Mbr> mbrList);

    void onAddMbrSuccess(Mbr mbr);
    void onDeleteHRSuccess(HealthRecords healthRecords);
    void onCreatedHealthRecord(HealthRecords healthRecords);
    void onUpdatedHealthRecord(HealthRecords healthRecords);
    void onEndOfTreatmentSuccess(HealthRecords healthRecords);
    void onContinueTreatmentSuccess(HealthRecords healthRecords);
    void onDeleteShareSuccess();

    void showProgressBar(int type);
    void onGetMbrError(int code);

    Fragment getFragment();
}