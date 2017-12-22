package com.xtelsolution.xmec.presenter.login;

import android.util.Log;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Error;
import com.xtelsolution.xmec.model.entity.NotifyConfig;
import com.xtelsolution.xmec.model.entity.RESP_Basic;
import com.xtelsolution.xmec.model.req.REQ_Active;
import com.xtelsolution.xmec.model.req.REQ_Login;
import com.xtelsolution.xmec.model.req.REQ_ReActive;
import com.xtelsolution.xmec.model.req.REQ_Reset;
import com.xtelsolution.xmec.model.resp.RESP_Login;
import com.xtelsolution.xmec.model.resp.RESP_Notification;
import com.xtelsolution.xmec.model.resp.RESP_Register;
import com.xtelsolution.xmec.model.resp.RESP_Reset;
import com.xtelsolution.xmec.model.resp.RESP_UserInfo;
import com.xtelsolution.xmec.model.server.ActiveModel;
import com.xtelsolution.xmec.model.server.GetUserInfoModel;
import com.xtelsolution.xmec.model.server.LoginModel;
import com.xtelsolution.xmec.model.server.NotificationModel;
import com.xtelsolution.xmec.model.server.ReActiveModel;
import com.xtelsolution.xmec.model.server.ResetModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.inf.login.ILoginView;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/14/2017
 * Email: leconglongvu@gmail.com
 */
public class LoginPresenter extends BasicPresenter {
    private ILoginView view;

