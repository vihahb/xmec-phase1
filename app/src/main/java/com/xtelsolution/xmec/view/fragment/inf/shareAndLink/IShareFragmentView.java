package com.xtelsolution.xmec.view.fragment.inf.shareAndLink;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by xtel on 11/13/17.
 */

public interface IShareFragmentView extends IBasicView {
    Activity getActivity();

    void onShowProgressBar();

    void onGetFriendSuccess(List<Friends> data);

    void onSearchSuccess(Friends friends);

    void shareToSuccess();

    void saveObjectSuccess();
}
