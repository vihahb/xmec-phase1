package com.xtelsolution.xmec.presenter.disease;

import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.model.server.GetDiseaseByIdModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.disease.IDiseaseDetailView;

/**
 * Created by vivu on 11/23/17.
 */

public class DiseaseDetailPresenter extends BasicPresenter{

    private IDiseaseDetailView view;

    public DiseaseDetailPresenter(IDiseaseDetailView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(Object... params){
        switch ((int)params[0]){
            case 1:
                int id = (int) params[1];
                new GetDiseaseByIdModel(id) {
                    @Override
                    protected void onSuccess(DiseaseObject object) {
                        view.getDiseaseSuccess(object);
                    }

                    @Override
                    protected void onErrror(int code) {
                        showError(code);
                    }
                };
                break;
        }
    }

    @Override
    protected void getSessionSuccess(Object... params) {

    }

    public void getDiseaseById(int id){
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(300);
            return;
        }
        iCmd(1, id);
    }
}
