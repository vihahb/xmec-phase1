package com.xtelsolution.xmec.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/16/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class DialogFpPassword {
    private Dialog dialog;

    private TextInputEditText edtPass;
    private TextInputEditText edtPassAgain;

    private Button btnDone;

    public DialogFpPassword(Context context) {
        dialog = new Dialog(context, R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_fp_password);
        //noinspection ConstantConditions
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        dialog.setCancelable(isTouchOutside);
//        dialog.setCanceledOnTouchOutside(isCancelable);

        edtPass = dialog.findViewById(R.id.edt_password);
        edtPassAgain = dialog.findViewById(R.id.edt_password_again);
        btnDone = dialog.findViewById(R.id.btn_done);
        dialog.show();

        initActionListener();
    }

    private void initActionListener() {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputPhone();
            }
        });
    }

    private void checkInputPhone() {
        String password = edtPass.getText().toString();
        String passwordAgain = edtPassAgain.getText().toString();

        if (TextUtils.isEmpty(password)) {
            showShortSnackBar(dialog.getContext().getString(R.string.error_rg_input_password));
            return;
        }

        if (TextUtils.isEmpty(passwordAgain)) {
            showShortSnackBar(dialog.getContext().getString(R.string.error_rg_input_password_again));
            return;
        }

        if (password.length() < 6 || password.length() > 20) {
            showShortSnackBar(dialog.getContext().getString(R.string.error_rg_invalid_password));
            return;
        }

        if (passwordAgain.length() < 6 || passwordAgain.length() > 20) {
            showShortSnackBar(dialog.getContext().getString(R.string.error_rg_invalid_password_again));
            return;
        }

        if (!password.equals(passwordAgain)) {
            showShortSnackBar(dialog.getContext().getString(R.string.error_rg_not_equal_password));
            return;
        }

        if (!NetworkUtils.isConnected(dialog.getContext().getApplicationContext())) {
            showShortSnackBar(dialog.getContext().getString(R.string.error_no_internet));
            return;
        }

        dialog.dismiss();

        setPassword(password);
    }

    private void showShortSnackBar(String message) {
        Snackbar.make(btnDone, message, Snackbar.LENGTH_SHORT).show();
    }

    protected abstract void setPassword(String password);
}