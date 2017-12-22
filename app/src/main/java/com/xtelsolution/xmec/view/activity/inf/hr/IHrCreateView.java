package com.xtelsolution.xmec.view.activity.inf.hr;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/24/2017
 * Email: leconglongvu@gmail.com
 */
public interface IHrCreateView extends IBasicView {

    void onUploadSuccess(HealthRecordImages healthRecordImages);
    void onSearchHospitalSuccess(List<Hospital> hospitals, String key);
    void onCreateHrSuccess();
    void showProgressBar(int type);

    List<Object> getDiseaseList();
    List<Object> getImageList();
    Activity getActivity();
}