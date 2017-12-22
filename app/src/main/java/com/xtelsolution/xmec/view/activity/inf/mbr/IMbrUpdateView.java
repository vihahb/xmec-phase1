package com.xtelsolution.xmec.view.activity.inf.mbr;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public interface IMbrUpdateView extends IBasicView {

    /*
    * Success
    * */
    void onGetMbrSuccess(Mbr mbr);
    void onUpdateSuccess();
    void onDeleteSuccess();

    /*
    * Error
    * */

    /*
    * Other
    * */
    void showProgressBar(int type);
    Mbr getMbr();
    List<Object> getFriendsList();
    Activity getActivity();
}