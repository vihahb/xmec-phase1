package com.xtelsolution.xmec.view.fragment.diseases;

import com.xtelsolution.xmec.model.entity.DiseaseObject;

/**
 * Created by vivu on 11/22/17.
 */

public interface OnItemCLick {
    void onClickItem(int position, DiseaseObject object);

    void onDeleteItem();
}
