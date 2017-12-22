package com.xtelsolution.xmec.view.activity.inf;

import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.DrugSearchEntity;

/**
 * Created by NutIT on 07/11/2017.
 */

public interface IClickItem {
    void onClickItem(int position, DrugSearchEntity drug);

    void onDeleteItem(int position, DrugSearchEntity drug);
}
