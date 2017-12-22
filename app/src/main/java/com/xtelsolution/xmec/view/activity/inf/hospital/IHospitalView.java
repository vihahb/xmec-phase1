package com.xtelsolution.xmec.view.activity.inf.hospital;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by ThanhChung on 08/11/2017.
 */

public interface IHospitalView extends IBasicView {

    void onSearchHospitalSuccess(List<Hospital> hospitalList, String key);

    void onGetHospitalAroundSuccess(List<Hospital> hospitalList);

    void showProgressBar(int type);

    Activity getActivity();
}
