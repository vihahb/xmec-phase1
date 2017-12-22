package com.xtelsolution.xmec.view.fragment.news;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.view.widget.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vivu on 12/13/17
 * xtel.vn
 */

public class NewsCategoryAdapter extends RecyclerTabLayout.Adapter<RecyclerTabLayout.ViewHolder> {

    List<Fragments> fragmentsList;
    Context context;
    private ViewPager viewPager;


    public NewsCategoryAdapter(ViewPager viewPager, Context context) {
        super(viewPager);
        this.viewPager = viewPager;
        this.context = context;
        fragmentsList = new ArrayList<>();
    }

    @Override
    public RecyclerTabLayout.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerTabLayout.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder){
            final ViewHolder holder1 = (ViewHolder) holder;
            holder1.setData(fragmentsList.get(position).getTitle());
            holder1.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewPager.setCurrentItem(position);
                    holder1.itemView.setSelected(true);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return fragmentsList.size();
    }

    class ViewHolder extends RecyclerTabLayout.ViewHolder {

        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title_category);
        }

        public void setData(String title) {
            if (TextUtils.isEmpty(title))
                textView.setText(title);
        }
    }

    public void refreshData(List<Fragments> fragmentsList){
        this.fragmentsList.clear();
        this.fragmentsList.addAll(fragmentsList);
        notifyDataSetChanged();
    }
}