    public LoginPresenter(ILoginView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:

                String phone = (String) params[1];
                String password = (String) params[2];

                REQ_Login login = new REQ_Login();
                login.setUsername(phone);
                login.setPassword(password);

                new LoginModel(JsonHelper.toJson(login)) {
                    @Override
                    protected void onSuccess(RESP_Login obj) {
                        SharedUtils.getInstance().putBooleanValue(Constants.FLAG_NOTIFICATION, true);
                        SharedUtils.getInstance().putStringValue(Constants.AUTHENTICATION_ID, obj.getAuthenticationid());
                        SharedUtils.getInstance().putStringValue(Constants.SESSION, obj.getSession());
                        SharedUtils.getInstance().putLongValue(Constants.LOGIN_TIME, obj.getLogin_time());
                        SharedUtils.getInstance().putLongValue(Constants.EXPIRED_TIME, obj.getExpired_time());
                        SharedUtils.getInstance().putIntValue(Constants.TIME_ALIVE, obj.getTime_alive());
                        SharedUtils.getInstance().putIntValue(Constants.DEV_INFO_STATUS, obj.getDev_info_status());

//                        view.onLoginSuccess();
                        iCmd(6);
                    }

                    @Override
                    protected void onError(Error error) {
                        view.onError(error.getCode());
                    }
                };

                break;
            case 2:

                final String username = (String) params[1];
                final String token2 = (String) params[2];

                final REQ_ReActive reActive = new REQ_ReActive();
                reActive.setUsername(username);

                new ReActiveModel(JsonHelper.toJson(reActive)) {
                    @Override
                    protected void onSuccess(RESP_Register obj) {
                        iCmd(3, token2, obj.getActivation_code());
                    }

                    @Override
                    protected void onError(Error error) {
                        view.onActiveError();
                    }
                };

                break;
            case 3:

                String token3 = (String) params[1];
                String activationCode = (String) params[2];

                REQ_Active active = new REQ_Active();
                active.setAuthorization_code(token3);
                active.setActivation_code(activationCode);

                new ActiveModel(JsonHelper.toJson(active)) {
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
            case 4:

                final String phone4 = (String) params[1];
                final String authorizationCode4 = (String) params[2];

                String password4 = System.currentTimeMillis() + "" + System.currentTimeMillis() + "" + System.currentTimeMillis();

                REQ_Login login4 = new REQ_Login();
                login4.setUsername(phone4);
                login4.setPassword(password4);

                new LoginModel(JsonHelper.toJson(login4)) {
                    @Override
                    protected void onSuccess(RESP_Login obj) {
                        iCmd(5, phone4, authorizationCode4);
                    }

                    @Override
                    protected void onError(Error error) {
                        if (error.getCode() == 108)
                            view.onValidateError();
                        else
                            iCmd(5, phone4, authorizationCode4);
                    }
                };

                break;
            case 5:

                String password5 = (String) params[1];
                String authorizationCode5 = (String) params[2];

                REQ_Reset reset = new REQ_Reset();
                reset.setPassword(password5);
                reset.setAuthorization_code(authorizationCode5);

                new ResetModel(JsonHelper.toJson(reset)) {
                    @Override
                    protected void onSuccess(RESP_Reset obj) {
                        view.onResetSuccess();
                    }

                    @Override
                    protected void onError(int errorCode) {
                        view.onResetError();
                    }
                };

                break;
            case 6:
                SharedUtils.getInstance().putBooleanValue(Constants.FLAG_NOTIFICATION, true);
                new GetUserInfoModel() {
                    @Override
                    protected void onSuccess(RESP_UserInfo respUserInfo) {
                        SharedUtils.getInstance().putIntValue(Constants.USER_UID, respUserInfo.getUid());
                        SharedUtils.getInstance().putStringValue(Constants.USER_FULLNAME, respUserInfo.getFullname());
                        SharedUtils.getInstance().putStringValue(Constants.USER_PHONENUMBER, respUserInfo.getPhonenumber());
                        SharedUtils.getInstance().putStringValue(Constants.USER_ADDRESS, respUserInfo.getAddress());
                        SharedUtils.getInstance().putStringValue(Constants.USER_AVATAR, respUserInfo.getAvatar());

                        if (respUserInfo.getGender() != null)
                            SharedUtils.getInstance().putIntValue(Constants.USER_GENDER, respUserInfo.getGender());

                        if (respUserInfo.getBirthdayLong() != null)
                            SharedUtils.getInstance().putLongValue(Constants.USER_BIRTHDAYLONG, respUserInfo.getBirthdayLong());

                        if (respUserInfo.getWeight() != null)
                            SharedUtils.getInstance().putLongValue(Constants.USER_WEIGHT, respUserInfo.getWeight().longValue());

                        if (respUserInfo.getHeight() != null)
                            SharedUtils.getInstance().putLongValue(Constants.USER_HEIGHT, respUserInfo.getHeight().longValue());

                        getCountNotify();
                    }

                    @Override
                    protected void onErrror(int code) {
                        SharedUtils.getInstance().clearData();
                        showError(Constants.ERROR_UNKOW);
                    }
                };

                break;

            case 7:
                //Get Count notify
                int type;
                if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null){
                    type = 1;
                } else {
                    type = 0;
                }
                new NotificationModel(type) {
                    @Override
                    protected void onSuccess(RESP_Notification obj) {
                        int count = 0;
                        for (int i = 0; i < obj.getData().size(); i++) {
                            if (obj.getData().get(i).getNotifyState() == NotifyConfig.STATE_PENDING){
                                count++;
                            }
                        }
                        SharedUtils.getInstance().putIntValue(Constants.BADGE_COUNT, count);
                        MyApplication.log("Notify count", ":"+count);
                        view.onLoginSuccess();
                    }

                    @Override
                    protected void onError(int error) {
                        MyApplication.log("Err get Count", "Code: " + error);
                        SharedUtils.getInstance().putIntValue(Constants.BADGE_COUNT, 0);
                        view.onLoginSuccess();
                    }
                };
                break;
        }
    }

    public void getCountNotify(){
        if (!NetworkUtils.isConnected(MyApplication.context)) {
//            view.showLongToast(view.getActivity().getString(R.string.error_internet));
            Log.e("CountNotifyLogin", view.getActivity().getString(R.string.error_internet));
            return;
        }
        iCmd(7);
    }

    public void login(Object... params) {
        String phone = (String) params[0];
        String password = (String) params[1];

        if (TextUtils.isEmpty(phone)) {
            view.onError(1);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            view.onError(2);
            return;
        }

        if (phone.length() < 10 || phone.length() > 15) {
            view.onError(3);
            return;
        }

        if (password.length() < 6 || password.length() > 20 || password.contains(" ")) {
            view.onError(4);
            return;
        }

        if (!NetworkUtils.isConnected(view.getFragment().getContext().getApplicationContext())) {
            view.onError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBarLogin();
        iCmd(1, phone, password);
    }

    public void activeAccount(String username, String token) {
        if (!NetworkUtils.isConnected(view.getFragment().getContext().getApplicationContext())) {
            view.onError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressBarActive();
        iCmd(2, username, token);
    }

    public void validatePhone(String phone, String authorizationCode) {
        view.showProgressResetPass();

        iCmd(4, phone, authorizationCode);
    }

    public void resetPasswork(String password, String authorizationCode) {
        view.showProgressResetPass();
        iCmd(5, password, authorizationCode);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }
}