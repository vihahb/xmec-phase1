package com.xtelsolution.xmec.view.activity.inf.mbr;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public interface ISearchFriendView extends IBasicView {

    /*
    * Success
    * */
    void onGetFriendSuccess(List<Friends> friendsList);
    void onSearchSuccess(Friends friends);

    /*
    * Error
    * */

    /*
    * Other
    * */
    void onShowProgressBar();
    Activity getActivity();
}