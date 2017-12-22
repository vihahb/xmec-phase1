package com.xtelsolution.xmec.adapter.hr;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.xtelsolution.xmec.R;

/**
 * Created by NutIT on 07/11/2017.
 */

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView nameTxt;

    public MyViewHolder(View itemView) {
        super(itemView);
        nameTxt=  itemView.findViewById(R.id.tv_name_drug_recent);
    }
}
