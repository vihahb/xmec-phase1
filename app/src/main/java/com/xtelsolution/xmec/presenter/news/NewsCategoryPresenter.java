package com.xtelsolution.xmec.presenter.news;

import android.content.Context;

import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.entity.NewsPortsModel;
import com.xtelsolution.xmec.model.server.NewsPostCategoryModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.news.NewsPostsView;

import java.util.List;

import static com.xtelsolution.sdk.commons.Constants.ERROR_NO_INTERNET;

/**
 * Created by ThanhChung on 03/11/2017.
 */

public class NewsCategoryPresenter extends BasicPresenter {
    private NewsPostCategoryModel model;
    private NewsPostsView view;
    private Context context;

    public NewsCategoryPresenter(Context context, NewsPostsView basicView) {
        super(basicView);
        this.context = context;
        this.view = basicView;
        this.model = new NewsPostCategoryModel(context) {
            @Override
            protected void onSuccess(List<NewsPortsModel> newsList) {
                view.onSuccess(newsList);
            }

            @Override
            protected void onError(int errorCode) {
                view.onError(errorCode);
            }
        };
    }

    public void getData(String url) {
        view.loading();
        if (NetworkUtils.isConnected(context)) {
            model.setUrl(url);
            model.run();
        } else {
            view.onError(ERROR_NO_INTERNET);
        }
    }

    @Override
    protected void getSessionSuccess(Object... params) {


    }

}
