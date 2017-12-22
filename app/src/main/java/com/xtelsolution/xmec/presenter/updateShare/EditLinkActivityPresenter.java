package com.xtelsolution.xmec.presenter.updateShare;

import android.content.Intent;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.DeleteShare;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.entity.UpdateShare;
import com.xtelsolution.xmec.model.server.DeleteShareModel;
import com.xtelsolution.xmec.model.server.UpdateShareActionModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.IEditLinkView;

import io.realm.RealmList;

/**
 * Created by xtel on 11/16/17.
 */

public class EditLinkActivityPresenter extends BasicPresenter{

    private IEditLinkView view;

    public EditLinkActivityPresenter(IEditLinkView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(Object... params){
        switch ((int)params[0]){
            //Update
            case 1:
                final UpdateShare share = (UpdateShare) params[1];
                new UpdateShareActionModel(JsonHelper.toJson(share)) {
                    @Override
                    protected void onSuccess(RESP_Basic resp_basic) {
                        view.onUpdateSuccess(share);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };
                break;

            //Delete
            case 2:
                final DeleteShare deleteShare = (DeleteShare) params[1];
                final int mbr_id = (int) params[2];
                new DeleteShareModel(JsonHelper.toJson(deleteShare)){
                    @Override
                    protected void onSuccess() {
                        view.onDeleteSuccess();
                        //Get Mbr by id
                        new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbr_id) {
                            @Override
                            protected void onSuccess(final Mbr object) {
                                RealmList<ShareAccounts> shareAccounts = object.getShareAccounts();
                                for (int i = 0; i < shareAccounts.size(); i++) {
                                    if (shareAccounts.get(i).getId() == deleteShare.getId()) {
                                        shareAccounts.remove(i);
                                        break;
                                    }
                                }
                                object.setShareAccounts(shareAccounts);
                                new SaveObjectModel<Mbr>(object) {
                                    @Override
                                    protected void onSuccess() {
                                        if (view.getActivity() != null) {
                                            Intent intent = new Intent(Constants.ACTION_DELETE);
                                            intent.putExtra(Constants.MRB_ID, mbr_id);
                                            intent.putExtra(Constants.SHARE_ID, deleteShare.getId());
                                            intent.putExtra(Constants.TYPE, deleteShare.getType());
                                            view.getActivity().sendBroadcast(intent);
                                        }
                                        view.finishAction();
                                    }

                                    @Override
                                    protected void onError() {
                                        view.showLongToast("Có lỗi xảy ra. Vui lòng khởi động lại ứng dụng!");
                                    }
                                };
                            }
                        };
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };
                break;
        }
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

    public void updateShare(int share_id, int shareTo, int shareType){
        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.showLongToast("Vui lòng kiểm tra kết nối mạng và thử lại.");
            return;
        }
        UpdateShare share = new UpdateShare();
        share.setId(share_id);
        share.setShareType(shareType);
        share.setShareTo(shareTo);
        iCmd(1, share);
    }

    public void deleteLink(int id_share, int mbrID, int shareType){
        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.showLongToast("Vui lòng kiểm tra kết nối mạng và thử lại.");
            return;
        }
        DeleteShare deleteLink = new DeleteShare(id_share, shareType);
        iCmd(2, deleteLink, mbrID);
    }
}
