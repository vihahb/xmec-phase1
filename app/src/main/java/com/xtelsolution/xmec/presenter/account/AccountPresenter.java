package com.xtelsolution.xmec.presenter.account;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.req.REQ_User;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;
import com.xtelsolution.xmec.model.server.UpdateUserModel;
import com.xtelsolution.xmec.model.server.UploadUserAvatarModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.account.IAccountView;

import java.io.File;

/**
 * Created by lecon on 11/27/2017
 */
public class AccountPresenter extends BasicPresenter {
    private IAccountView view;

    private String avatar;
    private String filePath;

    public AccountPresenter(IAccountView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

                final File file = (File) params[1];

                new UploadUserAvatarModel(file) {
                    @Override
                    protected void onSuccess(RESP_Upload_Image obj) {
                        avatar = obj.getFull_path_server();
                        filePath = file.getPath();
                        view.onUploadAvatarSuccess(file);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION) {
                            getSessionSuccess(params);
                        } else {
                            view.onUploadAvatarSuccess(null);
                        }
                    }
                };

                break;
            case 2:

                final REQ_User reqUser = (REQ_User) params[1];

                new UpdateUserModel(JsonHelper.toJson(reqUser)) {
                    @Override
                    protected void onSuccess() {
                        SharedUtils.getInstance().putStringValue(Constants.USER_FULLNAME, reqUser.getFullname());

                        if (avatar != null)
                            SharedUtils.getInstance().putStringValue(Constants.USER_AVATAR, avatar);

                        view.onUpdateInfoSuccess(filePath);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;
        }
    }

    public void uploadImage(File file) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressView(1);
        iCmd(1, file);
    }

    public void updateAccount(String name) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        if (TextUtils.isEmpty(name)) {
            showError(301);
            return;
        }

        int uid = SharedUtils.getInstance().getIntValue(Constants.USER_UID);
        String fullname = name;
        int gender = SharedUtils.getInstance().getIntValue(Constants.USER_GENDER);
        long birthdayLong = SharedUtils.getInstance().getLongValue(Constants.USER_BIRTHDAYLONG);
        String phonenumber = SharedUtils.getInstance().getStringValue(Constants.USER_PHONENUMBER);
        String address = SharedUtils.getInstance().getStringValue(Constants.USER_ADDRESS);
        double weight = SharedUtils.getInstance().getLongValue(Constants.USER_WEIGHT).doubleValue();
        double height = SharedUtils.getInstance().getLongValue(Constants.USER_HEIGHT).doubleValue();

        REQ_User reqUser = new REQ_User(uid, fullname, gender, birthdayLong,
                phonenumber, address, weight, height, avatar);

        view.showProgressView(2);
        iCmd(2, reqUser);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}