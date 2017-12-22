package com.xtelsolution.xmec.view.activity.inf.hr;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.UserDrugImages;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/26/2017
 * Email: leconglongvu@gmail.com
 */
public interface IUpdateDrugView extends IBasicView {

    void onGetDataSuccess(UserDrugs userDrugs);
    void onSearchSuccess(List<Drug> drugs, String key);
    void onUploadSuccess(UserDrugImages userDrugImages);
    void onUpdateSuccess();

    void showProgressBar(int type);

    UserDrugs getUserDrugs();
    List<Object> getListImage();
    Activity getActivity();
}