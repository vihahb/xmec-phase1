package com.xtelsolution.xmec.presenter.detailhr;

import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.view.activity.inf.detailhr.IDetailHrView;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/13/2017
 * Email: leconglongvu@gmail.com
 */
public class DetailHrPresenter {
    private IDetailHrView view;

    public DetailHrPresenter(IDetailHrView view) {
        this.view = view;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:

                int id = (int) params[1];

                new GetObjectByKeyModel<HealthRecords>(HealthRecords.class, "id", id) {
                    @Override
                    protected void onSuccess(HealthRecords healthRecords) {

                        if (healthRecords == null)
                            view.onGetHrSuccess(null);
                        else
                            iCmd(2, healthRecords);
                    }
                };

                break;
            case 2:

                final HealthRecords healthRecords = (HealthRecords) params[1];

                new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", healthRecords.getMrbId()) {
                    @Override
                    protected void onSuccess(Mbr mbr) {
                        if (mbr == null)
                            view.onGetHrSuccess(null);
                        else {
                            if (mbr.getShareType() == 1 || (mbr.getShareType() == 2 && mbr.getShareTo() == 1)) {
                                healthRecords.setEndDateLong(1);
                            }

                            view.onGetHrSuccess(healthRecords);
                        }
                    }
                };

                break;
        }
    }

    public void getHealthRecord(int id) {
        iCmd(1, id);
    }
}