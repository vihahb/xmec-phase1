package com.xtelsolution.xmec.view.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;
import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.Utils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;
import com.xtelsolution.xmec.view.activity.login.LoginActivity;

import java.io.Serializable;

import static com.facebook.accountkit.ui.SkinManager.Skin.TRANSLUCENT;

/**
 * Created by Lê Công Long Vũ on 12/29/2016
 */

public abstract class BasicFragment extends Fragment implements IBasicView {
    private Dialog progressDialog;
    private Toast toast;
    private UIManager uiManager;

    protected int getIntExtra(String key) {
        try {
            return getArguments().getInt(key, -1);
        } catch (Exception e) {
            return -1;
        }
    }

    protected int getIntExtra(String key, Intent data) {
        if (data != null)
            try {
                return data.getIntExtra(key, -1);
            } catch (Exception e) {
                return -1;
            }
        else
            return -1;
    }

    /*
    * Hide soft Keyboard*/
    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    /*
    * Hiển thị thông báo snackbar 2s
    * */

    @Override
    public void showLongToast(String message) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onEndSession() {
        SharedUtils.getInstance().clearData();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


        getActivity().finish();
        startActivity(intent);
    }

    /*
            * Hiển thị thông báo snackbar 3.5s
            * */
//    protected void showLongSnackBar(String message) {
//        if (getView() != null)
//            Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
//    }

//    protected void showLongToast(String message) {
//        if (toast != null)
//            toast.cancel();
//
//        toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
//        toast.show();
//    }

//    protected void showLongToast(String message) {
//        if (toast != null)
//            toast.cancel();
//
//        toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
//        toast.show();
//    }

    protected void showProgressBar(boolean isTouchOutside, boolean isCancel, String message) {
        try {
            progressDialog = new Dialog(getContext(), R.style.Theme_Transparent);
            progressDialog.setContentView(R.layout.dialog_progressbar_v2);
            //noinspection ConstantConditions
            progressDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            progressDialog.setCancelable(isTouchOutside);
            progressDialog.setCanceledOnTouchOutside(isCancel);

            if (message != null) {
                TextView textView = progressDialog.findViewById(R.id.txt_message);
                textView.setText(message);
            }

            if (progressDialog != null)
                progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void closeProgressBar() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    protected void showMaterialDialog(boolean isTouchOutside, boolean isCancelable, String title, String message,
                                      String negative, String positive, final DialogListener dialogListener) {
        final Dialog dialog = new Dialog(getContext(), R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_material);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(isTouchOutside);
        dialog.setCanceledOnTouchOutside(isCancelable);

        TextView txt_title = dialog.findViewById(R.id.dialog_txt_title);
        TextView txt_message = dialog.findViewById(R.id.dialog_txt_message);
        Button btn_negative = dialog.findViewById(R.id.dialog_btn_negative);
        Button btn_positive = dialog.findViewById(R.id.dialog_btn_positive);

        if (title == null)
            txt_title.setVisibility(View.GONE);
        else
            txt_title.setText(title);

        if (message == null)
            txt_message.setVisibility(View.GONE);
        else
            txt_message.setText(message);

        if (negative == null)
            btn_negative.setVisibility(View.GONE);
        else
            btn_negative.setText(negative);

        if (positive == null)
            btn_positive.setVisibility(View.GONE);
        else
            btn_positive.setText(positive);

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.negativeClicked();
            }
        });

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogListener.positiveClicked();
            }
        });

        if (isTouchOutside && isCancelable)
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    dialogListener.negativeClicked();
                }
            });

        dialog.show();
    }

    protected void showConfirmDialog(String title, String message, final ConfirmListener confirmListener) {
        final Dialog dialog = new Dialog(getContext(), R.style.Theme_Transparent);
        dialog.setContentView(R.layout.dialog_material);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        TextView txt_title = (TextView) dialog.findViewById(R.id.dialog_txt_title);
        TextView txt_message = (TextView) dialog.findViewById(R.id.dialog_txt_message);
        Button btn_negative = (Button) dialog.findViewById(R.id.dialog_btn_negative);
        Button btn_positive = (Button) dialog.findViewById(R.id.dialog_btn_positive);

        if (title == null)
            txt_title.setVisibility(View.GONE);
        else
            txt_title.setText(title);

        if (message == null)
            txt_message.setVisibility(View.GONE);
        else
            txt_message.setText(message);

        btn_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                confirmListener.onConfirmSuccess();
            }
        });

        dialog.show();
    }

    /**
     * Xác nhận tài khoản bằng Account Kit
     */
    protected void startAccountKit(String phone, int requestCode) {

        uiManager = new SkinManager(TRANSLUCENT, getResources().getColor(R.color.colorPrimary), R.mipmap.im_account_kit,
                SkinManager.Tint.BLACK,
                30);


        Intent intent = new Intent(getActivity(), AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        configurationBuilder.setUIManager(uiManager);
        configurationBuilder.setSMSWhitelist(new String[]{"VN"});

        if (!TextUtils.isEmpty(phone))
            configurationBuilder.setInitialPhoneNumber(new PhoneNumber("+84", phone, "VN"));

        // ... perform additional configuration ...
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, requestCode);
    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
    }

    protected void startActivity(Class clazz, String key, Object object) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(key, (Serializable) object);
        startActivity(intent);
    }

    protected void startActivityForResult(Class clazz, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class clazz, String key, Object object, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(key, (Serializable) object);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class clazz, String[] key, Object[] value, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);

        for (int i = 0; i < key.length; i++) {
            intent.putExtra(key[i], (Serializable) value[i]);
        }

        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class clazz, String key, Object object, String key_1, Object object_1, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(key, (Serializable) object);
        intent.putExtra(key_1, (Serializable) object_1);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class clazz, String key, Object object, String key_1, Object object_1, String key_2, Object object_2, int requestCode) {
        Intent intent = new Intent(getActivity(), clazz);
        intent.putExtra(key, (Serializable) object);
        intent.putExtra(key_1, (Serializable) object_1);
        intent.putExtra(key_2, (Serializable) object_2);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityAndFinish(Class clazz) {
        startActivity(new Intent(getActivity(), clazz));
        getActivity().finish();
    }

    protected void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void onError(int code) {

        closeProgressBar();

        String error = TextUtils.getErrorCode(code);

        if (error != null) {
            showLongToast(error);
        }
    }
}