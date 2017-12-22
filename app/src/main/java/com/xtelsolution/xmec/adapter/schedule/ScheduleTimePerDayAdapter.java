package com.xtelsolution.xmec.adapter.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/3/2017
 * Email: leconglongvu@gmail.com
 */
public class ScheduleTimePerDayAdapter extends RecyclerView.Adapter<ScheduleTimePerDayAdapter.ViewHolder> {
    private RealmList<ScheduleTimePerDay> listData;

    private ItemClickListener itemClickListener;

    private int type = 0;

    public ScheduleTimePerDayAdapter() {
        listData = new RealmList<>();
    }

    public void clearData() {
        listData.clear();
        notifyDataSetChanged();
    }

    public void addItem(int count, int type) {
        this.type = type;
        listData.clear();

        if (type == 1) {
            for (int i = 1; i <= count; i++) {
                listData.add(new ScheduleTimePerDay(i, type));
            }
        } else {
            listData.add(new ScheduleTimePerDay(count, type));
        }

        notifyDataSetChanged();
    }

    public void setListData(RealmList<ScheduleTimePerDay> list) {
        type = list.get(0).getScheduleType();

        listData.clear();
        listData.addAll(list);

        notifyDataSetChanged();
    }

    public void updateItemTime(int pos, String time) {
        if (type == 1) {
            listData.get(pos).setStartTime(time);
        } else {
            if (pos == 0) {
                listData.get(0).setStartTime(time);
            } else {
                listData.get(0).setEndTime(time);
            }
        }

        notifyDataSetChanged();
    }

    public RealmList<ScheduleTimePerDay> getListData() {
        return listData;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_time_per_day, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        final ScheduleTimePerDay scheduleTimePerDay;

        if (type == 1) {
            scheduleTimePerDay = listData.get(pos);
        } else {
            scheduleTimePerDay = listData.get(0);
        }

        holder.setData(scheduleTimePerDay, pos);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null)
                    itemClickListener.onItemClick(pos, scheduleTimePerDay);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (type == 2)
            return 2;
        else
            return listData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_title;
        private TextView txt_time;

        ViewHolder(View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            txt_time = itemView.findViewById(R.id.txt_time);
        }

        private void setData(ScheduleTimePerDay scheduleTimePerDay, int position) {
            if (type == 1) {
                String title = "Lần " + (position + 1);
                txt_title.setText(title);
                txt_time.setText(scheduleTimePerDay.getStartTime());
            } else {
                if (position == 0) {
                    txt_title.setText(("Giờ bắt đầu"));
                    txt_time.setText(scheduleTimePerDay.getStartTime());
                } else {
                    txt_title.setText(("Giờ kết thúc"));
                    txt_time.setText(scheduleTimePerDay.getEndTime());
                }
            }
        }
    }
}