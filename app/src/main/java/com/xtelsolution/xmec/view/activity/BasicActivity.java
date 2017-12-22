package com.xtelsolution.xmec.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.Utils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.view.activity.inf.IBasicView;
import com.xtelsolution.xmec.view.activity.login.LoginActivity;

import java.io.Serializable;

/**
 * Created by Lê Công Long Vũ on 12/2/2016
 */

public abstract class BasicActivity extends AppCompatActivity implements IBasicView {
    boolean isWaitingForExit = false;
    private Dialog progressDialog;
    private Toast toast;

    public void initToolbar(int id, String title) {
        Toolbar toolbar = findViewById(id);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (title != null)
            actionBar.setTitle(title);
    }

    public void initToolbar(int id, String title, int resource) {
        Toolbar toolbar = (Toolbar) findViewById(id);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (resource != -1)
            toolbar.setNavigationIcon(resource);

        if (title != null)
            actionBar.setTitle(title);
    }

    protected int getIntExtra(String key) {
        try {
            return getIntent().getIntExtra(key, -1);
        } catch (Exception e) {
            return -1;
        }
    }

    protected boolean getBolExtra(String key) {
        try {
            return getIntent().getBooleanExtra(key, false);
        } catch (Exception e) {
            return false;
        }
    }

    public int getIntData(String key, Intent data) {
        try {
            return data.getIntExtra(key, -1);
        } catch (Exception e) {
            return -1;
        }
    }

    /*
    * Hide soft keyboard*/
    public void setupUI(View view, final Activity activity) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView, activity);
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

        toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
    /*
    * Hiển thị thông báo 2s
    * */
    protected void showShortToast(String message) {
        if (toast != null)
            toast.cancel();

        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onEndSession() {
        SharedUtils.getInstance().clearData();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        finish();
        startActivity(intent);
    }

    /*
    * Hiển thị tiến trình (đang thực hiện)
    * */
    protected void showProgressBar(boolean isTouchOutside, boolean isCancel, String message) {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            progressDialog = new Dialog(BasicActivity.this, R.style.Theme_Transparent);
            progressDialog.setContentView(R.layout.dialog_progressbar_v2);
            //noinspection ConstantConditions
            progressDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            progressDialog.setCancelable(isTouchOutside);
            progressDialog.setCanceledOnTouchOutside(isCancel);

            if (message != null) {
                TextView textView = progressDialog.findViewById(R.id.txt_message);
                textView.setText(message);
            }

            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Kết thúc hiển thị tiến trình (đang thực hiện)
    * */
    protected void closeProgressBar() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    protected void showErrorGetData(String message) {
        String mes = getString(R.string.error_try_again);

        if (message != null)
            mes = message;

        showMaterialDialog(false, false, null, mes, null, getString(R.string.layout_ok), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                finish();
            }
        });
    }

    /*
    * Hiển thị thông báo (chuẩn material)
    * */
    @SuppressWarnings("ConstantConditions")
    protected void showMaterialDialog(boolean isTouchOutside, boolean isCancelable, String title, String message, String negative, String positive, final DialogListener dialogListener) {
        final Dialog dialog = new Dialog(BasicActivity.this, R.style.Theme_Transparent);
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

        dialog.show();
    }

    protected void showConfirmDialog(String title, String message, final ConfirmListener confirmListener) {
        final Dialog dialog = new Dialog(BasicActivity.this, R.style.Theme_Transparent);
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

    /*
    * Khởi tạo fragment vào 1 view layout (FrameLayout)
    * */
    protected void replaceFragment(int id, Fragment fragment, String tag) {
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(id, fragment, tag);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
        }
    }

    public void getFragment(){

    }

    protected void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    protected void startActivity(Class clazz, String key, Object object) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(key, (Serializable) object);
        startActivity(intent);
    }

    protected void startActivityForResult(Class clazz, int requestCode) {
        startActivityForResult(new Intent(this, clazz), requestCode);
    }

    protected void startActivityForResult(Class clazz, String key, Object object, int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(key, (Serializable) object);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class clazz, String key, Object object, String key1, Object object1, int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(key, (Serializable) object);
        intent.putExtra(key1, (Serializable) object1);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityForResult(Class clazz, String key, Object object, String key1, Object object1, String key2, Object object2, int requestCode) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(key, (Serializable) object);
        intent.putExtra(key1, (Serializable) object1);
        intent.putExtra(key2, (Serializable) object2);
        startActivityForResult(intent, requestCode);
    }

    protected void startActivityAndFinish(Class clazz) {
        startActivity(new Intent(this, clazz));
        finish();
    }

    protected void startActivityAndFinish(Class clazz, String key, Object object) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra(key, (Serializable) object);
        startActivity(intent);
        finish();
    }

    protected void showConfirmExitApp() {
        if (isWaitingForExit) {
            finish();
        } else {
            new WaitingForExit().execute();
        }
    }

    @Override
    public void onError(int code) {
        closeProgressBar();

        String error = TextUtils.getErrorCode(code);

        if (error != null) {
            showLongToast(error);
        }
    }

    class WaitingForExit extends AsyncTask<Object, Object, Object> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isWaitingForExit = true;
            showLongToast(getString(R.string.message_back_press_to_exit));

        }

        @Override
        protected Object doInBackground(Object... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            isWaitingForExit = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showConfirmExitApp();
    }
}