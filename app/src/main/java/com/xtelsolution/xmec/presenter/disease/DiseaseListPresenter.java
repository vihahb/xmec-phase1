package com.xtelsolution.xmec.presenter.disease;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.model.server.DiseaseSearchKey;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.diseases.IDiseaseListFragment;

import java.util.List;

/**
 * Created by vivu on 11/20/17.
 */

public class DiseaseListPresenter extends BasicPresenter {

    private IDiseaseListFragment view;

    public DiseaseListPresenter(IDiseaseListFragment basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

                final String disease = (String) params[1];
                final int page = (int) params[2];
                new DiseaseSearchKey(disease, page, 20) {
                    @Override
                    protected void onSuccess(List<DiseaseObject> diseases) {
                        if (page == 1 && diseases.size() == 0) {
                            view.listEmpty("Không có dữ liệu bệnh tìm kiếm");
                        } else {
                            view.onGetDiseaseSuccess(diseases, disease);
                        }
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

    public void searchDisease(String disease, int page) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.onHiddesnDataAndShowWarning("Không có kết nối internet.\nVui lòng kiểm tra lại cài đặt internet!", -1);
            return;
        }

        if (TextUtils.isEmpty(disease)) {
            showError(301);
            return;
        }

        disease = disease.replaceAll(" ", "%20");
        iCmd(1, disease, page);
    }


    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}
