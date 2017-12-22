package com.xtelsolution.xmec.view.activity.inf;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.UpdateShare;

/**
 * Created by xtel on 11/15/17.
 */

public interface IEditShareView extends IBasicView{

    void onDeleteSuccess();
    void onUpdateSuccess(UpdateShare share);

    Activity getActivity();

    void finishAction();
}
