package com.xtelsolution.xmec.presenter.search;

import android.annotation.SuppressLint;
import android.util.Log;

import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.resp.RESP_SearchDrug;
import com.xtelsolution.xmec.model.server.GetDrugListSearch;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.search.IDrugSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NutIT on 01/11/2017.
 */

public class DrugSearchPresenter extends BasicPresenter {
    private static final String TAG = "DrugSearchPresenter";
    IDrugSearch view;

    public DrugSearchPresenter(IDrugSearch view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:
                final String key = (String) params[1];
                final int page = (int) params[2];
                new GetDrugListSearch(key, page, 20) {
                    @SuppressLint("LogConditional")
                    @Override
                    protected void onSuccess(RESP_SearchDrug searchDrug) {
                        if (page == 1 && searchDrug.getData().size() == 0) {
                            view.listEmpty("Không có thông tin về thuốc bạn tìm kiếm.");
                        } else {
                            view.onSearchSuccess(searchDrug.getData());
                        }
                        Log.d(TAG, "onSuccess:TestDrug " + searchDrug.getData().toString());
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (TextUtils.getErrorCode(code) != null){
                            view.onHiddenDataAndShowWarning(TextUtils.getErrorCode(code), -1);
                        } else {
                            view.onHiddenDataAndShowWarning("Có lỗi xảy ra.\nVui lòng thử lại!", -1);
                        }
                    }
                };
                break;
            case 2:

                break;
        }
    }

    public List<Drug> drug(Drug drug) {
        List<Drug> xxx = new ArrayList<>();
        xxx.add(drug);
        return xxx;
    }

    public void searchDrug(String drug, int page) {
        if (TextUtils.isEmpty(drug)) {
            showError(300);
            return;
        }
        if (!NetworkUtils.isConnected(view.getActivity())){
            view.onHiddenDataAndShowWarning("Không có kết nối internet.\nVui lòng kiểm tra lại cài đặt internet!", -1);
            return;
        }
        iCmd(1, drug, page);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

}
