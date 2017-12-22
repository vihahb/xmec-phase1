package com.xtelsolution.xmec.adapter.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.NewsPortsModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ThanhChung on 04/11/2017.
 */

public class NewsPortsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean loadmore = true;
    private final int ITEM = 1;
    private final int LOAD = 2;
    private Context context;
    private List<NewsPortsModel> list;
    private AdapterView.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NewsPortsAdapter(Context context, List<NewsPortsModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM)
            return new PortsHolder(LayoutInflater.from(context).inflate(R.layout.item_news_post, parent, false));
        else
            return new LoadHolder(LayoutInflater.from(context).inflate(R.layout.item_load_more, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PortsHolder) {
            PortsHolder h = (PortsHolder) holder;
            h.setData(list.get(position));
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, null, position, -1);
                }
            });
        } else {
            if (loadmore) holder.itemView.setVisibility(View.VISIBLE);
            else holder.itemView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position < list.size() ? ITEM : LOAD;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadmore = loadMore;
        notifyItemChanged(list.size());
    }


    class PortsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.titleNews)
        TextView titleNews;
        @BindView(R.id.descriptionNew)
        TextView descriptionNew;

        PortsHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(NewsPortsModel data) {
            try {
                Picasso.with(context).load(data.getImage()).resize(480, 200).centerCrop().into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
                Picasso.with(context).load(R.drawable.default_dot).into(imageView);
            }
            titleNews.setText(data.getTitle() != null ? data.getTitle() : "");
            descriptionNew.setText(data.getDescription() != null ? data.getDescription() : "");
        }
    }


}
