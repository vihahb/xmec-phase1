package com.xtelsolution.xmec.view.fragment.inf.share;

import android.support.v4.app.Fragment;

import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import io.realm.RealmList;

/**
 * Created by lecon on 12/1/2017
 */

public interface IBeSharedView extends IBasicView {

    void onGetSharedSuccess(RealmList<Mbr> realmList);
    void onGetSharedError(int code);
    void onDeleteSuccess(Mbr item);
    void onDeleteError();
    void onNoInternet();

    void showProgressBar(int type);

    Fragment getFragment();
}