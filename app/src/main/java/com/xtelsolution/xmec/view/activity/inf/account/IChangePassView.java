package com.xtelsolution.xmec.view.activity.inf.account;

import android.app.Activity;

import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Created by lecon on 11/28/2017
 */

public interface IChangePassView extends IBasicView {

    void onChangeSuccess();
    void showProgressView();

    Activity getActivity();
}