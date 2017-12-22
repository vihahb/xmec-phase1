package com.xtelsolution.xmec.view.activity.share;

import android.app.Activity;

import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Created by vivu on 11/25/17
 * xtel.vn
 */

public interface IShareLinkView extends IBasicView {
    void saveContactsSuccess();
    void saveContactsError(String message);

    Activity getActivity();
}
