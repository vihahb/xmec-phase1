package com.xtelsolution.xmec.view.fragment.detailhr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.callback.HealthRecordOptionListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.callback.ReminderItemClickListener;
import com.xtelsolution.sdk.callback.TimePickerListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.DialogUtils;
import com.xtelsolution.sdk.utils.SizeUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.detailhr.ReminderHrAdapter;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.presenter.schedule.ReminderHrPresenter;
import com.xtelsolution.xmec.view.activity.detailhr.DetailHrActivity;
import com.xtelsolution.xmec.view.activity.schedule.CreateScheduleActivity;
import com.xtelsolution.xmec.view.activity.schedule.DetailScheduleActivity;
import com.xtelsolution.xmec.view.activity.schedule.UpdateScheduleActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.schedule.IReminderHrView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/31/2017
 * Email: leconglongvu@gmail.com
 */
public class ReminderHrFragment extends BasicFragment implements IReminderHrView {
    private ReminderHrPresenter presenter;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    Unbinder unbinder;

    private ReminderHrAdapter adapter;

    private HealthRecords healthRecords;

    private final int REQUEST_ADD = 1;
    private final int REQUEST_VIEW = 2;

    public static ReminderHrFragment newInstance() {
        return new ReminderHrFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_hr, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ReminderHrPresenter(this);

        getData();
    }

    private void getData() {
        healthRecords = getHealthRecords();

        if (healthRecords == null)
            return;

        initRecyclerview();
        initActionListener();
    }

    private void initRecyclerview() {
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        adapter = new ReminderHrAdapter(this, healthRecords.getEndDateLong());
        recyclerview.setAdapter(adapter);

        adapter.setListUserDrug(healthRecords.getUserDrugs());
        adapter.setListData(healthRecords.getScheduleDrugs());
    }

