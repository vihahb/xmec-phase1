package com.xtelsolution.xmec.view.widget;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.xmec.R;

/**
 * Created by vivu on 12/8/17
 * xtel.vn
 */

public class BaseWarning {

    private ProgressBar progressBarLoading;
    private TextView tv_message;
    private ImageView img_mesage;

    public BaseWarning(View view) {
        progressBarLoading = view.findViewById(R.id.progressBarLoading);
        tv_message = view.findViewById(R.id.tv_message);
        img_mesage = view.findViewById(R.id.img_message);
    }

    public ProgressBar getProgressBarLoading() {
        return progressBarLoading;
    }

    public void setProgressBarLoading(ProgressBar progressBarLoading) {
        this.progressBarLoading = progressBarLoading;
    }

    public TextView getTv_message() {
        return tv_message;
    }

    public void setTv_message(TextView tv_message) {
        this.tv_message = tv_message;
    }

    public ImageView getImg_mesage() {
        return img_mesage;
    }

    public void setImg_mesage(ImageView img_mesage) {
        this.img_mesage = img_mesage;
    }

    public void showDataWarning(int resource, String message_warning){
        img_mesage.setVisibility(View.VISIBLE);
        tv_message.setVisibility(View.VISIBLE);
        progressBarLoading.setVisibility(View.GONE);

        if (resource != -1){
            img_mesage.setImageResource(resource);
        }

        if (!message_warning.isEmpty()){
            tv_message.setText(message_warning);
        }
    }

    public void showDataLoading(String message_warning){
        img_mesage.setVisibility(View.GONE);
        tv_message.setVisibility(View.VISIBLE);
        progressBarLoading.setVisibility(View.VISIBLE);

        if (!message_warning.isEmpty()){
            tv_message.setText(message_warning);
        }
    }

    public void hiddenData(){
        img_mesage.setVisibility(View.GONE);
        tv_message.setVisibility(View.GONE);
        progressBarLoading.setVisibility(View.GONE);
    }
}
