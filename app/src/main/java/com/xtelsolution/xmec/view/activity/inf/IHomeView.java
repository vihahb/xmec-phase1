package com.xtelsolution.xmec.view.activity.inf;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Mbr;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/17/2017
 * Email: leconglongvu@gmail.com
 */
public interface IHomeView extends IBasicView{

    void onLogoutSuccess(boolean isSuccess);
    void onUpdateAvatarSuccess(String filePath);

    void setCount(int count);

    void showProgressView(int type);

    Activity getActivity();

    void addToMbrList(Mbr mbr);

    void saveCategorySuccess();

    void saveCategoryError();
}