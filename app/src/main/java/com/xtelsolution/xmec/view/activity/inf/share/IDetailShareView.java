package com.xtelsolution.xmec.view.activity.inf.share;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Created by lecon on 12/6/2017
 */

public interface IDetailShareView extends IBasicView {

    void onGetMbrSuccess(Mbr mbr);

    void onErrorInternet();

    void showProgressBar();

    Activity getActivity();
}
