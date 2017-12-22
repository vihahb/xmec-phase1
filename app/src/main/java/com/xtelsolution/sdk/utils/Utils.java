package com.xtelsolution.sdk.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xtelsolution.MyApplication;
import com.xtelsolution.xmec.R;

import java.util.List;

import static com.xtelsolution.MyApplication.context;

/**
 * Created by vivhp on 11/14/2017.
 */

public class Utils {
    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }


    //    Open setting of current app
    public static Intent newAppDetailsIntent(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            return intent;
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");
            intent.putExtra("pkg", context.getPackageName());
            return intent;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName("com.android.settings",
                "com.android.settings.InstalledAppDetails");
        intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        return intent;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void showToast(String ms) {
        Toast.makeText(context, ms, Toast.LENGTH_SHORT).show();
    }

    public static void showImage(final Activity activity, final String url){
        final Dialog imageDialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        imageDialog.setContentView(activity.getLayoutInflater().inflate(R.layout.image_dialog
                , null));
        imageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageDialog.setCancelable(true);
        final LinearLayout ln_loading = imageDialog.findViewById(R.id.ln_loading);
        final ImageView imageView = imageDialog.findViewById(R.id.img_image);
        Button btnClose= imageDialog.findViewById(R.id.btn_close);
        final ProgressBar progressBarLoading = imageDialog.findViewById(R.id.progressBarLoading);
        final TextView tv_state = imageDialog.findViewById(R.id.tv_state);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog.dismiss();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(activity)
                        .load(url).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        ln_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBarLoading.setVisibility(View.GONE);
                        tv_state.setText("Có lỗi xảy ra. Vui lòng thử lại sau!");
                    }
                });
            }
        }, 1000);
        imageDialog.show();
    }

    public static void showImageDrawable(final Activity activity, final int resource){
        final Dialog imageDialog = new Dialog(activity, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        imageDialog.setContentView(activity.getLayoutInflater().inflate(R.layout.image_dialog
                , null));
        imageDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageDialog.setCancelable(true);
        final LinearLayout ln_loading = imageDialog.findViewById(R.id.ln_loading);
        final ImageView imageView = imageDialog.findViewById(R.id.img_image);
        Button btnClose= imageDialog.findViewById(R.id.btn_close);
        final ProgressBar progressBarLoading = imageDialog.findViewById(R.id.progressBarLoading);
        final TextView tv_state = imageDialog.findViewById(R.id.tv_state);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDialog.dismiss();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Picasso.with(activity)
                        .load(resource).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        ln_loading.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBarLoading.setVisibility(View.GONE);
                        tv_state.setText("Có lỗi xảy ra. Vui lòng thử lại sau!");
                    }
                });
            }
        }, 1000);
        imageDialog.show();
    }
}
