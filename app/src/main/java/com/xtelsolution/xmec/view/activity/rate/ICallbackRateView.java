package com.xtelsolution.xmec.view.activity.rate;

/**
 * Created by vivu on 11/30/17
 * xtel.vn
 */

public interface ICallbackRateView {
    void onSaveService(float service);

    void onSaveHygiene(float Hygiene);

    void onSaveQuality(float quality);

    void onSaveComment(String comment);

    void onCloseDialog();

    void onPriviousDialog();
}
