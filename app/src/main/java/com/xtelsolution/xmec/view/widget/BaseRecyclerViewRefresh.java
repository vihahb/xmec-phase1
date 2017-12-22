package com.xtelsolution.xmec.view.widget;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ReloadListener;
import com.xtelsolution.xmec.R;

/**
 * Created by vivu on 11/20/17.
 */

public class BaseRecyclerViewRefresh {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView tv_state;
    ImageView img_message;
    LinearLayout ln_warning;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;

    public BaseRecyclerViewRefresh(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(
                view.getContext().getResources().getColor(R.color.colorPrimaryDark),
                view.getContext().getResources().getColor(R.color.colorAccent),
                view.getContext().getResources().getColor(R.color.colorPrimaryDark));
        ln_warning = view.findViewById(R.id.ln_warning);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        progressBar = view.findViewById(R.id.progressBarLoading);
        tv_state = view.findViewById(R.id.tv_message);
        img_message = view.findViewById(R.id.img_message);
        linearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void clickReload(final ReloadListener reloadListener){
        ln_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reloadListener.OnClick();
            }
        });
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getTv_state() {
        return tv_state;
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public void onRefresh(SwipeRefreshLayout.OnRefreshListener refreshListener){
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
    }

    public void hideRefresh(){
        swipeRefreshLayout.setRefreshing(false);
    }

    public void showData() {
        hideRefresh();
        progressBar.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (recyclerView.getAdapter().getItemCount() == 0) {
            showError("Dữ liệu trống.");
        } else {
            img_message.setVisibility(View.GONE);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
    //Show Data recyclerview
    public void showData(String message) {
        hideRefresh();
        progressBar.setVisibility(View.GONE);
        tv_state.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (recyclerView.getAdapter().getItemCount() == 0) {
            showError("Dữ liệu trống.");
        } else {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    //Show error
    public void showError(String mes) {
        hideRefresh();
        recyclerView.setVisibility(View.GONE);
        tv_state.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        img_message.setVisibility(View.VISIBLE);
        tv_state.setText(mes);
    }

    //Show loading with text
    public void loading() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tv_state.setVisibility(View.VISIBLE);
        img_message.setVisibility(View.GONE);
    }

}
