package com.xtelsolution.xmec.view.activity.drug;

import android.content.Context;

import com.xtelsolution.xmec.model.entity.InfoDrug;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by xtel on 11/17/17.
 */

public interface IDrugInfoView extends IBasicView{

    void getCollectionSucces(List<InfoDrug> list);

    Context getActivity();

    void showWarning(String s);

    void showLoading();

    void getDrugSuccess(List<InfoDrug> infoDrugList);
}
