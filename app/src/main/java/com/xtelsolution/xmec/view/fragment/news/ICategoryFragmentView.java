package com.xtelsolution.xmec.view.fragment.news;

import android.content.Context;

import com.xtelsolution.xmec.model.entity.CategoryListEntity;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by vivu on 12/13/17
 * xtel.vn
 */

public interface ICategoryFragmentView extends IBasicView {
    Context getContext();

    void setDataWarning(String s);
    void getCategoryListSuccess(List<CategoryListEntity> data);
}
