package com.xtelsolution.xmec.view.fragment.inf.login;

import android.support.v4.app.Fragment;

import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public interface IRegisterView extends IBasicView {

    /*
    * Success
    * */
    void onRegisterSuccess(String phone);
    void onActiveSuccess();

    /*
    * Error
    * */
    void onActiveError();

    /*
    * Other
    * */
//    void onActiveAccount(String phone);
    void showProgressRegister();
    Fragment getFragment();
}