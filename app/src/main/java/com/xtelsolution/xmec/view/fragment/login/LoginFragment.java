package com.xtelsolution.xmec.view.fragment.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.presenter.login.LoginPresenter;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.login.ILoginView;
import com.xtelsolution.xmec.view.widget.DialogFpPassword;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public class LoginFragment extends BasicFragment implements ILoginView {

    private LoginPresenter presenter;

    @BindView(R.id.edt_phone)
    TextInputEditText edtPhone;
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_forgot_pass)
    Button btnForgotPass;
    Unbinder unbinder;

    private final int PERMISSION_LOGIN = 1;
    private final int PERMISSION_RESET = 2;
    private final int REQUEST_ACTIVE = 1;
    private final int REQUEST_RESET = 2;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new LoginPresenter(this);
        initActionListener();
    }

    /**
     * Khởi tạo lắng nghe sự kiện đăng nhập
     */
    private void initActionListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
//                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_LOGIN);
            }
        });

        btnForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputPhone();
//                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_RESET);
            }
        });
    }

    /**
     * Thực hiện đăng nhập
     */
    private void login() {
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        presenter.login(phone, password);
    }

    /**
     * Nhập số điện thoại để thay đổi mậ khẩu
     */
    private void inputPhone() {
        startAccountKit(null, REQUEST_RESET);

//        new DialogFpPhone(getContext()) {
//            @Override
//            protected void setPhone(String phone) {
//                startAccountKit(phone, REQUEST_RESET);
//            }
//        };
    }

    /**
     * Nhập mật khẩu muốn đặt lại
     *
     * @param authorizationCode mã định danh điện thoại
     */
    private void inputPassword(final String authorizationCode) {
        new DialogFpPassword(getContext()) {
            @Override
            protected void setPassword(String password) {
                presenter.resetPasswork(password, authorizationCode);
            }
        };
    }

    /**
     * Hỏi người dùng active tài khoản
     */
    private void showConfirmActive() {
        showConfirmDialog(null, getString(R.string.message_lg_active), new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                String phone = edtPhone.getText().toString();

                if (phone.charAt(0) == '+' && phone.charAt(1) == '8' && phone.charAt(2) == '4') {
                    phone = "0" + new StringBuilder(phone).delete(0, 3).toString();
                } else if (phone.charAt(0) == '8' && phone.charAt(1) == '4') {
                    phone = "0" + new StringBuilder(phone).delete(0, 2).toString();
                }

                startAccountKit(phone, REQUEST_ACTIVE);
            }
        });
    }

    private void validatePhone(final String authorizationCode) {
        AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
            @Override
            public void onSuccess(final Account account) {
//                PhoneNumber phoneNumber = account.getPhoneNumber();

                String phoneNumberString = account.getPhoneNumber().getPhoneNumber();
                presenter.validatePhone(phoneNumberString, authorizationCode);
            }

            @Override
            public void onError(final AccountKitError error) {
                // Handle Error
                LoginFragment.this.onError(Constants.ERROR_UNKOW);
            }
        });
    }

    public void setData(String phone, String password) {
        edtPhone.setText(phone);
        edtPassword.setText(password);
    }

    public void autoLogin() {
        btnLogin.performClick();
    }


    /**
     * Đăng nhập thành công
     */
    @Override
    public void onLoginSuccess() {
        int position = SharedUtils.getInstance().getIntValue(Constants.GO_TO_ITEM);
        closeProgressBar();
        Intent intent = new Intent(getContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        if (position > -1){
            intent.putExtra(Constants.GO_TO_ITEM, position);
        }
        startActivity(intent);
        finishActivity();
    }

    /**
     * Kích hoạt tài khoản thành công
     */
    @Override
    public void onActiveSuccess() {
        closeProgressBar();
        showLongToast(getString(R.string.success_lg_active));
        autoLogin();
    }

    /**
     * Đổi mật khẩu thành công
     */
    @Override
    public void onResetSuccess() {
        closeProgressBar();
        showLongToast(getString(R.string.success_lg_reset));
    }

    /**
     * Kích hoạt tì khoản không thành công
     */
    @Override
    public void onActiveError() {
        closeProgressBar();
        showLongToast(getString(R.string.error_lg_active));
    }

    @Override
    public void onValidateError() {
        closeProgressBar();
        showLongToast(getString(R.string.error_lg_validate));
    }

    /**
     * Đổi mật khẩu không thành công
     */
    @Override
    public void onResetError() {
        closeProgressBar();
        showLongToast(getString(R.string.error_lg_reset));
    }

    /**
     * Hiển thị trạng thái đang thực hiện đăng nhập
     */
    @Override
    public void showProgressBarLogin() {
        showProgressBar(false, false, getString(R.string.doing_login));
    }

    /**
     * Hiển thị trạng thái đang thực hiện kích hoạt
     */
    @Override
    public void showProgressBarActive() {
        showProgressBar(false, false, getString(R.string.doing_active));
    }

    /**
     * Hiển thị trạng thái đang thực hiện lấy mật khẩu mới
     */
    @Override
    public void showProgressResetPass() {
        showProgressBar(false, false, getString(R.string.doing_reset_pass));
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    /**
     * Hiển thị lỗi
     *
     * @param code mã lỗi
     */
    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 1:
                showLongToast(getString(R.string.error_lg_input_phone));
                break;
            case 2:
                showLongToast(getString(R.string.error_lg_input_password));
                break;
            case 3:
                showLongToast(getString(R.string.error_lg_invalid_phone));
                break;
            case 4:
                showLongToast(getString(R.string.error_lg_invalid_password));
                break;
            case 112:
                showConfirmActive();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PermissionHelper.checkOnlyResult(grantResults, 0)) {
            if (requestCode == PERMISSION_LOGIN)
                login();
            else
                inputPhone();
        } else {
            showLongToast(getString(R.string.error_not_allow_permission));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ACTIVE || requestCode == REQUEST_RESET) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null) {
                if (requestCode == REQUEST_ACTIVE)
                    onActiveError();
            } else if (!loginResult.wasCancelled()) {
                if (loginResult.getAccessToken() != null) {
                    if (requestCode == REQUEST_ACTIVE)
                        onActiveError();
                    else {
                        String authorizationCode = loginResult.getAuthorizationCode();
                        validatePhone(authorizationCode);
                    }
                } else {
                    String authorizationCode = loginResult.getAuthorizationCode();

                    if (requestCode == REQUEST_ACTIVE)
                        presenter.activeAccount(edtPhone.getText().toString(), authorizationCode);
                    else {
//                        validatePhone(authorizationCode);
                        inputPassword(authorizationCode);
                    }
                }
            }
        }
    }
}