package com.xtelsolution.xmec.presenter.hospital;

import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.model.server.GetHospitalAround;
import com.xtelsolution.xmec.model.server.SearchHospitalModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.hospital.IHospitalView;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by ThanhChung on 08/11/2017.
 */

public class HospitalPresenter extends BasicPresenter {
    private IHospitalView view;

    public HospitalPresenter(IHospitalView view) {
        super(view);
        this.view = view;
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

    private String keyOld = "";
    private int cPage = 1;

    public void setcPage(int cPage) {
        this.cPage = cPage;
    }

    public void searchHospital(String key, int page) {
        String keyEndcode = URLEncoder.encode(key);
        if (!keyOld.equals(key) || cPage != page) {
            iCmd(Constants.SEARCH_HOSPITALBY_KEY, keyEndcode, page);
            keyOld = key;
            cPage = page;
        }
    }

    public void getHospitalRadius(LatLng myLatLng, int type) {
        iCmd(Constants.SEARCH_HOSPITALBY_LATLNG, myLatLng, type);
    }

    private void iCmd(final Object... params) {

        if (!NetworkUtils.isConnected2(MyApplication.context)) {
            view.showProgressBar((int) params[0]);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.onError(Constants.ERROR_NO_INTERNET);
                }
            }, 500);
            return;
        }

        switch ((int) params[0]) {

            case Constants.SEARCH_HOSPITALBY_KEY:

                final String key = (String) params[1];
                new SearchHospitalModel(key, (int) params[2]) {
                    @Override
                    protected void onSuccess(List<Hospital> hospitals) {
                        view.onSearchHospitalSuccess(hospitals, key);
                    }

                    @Override
                    protected void onErrror(int code) {
//                        if (code == Constants.ERROR_SESSION)
//                            getNewSession(params);
                        showError(code, params);
                    }
                };

                break;
            case Constants.SEARCH_HOSPITALBY_LATLNG:
                LatLng latLng = (LatLng) params[1];
                if (latLng != null) {
                    view.showProgressBar(Constants.SEARCH_HOSPITALBY_LATLNG);
                    new GetHospitalAround(latLng, (int) params[2]) {
                        @Override
                        protected void onSuccess(List<Hospital> hospitals) {
                            view.onGetHospitalAroundSuccess(hospitals);
                        }

                        @Override
                        protected void onErrror(int code) {
                            showError(code, params);
                        }
                    };
                } else {
                    view.onError(Constants.ERROR_OFF_LOCATION);
                }
        }
    }


}
