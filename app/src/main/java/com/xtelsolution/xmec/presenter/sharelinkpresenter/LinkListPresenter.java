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
import com.xtelsolution.xmec.view.fragment.inf.shareAndLink.ILinkListFragmentView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xtel on 11/14/17.
 */

public class LinkListPresenter extends BasicPresenter {

    private ILinkListFragmentView view;

    public LinkListPresenter(ILinkListFragmentView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(final Object... params){
        switch ((int)params[0]){
            case 1:
                int mbrId_link = (int) params[1];
                new ShareLinkListModel(2, mbrId_link) {
                    @Override
                    protected void onSuccess(RESP_ListShareLinkMbr obj) {
                        view.setDataListLink(obj.getData());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        onError(errorCode);
                    }
                };
                break;

            case 2:
                int mbr_Id = (int) params[1];
                new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbr_Id) {
                    @Override
                    protected void onSuccess(Mbr object) {
                        view.getMbrSuccess(object);
                    }
                };
                break;
            case 3:
                int mbrId = (int) params[1];
                new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbrId) {
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

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

    public void  getListLink(int mbr){
        if (!NetworkUtils.isConnected(view.getActivity())) {
            getShareLinkOff(mbr);
        } else {
            iCmd(1, mbr);
        }
    }

    public void getMbr(int mbr_id) {
        iCmd(2, mbr_id);
    }

    public void getShareLinkOff(int mbr_id){
        iCmd(3, mbr_id);
    }
}
