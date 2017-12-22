package com.xtelsolution.xmec.adapter.detailhr;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.callback.ReminderItemClickListener;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.fragment.inf.schedule.IReminderHrView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/6/2017
 * Email: leconglongvu@gmail.com
 */
public class ReminderHrAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private IReminderHrView iView;

    private List<ScheduleDrug> listData;
    private SparseArray<UserDrugs> array;

    private long endDateLong;

    private final int UNSELECTED = -1;
    private int selectedItem = UNSELECTED;

    private final int EMPTY_TYPE = 0;
    private final int ITEM_TYPE = 1;

    private ItemClickListener itemClickListener;
    private ReminderItemClickListener reminderItemClickListener;

    public ReminderHrAdapter(IReminderHrView iView, long endDateLong) {
        this.iView = iView;
        this.endDateLong = endDateLong;

        listData = new ArrayList<>();
        array = new SparseArray<>();
    }

    public void addItem(ScheduleDrug scheduleDrug) {
        listData.add(scheduleDrug);
        notifyDataSetChanged();
    }

    public void updateItem(ScheduleDrug scheduleDrug) {
        for (int i = listData.size() - 1; i >= 0; i--) {
            if (listData.get(i).getId() == scheduleDrug.getId()) {
                listData.remove(i);
                listData.add(i, scheduleDrug);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public void setListUserDrug(RealmList<UserDrugs> userDrug) {
        array.clear();

        for (UserDrugs userDrugs : userDrug) {
            array.put(userDrugs.getId(), userDrugs);
        }

        notifyDataSetChanged();
    }

    public void setListData(List<ScheduleDrug> list) {
        listData.clear();
        listData.addAll(list);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        listData.remove(position);
        notifyDataSetChanged();
    }

    public List<ScheduleDrug> getListData() {
        return listData;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setReminderItemClickListener(ReminderItemClickListener reminderItemClickListener) {
        this.reminderItemClickListener = reminderItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case EMPTY_TYPE:
                return new EmptyHolder(layoutInflater.inflate(R.layout.item_empty_with_guid, parent, false));
            case ITEM_TYPE:
                return new ItemHolder(layoutInflater.inflate(R.layout.item_reminder_hr, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;

            final int pos = position;
            final ScheduleDrug scheduleDrug = listData.get(pos);

            itemHolder.setData(pos, scheduleDrug, reminderItemClickListener);

            if (endDateLong > 0) {
                itemHolder.switchAlarm.setVisibility(View.GONE);
                itemHolder.imgOption.setVisibility(View.GONE);
            } else {
                itemHolder.switchAlarm.setVisibility(View.VISIBLE);
                itemHolder.imgOption.setVisibility(View.VISIBLE);

                itemHolder.switchAlarm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (itemClickListener != null)
                            itemClickListener.onItemClick(-1, scheduleDrug);
                    }
                });

                itemHolder.imgOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (itemClickListener != null)
                            itemClickListener.onItemClick(pos, scheduleDrug);
                    }
                });
            }

            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null)
                        itemClickListener.onItemClick(-2, scheduleDrug);
                }
            });
        } else if (holder instanceof EmptyHolder) {
            EmptyHolder emptyHolde = (EmptyHolder) holder;

            emptyHolde.setData();
        }
    }

    @Override
    public int getItemCount() {
        int count = listData.size();
        return count > 0 ? count : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (listData.size() == 0)
            return EMPTY_TYPE;
        else
            return ITEM_TYPE;
    }

    private class EmptyHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle;
        private TextView txtMessage;
        private LinearLayout layout_1;

        EmptyHolder(View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txt_title);
            txtMessage = itemView.findViewById(R.id.txt_message);
            layout_1 = itemView.findViewById(R.id.layout_1);
        }

        private void setData() {
            txtTitle.setText(("Không có lịch nhắc nào"));

            if (endDateLong > 0) {
                layout_1.setVisibility(View.GONE);
            } else {
                txtMessage.setText(("để thêm lịch nhắc)"));
            }
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ExpandableLayout.OnExpansionUpdateListener {
        private SwitchCompat switchAlarm;
        private ImageButton imgOption;
        private TextView txtName;
        private TextView txtDrugs;
        private TextView txtDate;
        private TextView txtType;
        private RecyclerView recyclerView;
        private ExpandableLayout expandableLayout;

        private int position;

        private ItemHolder(View itemView) {
            super(itemView);

            switchAlarm = itemView.findViewById(R.id.switch_alarm);
            imgOption = itemView.findViewById(R.id.img_option);
            txtName = itemView.findViewById(R.id.txt_name);
            txtDrugs = itemView.findViewById(R.id.txt_drugs);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtType = itemView.findViewById(R.id.txt_type);
            recyclerView = itemView.findViewById(R.id.recyclerview);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);

            setListener();
        }

        private void setListener() {
            expandableLayout.setInterpolator(new OvershootInterpolator());
            expandableLayout.setOnExpansionUpdateListener(this);

            expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
                @Override
                public void onExpansionUpdate(float expansionFraction, int state) {
                    if (position != getAdapterPosition()) {
                        if (state == ExpandableLayout.State.EXPANDED) {
                            expandableLayout.collapse();
                        }
                    }

                    if (state == ExpandableLayout.State.EXPANDED) {
                        WidgetUtils.setTextViewDrawable(txtType, 2, R.mipmap.ic_arrow_up_blue_12);
                    } else {
                        WidgetUtils.setTextViewDrawable(txtType, 2, R.mipmap.ic_arrow_down_blue_12);
                    }
                }
            });

            txtType.setOnClickListener(this);
        }

        private void setData(int pos, ScheduleDrug scheduleDrug, ReminderItemClickListener reminderItemClickListener) {
            this.position = pos;

            if (position == selectedItem) {
                expandableLayout.expand();
            } else {
                expandableLayout.collapse();
            }

            txtName.setText(scheduleDrug.getName());

            String date = "Ngày bắt đầu: " + TimeUtils.convertLongToDate(scheduleDrug.getDateCreateLong());
            String period = "";

            switch (scheduleDrug.getPeriodType()) {
                case 2:
                    period = " (Cách " + scheduleDrug.getPeriod() + " ngày)";
                    break;
                case 3:
                    period = scheduleDrug.getPeriod().replace("1", "CN");
                    period = " (Thứ: " + period + ")";
                    break;
            }

            date += period;

            txtDate.setText(date);

            boolean isAlarm = scheduleDrug.getState() == 1;
            switchAlarm.setChecked(isAlarm);

            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext().getApplicationContext()));

            Collections.sort(scheduleDrug.getScheduleTimePerDays(), new Comparator<ScheduleTimePerDay>() {
                @Override
                public int compare(ScheduleTimePerDay lhs, ScheduleTimePerDay rhs) {
                    try {
                        long time1 = TimeUtils.convertTimeToLong(lhs.getStartTime());
                        long time2 = TimeUtils.convertTimeToLong(rhs.getStartTime());

                        return (int) (time1 - time2);
                    } catch (Exception e) {
                        throw new IllegalArgumentException(e);
                    }
                }
            });

            ReminderTimePerDayAdapter adapter = new ReminderTimePerDayAdapter(endDateLong);
            recyclerView.setAdapter(adapter);
            adapter.setParentPos(pos);
            adapter.setListData(scheduleDrug.getScheduleTimePerDays());

            setDrugs(scheduleDrug.getUserDrugSchedules());
            setType(scheduleDrug.getScheduleTimePerDays());
            adapter.setItemClickListener(reminderItemClickListener);
        }

        private void setDrugs(List<UserDrugSchedule> list) {
            if (list.size() > 0) {
                StringBuilder drugs = new StringBuilder("Thuốc:*");

                for (UserDrugSchedule userDrugSchedule : list) {
                    UserDrugs userDrugs = array.get(userDrugSchedule.getUserDrugId());

                    if (userDrugs != null)
                        drugs.append(", ").append(userDrugs.getDrugName());
                    else
                        drugs.append(", ").append(userDrugSchedule.getDrugName());
                }

                drugs = new StringBuilder(drugs.toString().replace("*,", ""));

                txtDrugs.setText(drugs.toString());
                txtDrugs.setVisibility(View.VISIBLE);
            } else {
                txtDrugs.setVisibility(View.GONE);
            }
        }

        private void setType(List<ScheduleTimePerDay> list) {
            String type;

            if (list.get(0).getScheduleType() == 1) {
                type = list.size() + " lần / ngày";
            } else {
                type = list.get(0).getTime() + " giờ / 1 lần";
            }

            txtType.setText(type);
        }

        @Override
        public void onClick(View view) {
            ItemHolder holder = (ItemHolder) iView.getRecyclerview().findViewHolderForAdapterPosition(selectedItem);

            if (holder != null) {
                holder.expandableLayout.collapse();
                WidgetUtils.setTextViewDrawable(holder.txtType, 2, R.mipmap.ic_arrow_down_blue_12);
            }

            if (position == selectedItem) {
                selectedItem = UNSELECTED;
            } else {
                expandableLayout.expand();
                selectedItem = position;
            }
        }

        @Override
        public void onExpansionUpdate(float expansionFraction, int state) {
            iView.getRecyclerview().smoothScrollToPosition(getAdapterPosition());
        }
    }
}