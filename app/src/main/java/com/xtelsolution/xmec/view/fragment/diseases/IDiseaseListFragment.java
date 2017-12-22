package com.xtelsolution.xmec.view.fragment.diseases;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by vivu on 11/20/17.
 */

public interface IDiseaseListFragment extends IBasicView {

    Activity getActivity();

    void showLoading();

    void onGetDiseaseSuccess(List<DiseaseObject> diseases, String disease);

    void listEmpty(String s);

    void onHiddesnData();
    void onHiddesnDataAndShowWarning(String mesage, int resource);
}
