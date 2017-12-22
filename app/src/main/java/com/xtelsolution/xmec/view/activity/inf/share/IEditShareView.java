package com.xtelsolution.xmec.view.activity.inf.share;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Created by lecon on 12/8/2017
 */

public interface IEditShareView extends IBasicView {

    void onSetData(Mbr mbr);
    void onGetDataError();

    Activity getActivity();
}