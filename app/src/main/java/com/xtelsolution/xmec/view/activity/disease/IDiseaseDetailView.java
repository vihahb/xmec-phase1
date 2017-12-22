package com.xtelsolution.xmec.view.activity.disease;

import android.app.Activity;

import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

/**
 * Created by vivu on 11/23/17.
 */

public interface IDiseaseDetailView extends IBasicView{
    void getDiseaseSuccess(DiseaseObject object);

    Activity getActivity();
}
