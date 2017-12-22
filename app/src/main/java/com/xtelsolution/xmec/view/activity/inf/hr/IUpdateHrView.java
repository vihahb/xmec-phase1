package com.xtelsolution.xmec.view.activity.inf.hr;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/1/2017
 * Email: leconglongvu@gmail.com
 */
public interface IUpdateHrView extends IBasicView {

    void onGetHealthRecordSuccess(HealthRecords healthRecords);
    void onSearchHospitalSuccess(List<Hospital> hospitalList, String key);
    void onUploadSuccess(HealthRecordImages healthRecordImages);
    void onUpdateSuccess();

    void showProgressBar(int type);

    HealthRecords getHealthRecord();
    List<Object> getDiseaseList();
    List<Object> getImageList();
    Activity getActivity();
}