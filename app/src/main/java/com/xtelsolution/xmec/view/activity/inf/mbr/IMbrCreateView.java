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
public interface IMbrCreateView extends IBasicView {

    /*
    * Success
    * */
    void onCreateSuccess(Mbr mbr);

    /*
    * Error
    * */

    /*
    * Other
    * */
    void showProgressBarCreate();
    void onDeleteFriend();
    List<Object> getFriendsList();
    Activity getActivity();
}