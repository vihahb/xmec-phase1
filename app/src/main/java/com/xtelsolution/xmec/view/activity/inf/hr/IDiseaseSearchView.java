package com.xtelsolution.xmec.view.activity.inf.hr;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.UserDiseases;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/23/2017
 * Email: leconglongvu@gmail.com
 */
public interface IDiseaseSearchView extends IBasicView {

    /*
    * Success
    * */
    void onGetDiseaseSuccess(List<UserDiseases> diseases, String key);

    /*
    * Other
    * */
    void showLoading();
    Activity getActivity();
}