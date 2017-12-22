package com.xtelsolution.xmec.view.fragment.inf;

import android.support.v4.app.Fragment;

import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/1/2017
 * Email: leconglongvu@gmail.com
 */
public interface IDrugHrView extends IBasicView {

    void onGetUserDrugsSuccess(RealmList<UserDrugs> realmList);
    void onDeleteSuccess(UserDrugs userDrugs);

    void onCreatedDrug(UserDrugs userDrugs);
    void onUpdatedDrug(UserDrugs userDrugs);

    void showProgressBar(int type);

    Fragment getFragment();
}