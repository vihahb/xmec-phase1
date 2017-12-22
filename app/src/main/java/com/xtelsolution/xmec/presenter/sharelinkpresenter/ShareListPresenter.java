package com.xtelsolution.xmec.presenter.sharelinkpresenter;

import android.util.Log;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.resp.RESP_ListShareLinkMbr;
import com.xtelsolution.xmec.model.server.GetMbrByIdModel;
import com.xtelsolution.xmec.model.server.ShareLinkListModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.inf.shareAndLink.IShareListFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xtel on 11/14/17.
 */

public class ShareListPresenter extends BasicPresenter {

    IShareListFragmentView view;

    public ShareListPresenter(IShareListFragmentView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(final Object... params){
        switch ((int)params[0]){
            case 1:
                int mbrId_share = (int) params[1];
                new ShareLinkListModel(1, mbrId_share) {
                    @Override
                    protected void onSuccess(RESP_ListShareLinkMbr obj) {
                        view.setDataListShare(obj.getData());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        onError(errorCode);
                    }
                };
                break;

            case 2:
                final int mbr_id = (int) params[1];
                new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbr_id) {
                    @Override
                    protected void onSuccess(Mbr object) {
                        Log.e("GetObjectByKeyModel", "onSuccess: " + object.getShareAccounts().toString());
                        List<ShareAccounts> shareAccounts = new ArrayList<>();
                        shareAccounts.addAll(object.getShareAccounts());
                        Log.e("GetObjectByKeyModel", "shareAccounts: " + shareAccounts.toString());
                        view.getMbrSuccess(object);
                        getListShare(mbr_id);
                    }
                };
                break;

            case 3:
                int mbr_Id = (int) params[1];
                new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbr_Id) {
                    @Override
                    protected void onSuccess(Mbr object) {
                        List<ShareAccounts> shareAccounts = new ArrayList<>();
                        shareAccounts.addAll(object.getShareAccounts());
                        Log.e("GetObjectByKeyModel", "shareAccounts: " + shareAccounts.toString());
                        view.shareOffSuccess(shareAccounts);
                    }
                };
                break;
        }
    }

    public void getMbr(int mbr_id) {
        iCmd(2, mbr_id);
    }

    private void getListShare(int mbr){
        if (!NetworkUtils.isConnected(view.getContext())) {
            getShareLinkOff(mbr);
        } else {
            iCmd(1, mbr);
        }
    }

    private void getShareLinkOff(int mbr_id){
        iCmd(3, mbr_id);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}
