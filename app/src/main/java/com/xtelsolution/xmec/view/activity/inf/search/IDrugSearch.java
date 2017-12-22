package com.xtelsolution.xmec.view.activity.inf.search;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.DrugSearchEntity;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by NutIT on 02/11/2017.
 */

public interface IDrugSearch extends IBasicView {
    void onSearchSuccess(List<DrugSearchEntity> list);
    void onProgressBarLoading();
    void onHiddenData();
    void onHiddenDataAndShowWarning(String message, int resource);
    Activity getActivity();

    void listEmpty(String s);
}
