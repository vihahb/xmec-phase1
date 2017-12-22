package com.xtelsolution.xmec.presenter.account;

import com.google.gson.annotations.SerializedName;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.req.REQ_Pass;
import com.xtelsolution.xmec.model.server.UpdatePassModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.account.IChangePassView;

/**
 * Created by lecon on 11/28/2017
 */
public class ChangePassPresenter extends BasicPresenter {
    private IChangePassView view;

    public ChangePassPresenter(IChangePassView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:

                REQ_Pass reqPass = (REQ_Pass) params[1];

                new UpdatePassModel(JsonHelper.toJson(reqPass)) {
                    @Override
                    protected void onSuccess() {
                        view.onChangeSuccess();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;
        }
    }

    public void changePass(Object... params) {
        String pass_old = (String) params[0];
        String pass_new = (String) params[1];
        String pass_again = (String) params[2];

        if (pass_old.isEmpty()) {
            showError(301);
            return;
        }

        if (pass_new.isEmpty()) {
            showError(302);
            return;
        }

        if (pass_again.isEmpty()) {
            showError(303);
            return;
        }

        if (!TextUtils.checkPass(pass_old)) {
            showError(304);
            return;
        }

        if (!TextUtils.checkPass(pass_new)) {
            showError(305);
            return;
        }

        if (!TextUtils.checkPass(pass_again)) {
            showError(306);
            return;
        }

        if (!pass_again.equals(pass_new)) {
            showError(307);
            return;
        }

        int uid = SharedUtils.getInstance().getIntValue(Constants.USER_UID);
        String username = SharedUtils.getInstance().getStringValue(Constants.USER_PHONENUMBER);

        REQ_Pass reqPass = new REQ_Pass();
        reqPass.setId(uid);
        reqPass.setUsername(username);
        reqPass.setOldPassword(pass_old);
        reqPass.setPassword(pass_new);
        reqPass.setType(1);

        view.showProgressView();
        iCmd(1, reqPass);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}