package com.xtelsolution.xmec.view.activity.inf.account;

import android.app.Activity;

import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.io.File;

/**
 * Created by lecon on 11/27/2017
 */

public interface IAccountView extends IBasicView {

    void onUploadAvatarSuccess(File file);
    void onUpdateInfoSuccess(String filePath);

    void showProgressView(int type);

    Activity getActivity();
}