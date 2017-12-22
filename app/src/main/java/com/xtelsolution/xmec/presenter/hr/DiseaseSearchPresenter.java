package com.xtelsolution.xmec.presenter.hr;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.entity.UserDiseases;
import com.xtelsolution.xmec.model.server.SearchDiseasesModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.hr.IDiseaseSearchView;

import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/23/2017
 * Email: leconglongvu@gmail.com
 */
public class DiseaseSearchPresenter extends BasicPresenter {
    private IDiseaseSearchView view;

    public DiseaseSearchPresenter(IDiseaseSearchView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

                final String disease = (String) params[1];

                new SearchDiseasesModel(disease, 1, 3) {
                    @Override
                    protected void onSuccess(List<UserDiseases> diseases) {
                        view.onGetDiseaseSuccess(diseases, disease);
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (code == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(404, params);
                    }
                };

                break;
        }
    }

    public void searchDisease(String disease) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(300);
            return;
        }

        if (TextUtils.isEmpty(disease)) {
            showError(301);
            return;
        }

        disease = TextUtils.unicodeToKoDauLowerCase(disease);
        disease = disease.replaceAll(" ", "%20");

        view.showLoading();
        iCmd(1, disease);
    }


    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}