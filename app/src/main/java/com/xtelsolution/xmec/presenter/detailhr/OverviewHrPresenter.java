package com.xtelsolution.xmec.presenter.detailhr;

import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.view.fragment.inf.detailhr.IOverviewHrView;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/13/2017
 * Email: leconglongvu@gmail.com
 */
public class OverviewHrPresenter {
    private IOverviewHrView view;

    public OverviewHrPresenter(IOverviewHrView view) {
        this.view = view;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:

                int id = (int) params[1];

                new GetObjectByKeyModel<HealthRecords>(HealthRecords.class, "id", id) {
                    @Override
                    protected void onSuccess(HealthRecords healthRecords) {
                        view.onGetHealthRecordSuccess(healthRecords);
                    }
                };

                break;
        }
    }

    public void getHealthRecord(int id) {
        if (id == - 1)
            return;

        iCmd(1, id);
    }
}