package com.xtelsolution.xmec.presenter.Rate;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.RateObject;
import com.xtelsolution.xmec.model.server.AddRateModel;
import com.xtelsolution.xmec.model.server.UpdateRate;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.rate.IRatesView;

/**
 * Created by vivu on 11/29/17
 * xtel.vn
 */

public class RatePresenter extends BasicPresenter{

    private IRatesView view;

    public RatePresenter(IRatesView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(Object... params){
        switch ((int)params[0]){
            case 1:
                //Add rate
                RateObject object = (RateObject) params[1];
                new AddRateModel(object) {
                    @Override
                    protected void onSuccess(RESP_Basic respId) {
                        view.rateSuccess();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == 3){
                            view.showLongToast("Bạn đã đánh giá cơ sở y tế này.");
                        }
                        showError(errorCode);
                    }
                };
                break;

            case 2:
                //Update rate
                RateObject object_update = (RateObject) params[1];
                new UpdateRate(object_update) {
                    @Override
                    protected void onSuccess(RESP_Basic respId) {
                        view.updateSuccess();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };
                break;
        }
    }

    public void addRate(RateObject object){
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }
        iCmd(1, object);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

    public void updateRate(RateObject object) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }
        iCmd(2, object);
    }
}
