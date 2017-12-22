package com.xtelsolution.xmec.presenter.mbr;

import android.util.SparseBooleanArray;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.DeleteObjectByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.req.REQ_Id;
import com.xtelsolution.xmec.model.req.REQ_Mbr_Update;
import com.xtelsolution.xmec.model.req.REQ_ShareLink;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;
import com.xtelsolution.xmec.model.server.DeleteMbrModel;
import com.xtelsolution.xmec.model.server.DeleteShareModel;
import com.xtelsolution.xmec.model.server.MbrUpdateModel;
import com.xtelsolution.xmec.model.server.ShareModel;
import com.xtelsolution.xmec.model.server.UploadAvatarModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.mbr.IMbrUpdateView;

import java.io.File;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class MbrUpdatePresenter extends BasicPresenter {
    private IMbrUpdateView view;

    private SparseBooleanArray arrayShare;

    private RealmList<ShareAccounts> realmList;

    public MbrUpdatePresenter(IMbrUpdateView view) {
        super(view);
        this.view = view;

        arrayShare = new SparseBooleanArray();
        realmList = new RealmList<>();
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 0:

                int mbrID = (int) params[1];

                new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbrID) {
                    @Override
                    protected void onSuccess(Mbr object) {
                        view.onGetMbrSuccess(object);
                    }
                };

                break;
            case 1:

                File avatar = (File) params[1];

                if (avatar == null) {
                    iCmd(2, null);
                    return;
                }

                new UploadAvatarModel(avatar) {
                    @Override
                    protected void onSuccess(RESP_Upload_Image obj) {
                        view.getMbr().setAvatar(obj.getPath());

                        iCmd(2, obj.getFull_path_server());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 2:

                final String fullPath = (String) params[1];

                REQ_Mbr_Update mbrUpdate = new REQ_Mbr_Update();

                mbrUpdate.setMrbId(view.getMbr().getMrbId());
                mbrUpdate.setName(view.getMbr().getName());
                mbrUpdate.setGender(view.getMbr().getGender());
                mbrUpdate.setBirthdayLong(view.getMbr().getBirthdayLong());
                mbrUpdate.setRelationshipType(view.getMbr().getRelationshipType());

                if (fullPath != null)
                    mbrUpdate.setAvatar(view.getMbr().getAvatar());

                new MbrUpdateModel(JsonHelper.toJson(mbrUpdate)) {
                    @Override
                    protected void onSuccess() {
                        if (fullPath != null)
                            view.getMbr().setAvatar(fullPath);

                        separateShare();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 3:

                final ShareAccounts shareAccounts = view.getMbr().getShareAccounts().get(0);

                if (arrayShare.get(shareAccounts.getId())) {
                    realmList.add(shareAccounts);
                    view.getMbr().getShareAccounts().remove(0);
                    iCmd(7, shareAccounts.getId());
                    return;
                }

                final REQ_Id reqId = new REQ_Id(shareAccounts.getId());

                new DeleteShareModel(JsonHelper.toJson(reqId)) {
                    @Override
                    protected void onSuccess() {

                        view.getMbr().getShareAccounts().remove(0);

                        iCmd(7, reqId.getId());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            showError(errorCode, params);
                        else
                            showError(305);
                    }
                };

                break;
            case 4:

                final ShareAccounts shareAccounts4 = (ShareAccounts) view.getFriendsList().get(0);

                if (arrayShare.get(shareAccounts4.getId())) {
                    realmList.add(shareAccounts4);
                    view.getFriendsList().remove(0);
                    shareLink();
                    return;
                }

                REQ_ShareLink shareLink = new REQ_ShareLink();
                shareLink.setUid(shareAccounts4.getUid());
                shareLink.setMrbId(view.getMbr().getMrbId());
                shareLink.setShareType(2);
                shareLink.setShareTo(shareAccounts4.getShareTo());

                new ShareModel(JsonHelper.toJson(shareLink)) {
                    @Override
                    protected void onSuccess(RESP_Id obj) {

                        shareAccounts4.setId(obj.getId());
                        realmList.add(shareAccounts4);
                        view.getFriendsList().remove(0);

                        shareLink();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            showError(errorCode, params);
                        else
                            showError(305);
                    }
                };

                break;
            case 5:

                final int mbrID5 = view.getMbr().getMrbId();

                new DeleteMbrModel(mbrID5) {

                    @Override
                    protected void onSuccess(RESP_Basic obj) {
                        iCmd(6, mbrID5);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 6:

                int mbrID6 = (int) params[1];

                new DeleteObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbrID6) {
                    @Override
                    protected void onSuccess() {
                        view.onDeleteSuccess();
                    }

                    @Override
                    protected void onError() {
                        view.onDeleteSuccess();
                    }
                };

                break;
            case 7:

                int id7 = (int) params[1];

                new DeleteObjectByKeyModel<ShareAccounts>(ShareAccounts.class, "id", id7) {
                    @Override
                    protected void onSuccess() {
                        deleteLink();
                    }

                    @Override
                    protected void onError() {
                        deleteLink();
                    }
                };

                break;
            case 8:

                view.getMbr().getShareAccounts().clear();
                view.getMbr().getShareAccounts().addAll(realmList);

                new SaveObjectModel<Mbr>(view.getMbr()) {
                    @Override
                    protected void onSuccess() {
                        view.onUpdateSuccess();
                    }

                    @Override
                    protected void onError() {
                        view.onUpdateSuccess();
                    }
                };

                break;
        }
    }

    public void getMbr(int mbrID) {
        iCmd(0, mbrID);
    }

    public void updateMbr(Object... params) {
        File avatar = (File) params[0];
        Mbr mbr = (Mbr) params[1];

        if (TextUtils.isEmpty(mbr.getName())) {
            view.onError(301);
            return;
        }

        if (mbr.getBirthdayLong() == 0) {
            view.onError(303);
            return;
        }

        if (mbr.getBirthdayLong() > System.currentTimeMillis()) {
            view.onError(306);
            return;
        }

        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.onError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(1);

        iCmd(1, avatar);
    }

    public void deleteMbr() {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBar(2);

        iCmd(5);
    }

    public void separateShare() {

        for (Object item : view.getFriendsList()) {
            ShareAccounts shareAccounts = (ShareAccounts) item;

            if (shareAccounts.getId() != -1) {
                arrayShare.put(shareAccounts.getId(), true);
            }
        }

        for (int i = view.getMbr().getShareAccounts().size() - 1; i >= 0; i--) {
            ShareAccounts shareAccounts = view.getMbr().getShareAccounts().get(i);

            if (arrayShare.get(shareAccounts.getId())) {
                realmList.add(0, shareAccounts);
                view.getMbr().getShareAccounts().remove(shareAccounts);
            }
        }

        deleteLink();
    }

    private void deleteLink() {
        if (view.getMbr().getShareAccounts().size() == 0) {

            for (int i = view.getFriendsList().size() - 1; i >= 0; i--) {
                ShareAccounts shareAccounts = (ShareAccounts) view.getFriendsList().get(i);

                if (arrayShare.get(shareAccounts.getId())) {
                    view.getFriendsList().remove(i);
                }
            }

            shareLink();
            return;
        }

        iCmd(3);
    }

    private void shareLink() {
        if (view.getFriendsList().size() == 0) {
            iCmd(8);
            return;
        }

        iCmd(4);
    }

    public void addShareAccount(ShareAccounts shareAccounts) {
        realmList.add(shareAccounts);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}