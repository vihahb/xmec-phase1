package com.xtelsolution.xmec.presenter.mbr;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.req.REQ_Mbr_Create;
import com.xtelsolution.xmec.model.req.REQ_ShareLink;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;
import com.xtelsolution.xmec.model.server.MbrCreateModel;
import com.xtelsolution.xmec.model.server.ShareModel;
import com.xtelsolution.xmec.model.server.UploadAvatarModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.reminder.ReminderUtils;
import com.xtelsolution.xmec.view.activity.inf.mbr.IMbrCreateView;

import java.io.File;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class MbrCreatePresenter extends BasicPresenter {
    private IMbrCreateView view;

    private Mbr mbr;

    public MbrCreatePresenter(IMbrCreateView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

                File avatar = (File) params[1];
                String name = (String) params[2];
                int gender = (int) params[3];
                long birthDayLong = (long) params[4];
                int relationshipType = (int) params[5];

                final REQ_Mbr_Create yBaCreate = new REQ_Mbr_Create();
                yBaCreate.setName(name);
                yBaCreate.setGender(gender);
                yBaCreate.setBirthdayLong(birthDayLong);
                yBaCreate.setRelationshipType(relationshipType);

                if (avatar == null) {
                    iCmd(2, yBaCreate, yBaCreate.getAvatar());
                    return;
                }

                new UploadAvatarModel(avatar) {
                    @Override
                    protected void onSuccess(RESP_Upload_Image obj) {
                        yBaCreate.setAvatar(obj.getPath());

                        iCmd(2, yBaCreate, obj.getFull_path_server());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 2:

                final REQ_Mbr_Create yBaCreate2 = (REQ_Mbr_Create) params[1];
                final String fullPath = (String) params[2];

                new MbrCreateModel(JsonHelper.toJson(yBaCreate2)) {
                    @Override
                    protected void onSuccess(RESP_Id obj) {
                        createMbr(yBaCreate2, obj.getId(), fullPath);

                        if (view.getFriendsList().size() > 0) {
                            shareLink();
                            return;
                        }

                        iCmd(4);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode, params);
                    }
                };

                break;
            case 3:
                final Friends friends = (Friends) view.getFriendsList().get(0);

                REQ_ShareLink shareLink = new REQ_ShareLink();
                shareLink.setUid(friends.getUid());
                shareLink.setMrbId(mbr.getMrbId());
                shareLink.setShareType(2);
                shareLink.setShareTo(friends.getShareTo());

                new ShareModel(JsonHelper.toJson(shareLink)) {
                    @Override
                    protected void onSuccess(RESP_Id obj) {
                        mbr.getShareAccounts().add(new ShareAccounts(obj.getId(), friends, 2));
                        view.onDeleteFriend();
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
            case 4:

                new SaveObjectModel<Mbr>(mbr) {
                    @Override
                    protected void onSuccess() {
                        for (HealthRecords healthRecords : mbr.getHealthRecords()) {
                            for (ScheduleDrug scheduleDrug : healthRecords.getScheduleDrugs()) {
                                ReminderUtils.startService(view.getActivity().getApplicationContext(), scheduleDrug.getId());
                            }
                        }

                        view.onCreateSuccess(mbr);
                    }

                    @Override
                    protected void onError() {
                        view.onCreateSuccess(mbr);
                    }
                };

                break;
        }
    }

    public void createMbr(Object... params) {
        File avatar = (File) params[0];
        String name = (String) params[1];
        int gender = (int) params[2];
        String birthDay = (String) params[3];
        int relationshipType = (int) params[4];

        if (TextUtils.isEmpty(name)) {
            view.onError(301);
            return;
        }

        if (TextUtils.isEmpty(birthDay)) {
            view.onError(303);
            return;
        }

        long birthDayLong = TimeUtils.convertDateToLong(birthDay);

        if (birthDayLong > TimeUtils.convertDateToLong(TimeUtils.getToday())) {
            view.onError(306);
            return;
        }

        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.onError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBarCreate();

        iCmd(1, avatar, name, gender, birthDayLong, relationshipType);
    }

    private void createMbr(REQ_Mbr_Create reqMbrCreate, int id, String fullPath) {
        String avatar = SharedUtils.getInstance().getStringValue(Constants.USER_AVATAR);

        mbr = new Mbr();

        mbr.setMrbId(id);
        mbr.setName(reqMbrCreate.getName());
        mbr.setGender(reqMbrCreate.getGender());
        mbr.setAvatar(fullPath);
        mbr.setRelationshipType(reqMbrCreate.getRelationshipType());
        mbr.setBirthday(TimeUtils.convertLongToDate(reqMbrCreate.getBirthdayLong()));
        mbr.setBirthdayLong(reqMbrCreate.getBirthdayLong());
        mbr.setShareAccounts(new RealmList<ShareAccounts>());
        mbr.setHealthRecords(new RealmList<HealthRecords>());
        mbr.setCreatorAvatar(avatar);
    }

    public void shareLink() {
        if (view.getFriendsList().size() == 0 || mbr.getMrbId() == 0) {
            iCmd(4);
            return;
        }

        iCmd(3);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}