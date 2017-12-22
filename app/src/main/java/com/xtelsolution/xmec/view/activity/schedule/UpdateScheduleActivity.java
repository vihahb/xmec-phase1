package com.xtelsolution.xmec.view.activity.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.DatePickerListener;
import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.callback.HealthRecordOptionListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.callback.SelectDayListener;
import com.xtelsolution.sdk.callback.TimePickerListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.DialogUtils;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.hr.ScheduleSpinnerAdapter;
import com.xtelsolution.xmec.adapter.schedule.ScheduleTimePerDayAdapter;
import com.xtelsolution.xmec.adapter.schedule.UserDrugScheduleAdapter;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.presenter.detailhr.UpdateSchedulePresener;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.schedule.IUpdateScheduleView;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class UpdateScheduleActivity extends BasicActivity implements View.OnClickListener, IUpdateScheduleView {
    private UpdateSchedulePresener presener;

    @BindView(R.id.edt_name)
    TextInputEditText edtName;
    @BindView(R.id.edt_date_create)
    TextInputEditText edtDateCreate;
    @BindView(R.id.sp_time_per_day)
    AppCompatSpinner spTimePerDay;
    @BindView(R.id.rv_time_per_day)
    RecyclerView rvTimePerDay;
    @BindView(R.id.rd_day_duration_1)
    RadioButton rdDayDuration1;
    @BindView(R.id.rd_day_duration_2)
    RadioButton rdDayDuration2;
    @BindView(R.id.rd_period_1)
    RadioButton rdPeriod1;
    @BindView(R.id.rd_period_2)
    RadioButton rdPeriod2;
    @BindView(R.id.rd_period_3)
    RadioButton rdPeriod3;
    @BindView(R.id.rd_notice_1)
    RadioButton rdNotice1;
    @BindView(R.id.rd_notice_2)
    RadioButton rdNotice2;
    @BindView(R.id.rd_notice_3)
    RadioButton rdNotice3;
    @BindView(R.id.btn_add_drug)
    Button btnAddDrug;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    public static ScheduleDrug scheduleDrug;

    private ScheduleTimePerDayAdapter perDayAdapter;
    private UserDrugScheduleAdapter drugAdapter;

    private boolean isSetData = false;

    private final int REQUEST_ADD_DRUG = 1;
    private final int REQUEST_UPDATE_DRUG = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule);
        ButterKnife.bind(this);

        presener = new UpdateSchedulePresener(this);

        initToolbar(R.id.toolbar, "Sửa lịch nhắc");
        initListTimePerDay();
        initTimePerDay();
        initRdListener();
        initRecycelerView();
        initActionListener();
        getData();
        KeyboardUtils.autoHideKeboard(UpdateScheduleActivity.this, findViewById(R.id.rootView));
    }

    private void getData() {
        int id = getIntExtra(Constants.OBJECT);

        if (id != -1) {
            presener.getData(id);
        } else {
            showErrorGetData(null);
        }
    }

    private void initListTimePerDay() {
        rvTimePerDay.setHasFixedSize(false);
        rvTimePerDay.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        perDayAdapter = new ScheduleTimePerDayAdapter();
        rvTimePerDay.setAdapter(perDayAdapter);

        perDayAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                ScheduleTimePerDay scheduleTimePerDay = (ScheduleTimePerDay) object;
                selectTimePerDayTime(position, scheduleTimePerDay.getStartTime());
            }
        });
    }

    private void initTimePerDay() {
        String[] list = getResources().getStringArray(R.array.timePerDay);
        ScheduleSpinnerAdapter spinnerAdapter = new ScheduleSpinnerAdapter(list);

        spTimePerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (isSetData) {
                    if (i < 5) {
                        perDayAdapter.addItem((i + 1), 1);
                    } else {
                        perDayAdapter.addItem((i - 4), 2);
                    }
                } else {
                    isSetData = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spTimePerDay.setAdapter(spinnerAdapter);
    }

    private void initRdListener() {
        rdDayDuration2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    scheduleDrug.setDayDuration(0);
                    rdDayDuration1.setText(("Trong số ngày:"));
                }
            }
        });

        rdPeriod1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    scheduleDrug.setPeriodType(1);
                    clearPeriod();
                }
            }
        });
    }

    private void initRecycelerView() {
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        drugAdapter = new UserDrugScheduleAdapter();
        drugAdapter.setShowType(2);
        recyclerView.setAdapter(drugAdapter);

        drugAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                UserDrugSchedule userDrugSchedule = (UserDrugSchedule) object;

                if (position == -1) {
                    showDrugOption(userDrugSchedule);
                } else {
                    startActivity(DetailDrugScheduleActivity.class, Constants.OBJECT, userDrugSchedule.getId());
                }
            }
        });
    }

    private void setData() {
        edtName.setText(scheduleDrug.getName());
        edtDateCreate.setText(TimeUtils.convertLongToDate(scheduleDrug.getDateCreateLong()));

        if (scheduleDrug.getDayDuration() == 0) {
            rdDayDuration2.setChecked(true);
        } else {
            rdDayDuration1.setChecked(true);
            String message1 = "Trong số ngày: " + scheduleDrug.getDayDuration() + " ngày";
            rdDayDuration1.setText(message1);
        }

        switch (scheduleDrug.getPeriodType()) {
            case 1:
                rdPeriod1.setChecked(true);
                break;
            case 2:
                rdPeriod2.setChecked(true);
                String message2 = "Cách ngày: " + scheduleDrug.getPeriod() + " ngày";
                rdPeriod2.setText(message2);
                break;
            case 3:
                rdPeriod3.setChecked(true);
                String period = scheduleDrug.getPeriod().replace("1", "CN");
//                period = period.contains("CN") ? period.replace("CN,", "").concat(",CN") : period;
                String message3 = "Uống vào các thứ: " + period;
                rdPeriod3.setText(message3);
                break;
        }

        switch (scheduleDrug.getNotice()) {
            case 1:
                rdNotice1.setChecked(true);
                break;
            case 2:
                rdNotice2.setChecked(true);
                break;
            case 3:
                rdNotice3.setChecked(true);
                break;
        }

        int selection = 0;
        int type = scheduleDrug.getScheduleTimePerDays().get(0).getScheduleType();

        if (type == 1) {
            if (scheduleDrug.getScheduleTimePerDays().size() > 0)
                selection = scheduleDrug.getScheduleTimePerDays().size() - 1;
        } else {
            selection = scheduleDrug.getScheduleTimePerDays().get(0).getTime() + 4;
        }

        spTimePerDay.setSelection(selection);

        drugAdapter.setListData(scheduleDrug.getUserDrugSchedules());
    }

    private void setTimePerDay() {
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

        perDayAdapter.setListData(scheduleDrug.getScheduleTimePerDays());
    }

    private void initActionListener() {
        edtDateCreate.setOnClickListener(this);
        rdDayDuration1.setOnClickListener(this);
        rdPeriod2.setOnClickListener(this);
        rdPeriod3.setOnClickListener(this);
        btnAddDrug.setOnClickListener(this);
    }

    public void selectNotice(View view) {
        switch (view.getId()) {
            case R.id.rd_notice_1:
                scheduleDrug.setNotice(1);
                break;
            case R.id.rd_notice_2:
                scheduleDrug.setNotice(2);
                break;
            case R.id.rd_notice_3:
                scheduleDrug.setNotice(3);
                break;
        }
    }

    private void selectTimePerDayTime(final int position, String time) {
        TimeUtils.timePicker(UpdateScheduleActivity.this, TimeUtils.convertTimeToLong(time), new TimePickerListener() {
            @Override
            public void onTimeSet(String time, long miliseconds) {
                perDayAdapter.updateItemTime(position, time);
            }
        });
    }

    private void selectDayDuration() {
        String currentDay = null;

        if (scheduleDrug.getDayDuration() != 0 && scheduleDrug.getDayDuration() > 0) {
            currentDay = String.valueOf(scheduleDrug.getDayDuration());
        }

        DialogUtils.selectDay(UpdateScheduleActivity.this, "UỐNG TRONG VÒNG", currentDay, new SelectDayListener() {
            @Override
            public void onDismis() {
                switch (scheduleDrug.getDayDuration()) {
                    case 0:
                        rdDayDuration2.setChecked(true);
                        break;
                    default:
                        rdDayDuration1.setChecked(true);
                        break;
                }
            }

            @Override
            public void onSet(String text) {
                String message = "Trong số ngày: " + text + " ngày";
                rdDayDuration1.setText(message);
                scheduleDrug.setDayDuration(Integer.parseInt(text));
            }
        });
    }

    private void selectPeriodDay() {
        String currentDay = null;

        if (scheduleDrug.getPeriodType() == 2) {
            currentDay = scheduleDrug.getPeriod();
        }

        DialogUtils.selectDay(UpdateScheduleActivity.this, "UỐNG CÁCH NGÀY", currentDay, new SelectDayListener() {
            @Override
            public void onDismis() {
                switch (scheduleDrug.getPeriodType()) {
                    case 1:
                        rdPeriod1.setChecked(true);
                        break;
                    case 2:
                        rdPeriod2.setChecked(true);
                        break;
                    case 3:
                        rdPeriod3.setChecked(true);
                        break;
                }
            }

            @Override
            public void onSet(String text) {
                String message = "Cách ngày: " + text + " ngày";
                rdPeriod2.setText(message);
                scheduleDrug.setPeriodType(2);
                scheduleDrug.setPeriod(String.valueOf(text));
                clearPeriod();
            }
        });
    }

    private void selectPeriodWeek() {
        DialogUtils.selectPeriodWeek(UpdateScheduleActivity.this, scheduleDrug.getPeriod(), new SelectDayListener() {
            @Override
            public void onDismis() {
                switch (scheduleDrug.getPeriodType()) {
                    case 1:
                        rdPeriod1.setChecked(true);
                        break;
                    case 2:
                        rdPeriod2.setChecked(true);
                        break;
                    case 3:
                        rdPeriod3.setChecked(true);
                        break;
                }
            }

            @Override
            public void onSet(String text) {
                scheduleDrug.setPeriodType(3);
                scheduleDrug.setPeriod(text);

                String message = "Uống vào các thứ: " + text;
                rdPeriod3.setText(message.replace("1", "CN"));

                clearPeriod();
            }
        });
    }

    private void clearPeriod() {
        switch (scheduleDrug.getPeriodType()) {
            case 1:
                rdPeriod2.setText(("Cách ngày:"));
                rdPeriod3.setText(("Uống vào các thứ:"));
                scheduleDrug.setPeriod(null);
                break;
            case 2:
                rdPeriod3.setText(("Uống vào các thứ:"));
                break;
            case 3:
                rdPeriod2.setText(("Cách ngày:"));
                break;
        }
    }

    private void setDateCreate() {
        TimeUtils.datePicker(UpdateScheduleActivity.this, edtDateCreate.getText().toString(), false, new DatePickerListener() {
            @Override
            public void onTimeSet(String date, long miliseconds) {
                edtDateCreate.setText(date);
                scheduleDrug.setDateCreateLong(miliseconds);
            }
        });
    }

    private void updateSchedule() {
        scheduleDrug.setName(edtName.getText().toString().trim());
        scheduleDrug.setDateCreateLong(TimeUtils.convertDateToLong(edtDateCreate.getText().toString()));
//        scheduleDrug.setScheduleTimePerDays(perDayAdapter.getListData());
        presener.updateSchedule();
    }

    private void onGetDrugSchedule(Intent data, int type) {
        int id = getIntData(Constants.OBJECT, data);

        if (id != -1) {
            presener.getDrugSchedule(id, type);
        }
    }

    private void showDrugOption(final UserDrugSchedule userDrugSchedule) {
        DialogUtils.showUserDrugOption(UpdateScheduleActivity.this, new HealthRecordOptionListener() {
            @Override
            public void onClicked(int type) {
                switch (type) {
                    case 3:
                        startActivityForResult(UpdateDrugScheduleActivity.class, Constants.OBJECT, userDrugSchedule.getId(), REQUEST_UPDATE_DRUG);
                        break;
                    case 4:
                        showConfirmDeleteDrug(userDrugSchedule);
                        break;
                }
            }
        });
    }

    private void showConfirmDeleteDrug(final UserDrugSchedule userDrugSchedule) {
        showConfirmDialog(null, "Bạn muốn xóa thuốc này?", new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                presener.deleteDrugSchedule(userDrugSchedule);
            }
        });
    }

    private void onScheduleChange() {
        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, scheduleDrug.getId());
        setResult(RESULT_OK, intent);
    }

    /**
     * Thông báo liên kết tài khoản không tồn tại
     */
    private void onUpdateTimePerDayError() {
        showMaterialDialog(false, false, null,
                "Cập nhật giờ uống không thành công. Bạn có muốn thử lại?",
                getString(R.string.layout_disagree), getString(R.string.layout_agree),
                new DialogListener() {
                    @Override
                    public void negativeClicked() {
                        finish();
                    }

                    @Override
                    public void positiveClicked() {
                        showProgressBar(false, false, getString(R.string.doing_mbr_link));
                        presener.checkDeleteTimePerDay();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_date_create:
                setDateCreate();
                break;
            case R.id.rd_day_duration_1:
                selectDayDuration();
                break;
            case R.id.rd_period_2:
                selectPeriodDay();
                break;
            case R.id.rd_period_3:
                selectPeriodWeek();
                break;
            case R.id.btn_add_drug:
                Intent intent = new Intent(this, AddDrugScheduleActivity.class);

                intent.putExtra(Constants.OBJECT, scheduleDrug.getHealthRecordId());
                intent.putExtra(Constants.OBJECT_2, scheduleDrug.getId());

                startActivityForResult(intent, REQUEST_ADD_DRUG);
                break;
        }
    }

    @Override
    public void onGetScheduleSuccess(ScheduleDrug scheduleDrug) {
        if (scheduleDrug != null) {
            UpdateScheduleActivity.scheduleDrug = scheduleDrug;
            setData();
            setTimePerDay();
        } else
            showErrorGetData(null);
    }

    @Override
    public void onGetListDrugSuccess(RealmList<UserDrugs> realmList) {
        drugAdapter.setListUserDrug(realmList);
    }

    @Override
    public void onDeleteDrugUserSuccess(UserDrugSchedule userDrugSchedule) {
        closeProgressBar();
        drugAdapter.deleteItem(userDrugSchedule);
        scheduleDrug.getUserDrugSchedules().remove(userDrugSchedule);
        showLongToast(("Xóa thuốc thành công"));

        onScheduleChange();
    }

    @Override
    public void onGetDrugScheduleSuccess(UserDrugSchedule userDrugSchedule, int type) {
        if (type == 1) {
            drugAdapter.addItem(userDrugSchedule);
        } else {
            drugAdapter.updateItem(userDrugSchedule);
        }

        scheduleDrug.getUserDrugSchedules().clear();
        scheduleDrug.getUserDrugSchedules().addAll(drugAdapter.getListData());

        onScheduleChange();
    }

    @Override
    public void onUpdateScheduleSuccess() {
        closeProgressBar();
        onScheduleChange();
        finish();
    }

    @Override
    public void showProgressBar(int type) {
        String message;

        switch (type) {
            case 1:
                message = "Đang cập nhật lịch nhắc…";
                break;
            case 2:
                message = "Đang xóa thuốc…";
                break;
            default:
                message = getString(R.string.doing_do);
                break;
        }

        showProgressBar(false, false, message);
    }

    @Override
    public ScheduleDrug getScheduleDrug() {
        return scheduleDrug;
    }

    @Override
    public RealmList<ScheduleTimePerDay> getTimePerDay() {
        return perDayAdapter.getListData();
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 301:
                edtName.requestFocus();
                edtName.setError("Vui lòng nhập tên lịch");
                break;
            case 302:
                edtDateCreate.requestFocus();
                edtDateCreate.setError("Vui lòng chọn ngày bắt đầu");
                break;
            case 303:
                showLongToast("Vui lòng nhập đủ giờ uống thuốc");
                break;
            case 304:
                showLongToast("Khoảng cách thời gian không hợp lệ");
                break;
            case 305:
                onUpdateTimePerDayError();
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_schedule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final MenuItem menuItem = menu.findItem(R.id.action_create);
        FrameLayout rootView1 = (FrameLayout) menuItem.getActionView();
        TextView textView1 = rootView1.findViewById(R.id.txt_title);
        textView1.setText(getString(R.string.menu_schedule_save));

        rootView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_create:
                updateSchedule();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ADD_DRUG:
                if (resultCode == RESULT_OK) {
                    onGetDrugSchedule(data, 1);
                }
                break;
            case REQUEST_UPDATE_DRUG:
                if (resultCode == RESULT_OK) {
                    onGetDrugSchedule(data, 2);
                }
                break;
        }
    }
}