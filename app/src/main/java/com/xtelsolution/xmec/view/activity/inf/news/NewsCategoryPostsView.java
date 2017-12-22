package com.xtelsolution.xmec.view.activity.inf.news;

import com.xtelsolution.xmec.model.entity.CategoryEntity;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

import io.realm.RealmList;

/**
 * Created by ThanhChung on 04/11/2017.
 */

public interface NewsCategoryPostsView extends IBasicView {
    void onSuccess(List<Fragments> list);

    void loading();
}
