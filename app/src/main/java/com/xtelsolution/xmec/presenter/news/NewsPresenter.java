package com.xtelsolution.xmec.presenter.news;

import android.content.Context;
import android.util.Log;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.entity.CategoryEntity;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.model.entity.NewCategory;
import com.xtelsolution.xmec.model.entity.NewsPortsModel;
import com.xtelsolution.xmec.model.resp.RESP_CategoryList;
import com.xtelsolution.xmec.model.server.GetCategoryNewsfeed;
import com.xtelsolution.xmec.model.server.NewsPostCategoryModel;
import com.xtelsolution.xmec.model.server.NewsPostLoadModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.news.NewsCategoryPostsView;
import com.xtelsolution.xmec.view.activity.inf.news.NewsPostsView;
import com.xtelsolution.xmec.view.fragment.news.CategoryFragment;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

import static com.xtelsolution.sdk.commons.Constants.ERROR_NO_INTERNET;

/**
 * Created by ThanhChung on 03/11/2017.
 */

public class NewsPresenter extends BasicPresenter {

    private static final String TAG = "NewsPresenter";
    private NewsCategoryPostsView view;
    private Context context;

    public NewsPresenter(Context context, NewsCategoryPostsView basicView) {
        super(basicView);
        this.context = context;
        this.view = basicView;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:
                new GetCategoryNewsfeed() {
                    @Override
                    protected void onSuccess(RESP_CategoryList object) {

                        List<Fragments> list = new ArrayList<>();

                        for (int i = 0; i < object.getData().size(); i++) {
                            list.add(
                                    new Fragments(CategoryFragment.newInstance(object.getData().get(i).getId()),
                                            object.getData().get(i).getCategoryName())
                            );
                        }

                        view.onSuccess(list);
                    }

                    @Override
                    protected void onErrror(int code) {
                        MyApplication.log(TAG, "" + TextUtils.getErrorCode(code));
                    }
                };
                break;

        }
    }

    public void getData() {
        boolean category_saved = SharedUtils.getInstance().getBooleanValue(Constants.CATEGORY_SAVE);
        if (category_saved) {
            new GetListByKeyModel<CategoryEntity>(CategoryEntity.class, null, -1) {
                @Override
                protected void onSuccess(RealmList<CategoryEntity> realmList) {
                    List<Fragments> list = new ArrayList<>();
                    for (int i = 0; i < realmList.size(); i++) {
                        list.add(
                                new Fragments(CategoryFragment.newInstance(realmList.get(i).getId()),
                                        realmList.get(i).getCategoryName())
                        );
                    }
                    view.onSuccess(list);
                }
            };
        } else {
            if (NetworkUtils.isConnected(context)) {
                iCmd(1);
            } else {
                view.onError(ERROR_NO_INTERNET);
            }
        }
    }

    @Override
    protected void getSessionSuccess(Object... params) {


    }

}
