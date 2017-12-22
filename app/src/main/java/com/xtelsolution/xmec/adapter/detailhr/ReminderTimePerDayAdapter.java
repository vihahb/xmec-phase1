package com.xtelsolution.xmec.adapter.detailhr;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ReminderItemClickListener;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/7/2017
 * Email: leconglongvu@gmail.com
 */
public class ReminderTimePerDayAdapter extends RecyclerView.Adapter<ReminderTimePerDayAdapter.ViewHolder> {
    private List<ScheduleTimePerDay> listData;

    private int parentPos;
    private int type = 0;
    private boolean isShowAction;
    private long endDateLong;

    private ReminderItemClickListener reminderItemClickListener;

    public ReminderTimePerDayAdapter(long endDateLong) {
        this.endDateLong = endDateLong;

        listData = new ArrayList<>();
        parentPos = 0;
        isShowAction = true;
    }

    public void setListData(List<ScheduleTimePerDay> list) {
        listData.clear();
        listData.addAll(list);
        type = listData.size() > 0 ? listData.get(0).getScheduleType() : 1;
        notifyDataSetChanged();
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }

    public void setShowAction(boolean showAction) {
        isShowAction = showAction;
    }

    public void setItemClickListener(ReminderItemClickListener reminderItemClickListener) {
        this.reminderItemClickListener = reminderItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder_time_per_day, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        final ScheduleTimePerDay scheduleTimePerDay;

        if (type == 2) {
            scheduleTimePerDay = listData.get(0);
        } else {
            scheduleTimePerDay = listData.get(pos);
        }

        holder.setData(pos, scheduleTimePerDay);

        if (endDateLong > 0) {
            holder.imgEdit.setVisibility(View.GONE);
        } else {
            holder.imgEdit.setVisibility(View.VISIBLE);

            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (reminderItemClickListener != null)
                        reminderItemClickListener.onItemClick(parentPos, pos, scheduleTimePerDay);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (type == 2)
            return type;
        else
            return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View line;
        private TextView txtTime;
        private TextView txtCount;
        private ImageButton imgEdit;

        public ViewHolder(View itemView) {
            super(itemView);

            line = itemView.findViewById(R.id.view_1);
            txtTime = itemView.findViewById(R.id.txt_time);
            txtCount = itemView.findViewById(R.id.txt_count);
            imgEdit = itemView.findViewById(R.id.img_edit);
        }

        private void setData(int position, ScheduleTimePerDay scheduleTimePerDay) {
            if (position == 0)
                line.setVisibility(View.GONE);
            else
                line.setVisibility(View.VISIBLE);

            if (type == 1) {
                txtTime.setText(scheduleTimePerDay.getStartTime());
                txtCount.setText(("Liều " + (position + 1)));
            } else {
                if (position == 0) {
                    txtTime.setText(scheduleTimePerDay.getStartTime());
                    txtCount.setText(("Giờ bắt đầu"));
                } else {
                    txtTime.setText(scheduleTimePerDay.getEndTime());
                    txtCount.setText(("Giờ kết thúc"));
                }
            }

            if (isShowAction)
                imgEdit.setVisibility(View.VISIBLE);
            else
                imgEdit.setVisibility(View.INVISIBLE);
        }
    }
}