package com.xtelsolution.xmec.presenter;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.model.server.GetSessionModel;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class BasicPresenter {

    private IBasicView basicView;

    public BasicPresenter(IBasicView basicView) {
        this.basicView = basicView;
    }

    protected void showError(int errorCode, Object... params) {
        if (errorCode == Constants.ERROR_SESSION)
            getNewSession(params);
        else
            basicView.onError(errorCode);
    }

    protected void getNewSession(final Object... params) {

        new GetSessionModel() {
            @Override
            protected void onSuccess() {
                getSessionSuccess(params);
            }

            @Override
            protected void onErrror() {
                basicView.onError(Constants.ERROR_SESSION);
                basicView.onEndSession();
            }
        };

    }

    protected abstract void getSessionSuccess(Object... params);
}