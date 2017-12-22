package com.xtelsolution.xmec.view.fragment.inf.login;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/14/2017
 * Email: leconglongvu@gmail.com
 */
public interface ILoginView extends IBasicView {

    /*
    * Success
    * */
    void onLoginSuccess();
    void onActiveSuccess();
    void onResetSuccess();

    /*
    * Error
    * */
    void onActiveError();
    void onValidateError();
    void onResetError();

    /*
    * Other
    * */
    void showProgressBarLogin();
    void showProgressBarActive();
    void showProgressResetPass();
    Fragment getFragment();

    Activity getActivity();
}