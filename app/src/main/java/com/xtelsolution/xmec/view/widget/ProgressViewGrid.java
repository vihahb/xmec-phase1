package com.xtelsolution.xmec.view.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.xmec.R;

/**
 * Created by Lê Công Long Vũ on 11/1/2016
 */

public class ProgressViewGrid {
    private RecyclerView recyclerView;
    private LinearLayout layout_data;
    private ImageView imageView;
    private TextView textView_data;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ProgressViewGrid(Activity activity, View view) {
        if (view == null) {
            layout_data = (LinearLayout) activity.findViewById(R.id.layout_progress_view_data);
            imageView = (ImageView) activity.findViewById(R.id.img_progress_view_data);
            textView_data = (TextView) activity.findViewById(R.id.txt_progress_view_data);
            swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_progress_view);
            recyclerView = (RecyclerView) activity.findViewById(R.id.recyclerview_progress_view);
        } else {
            layout_data = (LinearLayout) view.findViewById(R.id.layout_progress_view_data);
            imageView = (ImageView) view.findViewById(R.id.img_progress_view_data);
            textView_data = (TextView) view.findViewById(R.id.txt_progress_view_data);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_progress_view);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_progress_view);
        }

        setUpSwipeLayout();
    }

    private void setUpSwipeLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
    }

    public void setUpRecyclerView(Context context, final RecyclerView.Adapter adapter) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adapter.getItemViewType(position) == 1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

//    public void onScrollRecyclerview(RecyclerOnScrollListener onScrollListener) {
//        recyclerView.addOnScrollListener(onScrollListener);
//    }

    public void initData(int imageView, String textViewData, String button) {
        if (imageView == -1)
            this.imageView.setVisibility(View.GONE);
        else
            this.imageView.setImageResource(imageView);

        if (textViewData == null)
            this.textView_data.setVisibility(View.GONE);
        else {
            if (button != null)
                textViewData += "\n" + button;
            this.textView_data.setText(textViewData);
        }
    }

    public void updateData(int imageView, String textView, String button) {
        if (imageView == -1)
            this.imageView.setVisibility(View.GONE);
        else
            this.imageView.setImageResource(imageView);

        if (textView == null)
            this.textView_data.setVisibility(View.GONE);
        else {
            if (button != null)
                textView += "\n" + button;
            this.textView_data.setText(textView);
        }
    }

    public void showData() {
        if (recyclerView.getVisibility() == View.GONE)
            recyclerView.setVisibility(View.VISIBLE);
        if (layout_data.getVisibility() == View.VISIBLE)
            layout_data.setVisibility(View.GONE);
    }

    public void hideData() {
        if (recyclerView.getVisibility() == View.VISIBLE)
            recyclerView.setVisibility(View.GONE);
        if (layout_data.getVisibility() == View.GONE)
            layout_data.setVisibility(View.VISIBLE);
    }

    public void setEnableView(boolean isEnable) {
        swipeRefreshLayout.setEnabled(false);
        layout_data.setEnabled(isEnable);
    }

    public void onLayoutClicked(View.OnClickListener onClickListener) {
        layout_data.setOnClickListener(onClickListener);
    }

    public void onRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public void setRefreshing(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    public void onSwipeLayoutPost(Runnable runnable) {
        swipeRefreshLayout.post(runnable);
    }

    public void disableSwipe() {
        swipeRefreshLayout.setEnabled(false);
    }
}