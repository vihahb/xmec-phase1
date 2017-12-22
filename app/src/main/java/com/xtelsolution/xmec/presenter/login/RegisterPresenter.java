package com.xtelsolution.xmec.presenter.login;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.entity.UserInfo;
import com.xtelsolution.xmec.model.req.REQ_Active;
import com.xtelsolution.xmec.model.req.REQ_Register;
import com.xtelsolution.xmec.model.resp.RESP_Register;
import com.xtelsolution.xmec.model.resp.RESP_ValidatePhone;
import com.xtelsolution.xmec.model.server.ActiveModel;
import com.xtelsolution.xmec.model.server.RegisterModel;
import com.xtelsolution.xmec.model.server.ValidatePhoneNumber;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.inf.login.IRegisterView;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public class RegisterPresenter extends BasicPresenter {

    private IRegisterView view;

    private String activationCode;

    public RegisterPresenter(IRegisterView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {

            case 0:
                final String name_valid = (String) params[1];
                final String phone_valid = (String) params[2];
                final String password_valid = (String) params[3];
                new ValidatePhoneNumber(phone_valid) {
                    @Override
                    protected void onSuccess(RESP_ValidatePhone validatePhone) {
                        if (validatePhone.isValid()){
                            iCmd(1, name_valid, phone_valid, password_valid);
                        } else {
                            view.onError(309);
                        }
                    }

                    @Override
                    protected void onError(int errorCode) {
                        view.onError(errorCode);
                    }
                };
                break;

            case 1:

                String name = (String) params[1];
                final String phone = (String) params[2];
                String password = (String) params[3];

                REQ_Register registerAccount = new REQ_Register();
                registerAccount.setUsername(phone);
                registerAccount.setPassword(password);

                UserInfo userInfo = new UserInfo(name);
                registerAccount.setUserInfo(userInfo);

                new RegisterModel(JsonHelper.toJson(registerAccount)) {
                    @Override
                    protected void onSuccess(RESP_Register obj) {
                        activationCode = obj.getActivation_code();
                        view.onRegisterSuccess(phone);
//                        view.onActiveAccount(phone);
                    }

                    @Override
                    protected void onError(Error error) {
                        view.onError(error.getCode());
                    }
                };

                break;
            case 2:

                if (TextUtils.isEmpty(activationCode)) {
                    view.onActiveError();
                    return;
                }

                String token = (String) params[1];

                REQ_Active activeAccount = new REQ_Active();
                activeAccount.setAuthorization_code(token);
                activeAccount.setActivation_code(activationCode);

                new ActiveModel(JsonHelper.toJson(activeAccount)) {
                    @Override
                    protected void onSuccess(RESP_Basic obj) {
                        view.onActiveSuccess();
                    }

                    @Override
                    protected void onError(Error error) {
                        view.onActiveError();
                    }
                };

                break;
        }
    }

    public void register(Object... params) {
        String name = (String) params[0];
        String phone = (String) params[1];
        String password = (String) params[2];
        String password_again = (String) params[3];

        if (TextUtils.isEmpty(name)) {
            showError(301);
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            showError(302);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showError(303);
            return;
        }

        if (TextUtils.isEmpty(password_again)) {
            showError(304);
            return;
        }

        if (phone.length() < 10 || phone.length() > 15) {
            showError(305);
            return;
        }

        if (password.length() < 6 || password.length() > 20 || password.contains(" ")) {
            showError(306);
            return;
        }

        if (password_again.length() < 6 || password_again.length() > 20 || password_again.contains(" ")) {
            showError(307);
            return;
        }

        if (!password.equals(password_again)) {
            showError(308);
            return;
        }

        if (phone.charAt(0) == '+') {
            if (phone.charAt(1) != '8' || phone.charAt(2) != '4') {
                showError(309);
                return;
            }

            phone = "0" + new StringBuilder(phone).delete(0, 3).toString();
        } else if (phone.charAt(0) == '8') {
            if (phone.charAt(1) != '4') {
                showError(309);
                return;
            }

            phone = "0" + new StringBuilder(phone).delete(0, 2).toString();
        }

        if (!NetworkUtils.isConnected(view.getFragment().getContext().getApplicationContext())) {
            view.onError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressRegister();

        iCmd(0, name, phone, password);
    }

    public void activeAccount(String token) {
        iCmd(2, token);
    }

    @Override
    protected void getSessionSuccess(Object... params) {

    }
}