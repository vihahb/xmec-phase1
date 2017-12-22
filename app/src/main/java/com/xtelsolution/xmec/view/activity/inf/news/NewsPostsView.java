package com.xtelsolution.xmec.view.activity.inf.news;

import com.xtelsolution.xmec.model.entity.NewsPortsModel;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;

import java.util.List;

/**
 * Created by ThanhChung on 04/11/2017.
 */

public interface NewsPostsView extends IBasicView {
    void onSuccess(List<NewsPortsModel> list);

    void loading();
}
