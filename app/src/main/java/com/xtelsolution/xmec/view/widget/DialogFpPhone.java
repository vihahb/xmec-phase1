package com.xtelsolution.xmec.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/16/2017
 * Email: leconglongvu@gmail.com
 */
public abstract class DialogFpPhone {
    private Dialog dialog;

    private TextInputEditText edtPhone;

    private Button btnCancel;

    private Button btnContinue;

    public DialogFpPhone(Context context) {
        dialog = new Dialog(context, R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_fp_phone);
        //noinspection ConstantConditions
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        dialog.setCancelable(isTouchOutside);
//        dialog.setCanceledOnTouchOutside(isCancelable);

        edtPhone = dialog.findViewById(R.id.edt_phone);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnContinue = dialog.findViewById(R.id.btn_continue);
        dialog.show();

        initActionListener();
    }

    private void initActionListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputPhone();
            }
        });
    }

    private void checkInputPhone() {
        String phone = edtPhone.getText().toString();

        if (TextUtils.isEmpty(phone)) {
            showShortSnackBar(dialog.getContext().getString(R.string.error_lg_input_phone));
            return;
        }

        if (phone.length() < 10 || phone.length() > 15) {
            showShortSnackBar(dialog.getContext().getString(R.string.error_lg_invalid_phone));
            return;
        }

        dialog.dismiss();

        setPhone(phone);
    }

    private void showShortSnackBar(String message) {
        Snackbar.make(btnContinue, message, Snackbar.LENGTH_SHORT).show();
    }

    protected abstract void setPhone(String phone);
}