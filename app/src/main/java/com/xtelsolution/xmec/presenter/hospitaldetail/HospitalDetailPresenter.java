package com.xtelsolution.xmec.presenter.hospitaldetail;

import android.util.Log;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.resp.RESP_Hospital_Info;
import com.xtelsolution.xmec.model.resp.RESP_ListRate;
import com.xtelsolution.xmec.model.server.GetRateHospital;
import com.xtelsolution.xmec.model.server.HospitalDetailModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.hospitalInfo.IHospitalDetailView;

/**
 * Created by vivu on 11/28/17
 * xtel.vn
 */

public class HospitalDetailPresenter extends BasicPresenter {
    private static final String TAG = "HospitalDetailPresenter";

    private IHospitalDetailView view;

    public HospitalDetailPresenter(IHospitalDetailView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(Object... params){
        switch ((int)params[0]){
            //Get detail Hospital
            case 1:
                view.showProgress();
                int id = (int) params[1];
                new HospitalDetailModel(id) {
                    @Override
                    protected void onSuccess(RESP_Hospital_Info hospital_info) {
                        view.getHospitalDetailSuccess(hospital_info);
                    }

                    @Override
                    protected void onErrror(int code) {
                        Log.e(TAG, "onErrror iCMD1: " + code);
                        showError(code);
                    }
                };
                break;

            case 2:
                //Get list Rate
                int id_hospital = (int) params[1];
                final int page = (int) params[2];
                int size = (int) params[3];
                new GetRateHospital(id_hospital, page, size) {
                    @Override
                    protected void onSuccess(RESP_ListRate listRate) {
                        view.closeProgress();
                        if (page == 1 && listRate.getData().size() == 0) {
                            view.emptyListRate("Không có đánh giá nào");
                        }
                        if (page > 1 && listRate.getData().size() == 0) {
                            view.showLongToast("Không có đánh giá nào mới hơn");
                        } else {
                            view.getRateSuccess(listRate.getData());
                        }
                    }

                    @Override
                    protected void onErrror(int code) {
                        view.closeProgress();
                        Log.e(TAG, "onErrrorCode: " + code);
//                        showError(code);
                    }
                };
                break;
        }
    }

    public void getHospitalById(int id){
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            view.closeProgress();
            return;
        }
        iCmd(1, id);
    }

    public void getRateHospitalById(int id, int page, int size) {
        view.showProgress();
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            view.closeProgress();
            return;
        }
        iCmd(2, id, page, size);
    }


    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}