    private void initActionListener() {
        if (healthRecords.getEndDateLong() > 0) {
            fabAdd.setVisibility(View.GONE);
        }

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    showFab(false);
                } else if (dy < 0) {
                    showFab(true);
                }
            }
        });

        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                ScheduleDrug scheduleDrug = (ScheduleDrug) object;

                switch (position) {
                    case -2:
                        Intent intent = new Intent(getActivity(), DetailScheduleActivity.class);
                        intent.putExtra(Constants.OBJECT, scheduleDrug.getId());
                        intent.putExtra(Constants.OBJECT_2, healthRecords.getEndDateLong());
                        startActivityForResult(intent, REQUEST_VIEW);
                        break;
                    case -1:
                        confirmChganeAlarm(scheduleDrug);
                        break;
                    default:
                        showScheduleOption(position, scheduleDrug);
                        break;
                }
            }
        });

        adapter.setReminderItemClickListener(new ReminderItemClickListener() {
            @Override
            public void onItemClick(int parentPos, int childPos, Object object) {
                setTimePerDay(parentPos, childPos, (ScheduleTimePerDay) object);
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(CreateScheduleActivity.class, Constants.OBJECT, healthRecords.getId(), REQUEST_ADD);
            }
        });
    }

    private void showScheduleOption(final int position, final ScheduleDrug scheduleDrug) {
        DialogUtils.showUserDrugOption(getContext(), new HealthRecordOptionListener() {
            @Override
            public void onClicked(int type) {
                switch (type) {
                    case 3:
                        startActivityForResult(UpdateScheduleActivity.class, Constants.OBJECT, scheduleDrug.getId(), REQUEST_VIEW);
                        break;
                    case 4:
                        confirmDeleteSchedule(position, scheduleDrug);
                        break;
                }
            }
        });
    }

    private void confirmChganeAlarm(final ScheduleDrug scheduleDrug) {
        String message;

        if (scheduleDrug.getState() == 1) {
            message = "Bạn muốn tắt lịch nhắc nhở này?";
        } else {
            message = "Bạn muốn bật lịch nhắc nhở này?";
        }

        showMaterialDialog(true, true, null, message, getString(R.string.layout_disagree), getString(R.string.layout_agree), new DialogListener() {
            @Override
            public void negativeClicked() {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void positiveClicked() {
                presenter.changeAlarm(scheduleDrug);
            }
        });
    }

    private void confirmDeleteSchedule(final int position, final ScheduleDrug scheduleDrug) {

        showConfirmDialog(null, "Bạn muốn xóa lịch nhắc này?", new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                presenter.deleteSchedule(position, scheduleDrug.getId());
            }
        });
    }

    private void setTimePerDay(final int parentPos, final int childPos, final ScheduleTimePerDay scheduleTimePerDay) {
        final String oldTime;

        if (scheduleTimePerDay.getScheduleType() == 1) {
            oldTime = scheduleTimePerDay.getStartTime();
        } else {
            if (childPos == 0) {
                oldTime = scheduleTimePerDay.getStartTime();
            } else {
                oldTime = scheduleTimePerDay.getEndTime();
            }
        }

        TimeUtils.timePicker(getContext(), TimeUtils.convertTimeToLong(oldTime), new TimePickerListener() {
            @Override
            public void onTimeSet(String time, long miliseconds) {
                ScheduleDrug scheduleDrug = adapter.getListData().get(parentPos);

                presenter.changeTimeAlarm(scheduleDrug, oldTime, childPos, time);
            }
        });
    }

    private void onCreatedSchedule(Intent data) {
        int id = getIntExtra(Constants.OBJECT, data);

        if (id != -1) {
            presenter.getUserSchedule(id, 1);
        }
    }

    private void onUpdatedSchedule(Intent data) {
        int id = getIntExtra(Constants.OBJECT, data);

        if (id != -1) {
            presenter.getUserSchedule(id, 2);
        }
    }

    private void showFab(boolean isShow) {
        if (isShow) {
            WidgetUtils.slideView(fabAdd, 0);
        } else {
            WidgetUtils.slideView(fabAdd, fabAdd.getHeight() + SizeUtils.dpToPx(getContext(), 16));
        }
    }

    private HealthRecords getHealthRecords() {
        return ((DetailHrActivity) getActivity()).getHealthRecords();
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 300:
                showLongToast("Khoảng cách thời gian không hợp lệ");
                break;
        }
    }

    @Override
    public void onUpdateSuccess(ScheduleDrug scheduleDrug) {
        closeProgressBar();
        adapter.updateItem(scheduleDrug);

        String message;

        if (scheduleDrug.getState() == 1) {
            message = "Bật lịch nhắc nhở thành công";
        } else {
            message = "Tắt lịch nhắc nhở thành công?";
        }

        showLongToast(message);
    }

    @Override
    public void onUpdateTimeSuccess(ScheduleDrug scheduleDrug) {
        adapter.updateItem(scheduleDrug);

        closeProgressBar();
        showLongToast("Cập nhật thành công");
    }

    @Override
    public void onUpdateTimeError(ScheduleDrug scheduleDrug) {
        adapter.updateItem(scheduleDrug);

        closeProgressBar();
        showLongToast("Cập nhật không thành công");
    }

    @Override
    public void onDeleteSuccess(int position) {
        closeProgressBar();
        showLongToast("Xóa lịch nhắc thành công");
        adapter.deleteItem(position);
        WidgetUtils.slideView(fabAdd, 0);
    }

    @Override
    public void onGetUserSchedule(ScheduleDrug scheduleDrug, int type) {
        if (type == 1) {
            adapter.addItem(scheduleDrug);
        } else {
            adapter.updateItem(scheduleDrug);
        }

        getHealthRecords().getScheduleDrugs().clear();
        getHealthRecords().getScheduleDrugs().addAll(adapter.getListData());
    }

    @Override
    public void showProgressBar(int type) {
        String message;

        switch (type) {
            case 1:
                message = "Đang bật lịch nhắc…";
                break;
            case 2:
                message = "Đang tắt lịch nhắc…";
                break;
            case 3:
                message = "Đang cập nhật thời gian…";
                break;
            case 4:
                message = "Đang xóa lịch nhắc…";
                break;
            default:
                message = getString(R.string.doing_do);
        }

        showProgressBar(false, false, message);
    }

    @Override
    public RecyclerView getRecyclerview() {
        return recyclerview;
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == RESULT_OK) {
                    onCreatedSchedule(data);
                }
                break;
            case REQUEST_VIEW:
                if (resultCode == RESULT_OK) {
                    onUpdatedSchedule(data);
                }
                break;
        }
    }
}