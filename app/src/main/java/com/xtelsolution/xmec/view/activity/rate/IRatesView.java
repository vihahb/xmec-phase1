package com.xtelsolution.xmec.view.activity.rate;

import android.app.Activity;

import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Created by vivu on 11/29/17
 * xtel.vn
 */

public interface IRatesView extends IBasicView{
    void rateSuccess();

    Activity getActivity();

    void rateError(int errorCode);

    void updateSuccess();
}
