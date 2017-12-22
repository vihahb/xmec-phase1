package com.xtelsolution.xmec.view.activity.hospitalInfo;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.RatingObject;
import com.xtelsolution.xmec.model.resp.RESP_Hospital_Info;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by vivu on 11/28/17
 * xtel.vn
 */

public interface IHospitalDetailView extends IBasicView {
    void getHospitalDetailSuccess(RESP_Hospital_Info info);
    void getHospitalDetailError(String mes);

    Activity getActivity();

    void closeProgress();

    void showProgress();

    void getRateSuccess(List<RatingObject> data);

    void emptyListRate(String s);
}
