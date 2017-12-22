package com.xtelsolution.xmec.view.activity.inf;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public interface IBasicView {

    void onError(int code);

    void showLongToast(String message);

    void onEndSession();
}