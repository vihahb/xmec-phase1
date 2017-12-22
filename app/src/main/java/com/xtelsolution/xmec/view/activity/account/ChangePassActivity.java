package com.xtelsolution.xmec.view.activity.account;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.presenter.account.ChangePassPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.account.IChangePassView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePassActivity extends BasicActivity implements IChangePassView {
    private ChangePassPresenter presenter;

    @BindView(R.id.edt_pass_old)
    TextInputEditText edtPassOld;
    @BindView(R.id.edt_pass_new)
    TextInputEditText edtPassNew;
    @BindView(R.id.edt_pass_again)
    TextInputEditText edtPassAgain;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_add)
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind(this);

        presenter = new ChangePassPresenter(this);
        KeyboardUtils.showSoftKeyboard(this);
    }

    @OnClick({R.id.btn_cancel, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_add:
                String pass_old = edtPassOld.getText().toString();
                String pass_new = edtPassNew.getText().toString();
                String pass_again = edtPassAgain.getText().toString();

                presenter.changePass(pass_old, pass_new, pass_again);
                break;
        }
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 301:
                edtPassOld.setError(getString(R.string.error_pass_input));
                edtPassOld.requestFocus();
                break;
            case 302:
                edtPassNew.setError(getString(R.string.error_pass_input));
                edtPassNew.requestFocus();
                break;
            case 303:
                edtPassAgain.setError(getString(R.string.error_pass_input));
                edtPassAgain.requestFocus();
                break;
            case 304:
                edtPassOld.setError(getString(R.string.error_pass_invalid));
                edtPassOld.requestFocus();
                break;
            case 305:
                edtPassNew.setError(getString(R.string.error_pass_invalid));
                edtPassNew.requestFocus();
                break;
            case 306:
                edtPassAgain.setError(getString(R.string.error_pass_invalid));
                edtPassAgain.requestFocus();
                break;
            case 307:
                edtPassAgain.setError(getString(R.string.error_pass_equal));
                edtPassAgain.requestFocus();
                break;
            case 404:
                edtPassOld.setError(getString(R.string.error_pass_wrong));
                edtPassOld.requestFocus();
                break;
        }
    }

    @Override
    public void onChangeSuccess() {
        closeProgressBar();
        showLongToast(getString(R.string.success_change_pass));
        finish();
    }

    @Override
    public void showProgressView() {
        showProgressBar(false, false, getString(R.string.doing_change_pass));
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}