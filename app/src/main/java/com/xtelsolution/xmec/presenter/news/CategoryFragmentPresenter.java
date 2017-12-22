package com.xtelsolution.xmec.presenter.news;

import android.util.Log;

import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.resp.RESP_CategoryListInfo;
import com.xtelsolution.xmec.model.server.GetNewsListFromCategory;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.news.ICategoryFragmentView;

/**
 * Created by vivu on 12/13/17
 * xtel.vn
 */

public class CategoryFragmentPresenter extends BasicPresenter {
    private ICategoryFragmentView view;
    private static final String TAG = "CategoryFragmentPresent";

    public CategoryFragmentPresenter(ICategoryFragmentView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            //Get new from category id
            case 1:
                int id = (int) params[1];
                int page = (int) params[2];
                new GetNewsListFromCategory(id, page, 15) {
                    @Override
                    protected void onSuccess(RESP_CategoryListInfo list) {
                        Log.e(TAG, "onSuccess: " + list.toString());
                        view.getCategoryListSuccess(list.getData());
                    }

                    @Override
                    protected void onErrror(int code) {
                        String error = TextUtils.getErrorCode(code);
                        view.setDataWarning(error);
                    }
                };
                break;
        }
    }

    public void getCategoryById(int id, int page){
        if (!NetworkUtils.isConnected2(view.getContext())){
            view.setDataWarning("Không có kết nối mạng. Vui lòng kiểm tra lại!");
            return;
        } else {
            iCmd(1, id, page);
        }
    }

    @Override
    protected void getSessionSuccess(Object... params) {

    }
}
