package com.xtelsolution.xmec.presenter.login;

import android.util.Log;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.model.resp.RESP_ImageUrl;
import com.xtelsolution.xmec.model.server.GetImageUrl;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;
import com.xtelsolution.xmec.view.activity.inf.ISlideView;

/**
 * Created by vivu on 12/14/17
 * xtel.vn
 */

public class SlidePresenter extends BasicPresenter {
    private static final String TAG = "SlidePresenter";
    private ISlideView view;

    public SlidePresenter(ISlideView basicView) {
        super(basicView);
        view = basicView;
    }

    @Override
    protected void getSessionSuccess(Object... params) {

    }

    public void getBaseUrl() {
        new GetImageUrl() {
            @Override
            protected void onSuccess(RESP_ImageUrl url) {
                Log.e(TAG, "onSuccess - save URL...: " + url.getUrl());
                SharedUtils.getInstance().putStringValue(Constants.BASE_IMAGE_URL, url.getUrl());
            }

            @Override
            protected void onErrror(int code) {

            }
        };
    }
}
