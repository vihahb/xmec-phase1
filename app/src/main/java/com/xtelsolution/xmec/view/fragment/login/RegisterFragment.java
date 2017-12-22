package com.xtelsolution.xmec.view.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.accountkit.AccountKitLoginResult;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.presenter.login.RegisterPresenter;
import com.xtelsolution.xmec.view.activity.login.LoginActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.login.IRegisterView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/12/2017
 * Email: leconglongvu@gmail.com
 */
public class RegisterFragment extends BasicFragment implements IRegisterView {

    private RegisterPresenter presenter;

    @BindView(R.id.edt_name)
    TextInputEditText edtName;
    @BindView(R.id.edt_phone)
    TextInputEditText edtPhone;
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;
    @BindView(R.id.edt_passwork_again)
    TextInputEditText edtPassworkAgain;
    @BindView(R.id.btn_login)
    Button btnLogin;
    Unbinder unbinder;

    private final int REQUEST_ACTIVE = 1;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new RegisterPresenter(this);
        initActionListener();
    }

    /**
     * Khởi tạo sụ kiện khi click nút login
     */
    private void initActionListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
    }

    /**
     * Thực hiện đăng ký tài khoản
     */
    private void register() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String password = edtPassword.getText().toString();
        String password_again = edtPassworkAgain.getText().toString();

        presenter.register(name, phone, password, password_again);
    }

    /**
     * Xóa dữ liệu đã nhập vào view
     */
    private void clearData() {
        edtName.setText(null);
        edtPhone.setText(null);
        edtPassword.setText(null);
        edtPassworkAgain.setText(null);
    }

    /**
     * Thông báo lỗi
     *
     * @param code mã lỗi
     */
    @Override
    public void onError(int code) {
        super.onError(code);

        String message;

        switch (code) {
            case 301:
                message = getString(R.string.error_rg_input_name);
                break;
            case 302:
                message = getString(R.string.error_rg_input_phone);
                break;
            case 303:
                message = getString(R.string.error_rg_input_password);
                break;
            case 304:
                message = getString(R.string.error_rg_input_password_again);
                break;
            case 305:
                message = getString(R.string.error_rg_invalid_phone);
                break;
            case 306:
                message = getString(R.string.error_rg_invalid_password);
                break;
            case 307:
                message = getString(R.string.error_rg_invalid_password_again);
                break;
            case 308:
                message = getString(R.string.error_rg_not_equal_password);
                break;
            case 309:
                message = getString(R.string.error_rg_not_phone);
                break;
            default:
                return;
        }

        showLongToast(message);
    }

    /**
     * Đăng ký thành công
     */
    @Override
    public void onRegisterSuccess(String phome) {
        closeProgressBar();
        showLongToast(getString(R.string.success_register));

        ((LoginActivity) getActivity()).setDataToLogin(phome, edtPassword.getText().toString());
        clearData();
        startAccountKit(phome, REQUEST_ACTIVE);
    }

    @Override
    public void onActiveSuccess() {
        closeProgressBar();
        showLongToast(getString(R.string.success_active));
        ((LoginActivity) getActivity()).goToLogin(true);
    }

    @Override
    public void onActiveError() {
        closeProgressBar();
        showLongToast(getString(R.string.error_active));
        ((LoginActivity) getActivity()).goToLogin(false);
    }

    /**
     * Đăng ký thất bại
     */
//    @Override
//    public void onRegisterError() {
//        closeProgress();
//        showLongToast(getString(R.string.error_register));
//    }

//    @Override
//    public void onActiveAccount(String phone) {
//
//    }

    /**
     * Hiển thị thông báo đang đăng ký
     */
    @Override
    public void showProgressRegister() {
        showProgressBar(false, false, getString(R.string.doing_register));
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ACTIVE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (loginResult.getError() != null) {
                onActiveError();
            } else if (loginResult.wasCancelled()) {
                onActiveError();
            } else {
                if (loginResult.getAccessToken() != null) {

                    onActiveError();
                } else {
                    String token = loginResult.getAuthorizationCode();

                    presenter.activeAccount(token);
                }
            }
        }
    }
}