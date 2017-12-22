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
import com.xtelsolution.xmec.presenter.detailhr.CreateSchedulePresener;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.schedule.ICreateScheduleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class CreateScheduleActivity extends BasicActivity implements View.OnClickListener, ICreateScheduleView {
    private CreateSchedulePresener presener;

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
    @BindView(R.id.btn_add_drug)
    Button btnAddDrug;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private ScheduleTimePerDayAdapter perDayAdapter;
    private UserDrugScheduleAdapter drugAdapter;

    public static ScheduleDrug scheduleDrug;

    private int hrID;

    private final int REQUEST_DRUG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_schedule);
        ButterKnife.bind(this);

        getData();
    }

    private void getData() {
        try {
            hrID = getIntent().getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (hrID == -1) {
            showErrorGetData(null);
            return;
        }

        presener = new CreateSchedulePresener(this);
        presener.getData(hrID);
    }

    private void initDefaultData() {
        scheduleDrug = new ScheduleDrug();
        scheduleDrug.setDateCreateLong(System.currentTimeMillis());
        scheduleDrug.setDayDuration(0);
        scheduleDrug.setPeriodType(1);
        scheduleDrug.setNotice(1);
        scheduleDrug.setState(1);
        scheduleDrug.setHealthRecordId(hrID);

        edtDateCreate.setText(TimeUtils.getToday());
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
                if (i < 5) {
                    perDayAdapter.addItem((i + 1), 1);
                } else {
                    perDayAdapter.addItem((i - 4), 2);
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
        drugAdapter.setShowType(1);
        recyclerView.setAdapter(drugAdapter);

        drugAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (position == -1) {
                    showDeleteUserDrugSchedule((UserDrugSchedule) object);
                }
            }
        });
    }

    private void initActionListener() {
        edtDateCreate.setOnClickListener(this);
        rdDayDuration1.setOnClickListener(this);
        rdPeriod2.setOnClickListener(this);
        rdPeriod3.setOnClickListener(this);
        btnAddDrug.setOnClickListener(this);
    }

    private void showDeleteUserDrugSchedule(final UserDrugSchedule userDrugSchedule) {
        showConfirmDialog(null, "Bạn muốn xóa thuốc này?", new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                scheduleDrug.getUserDrugSchedules().remove(userDrugSchedule);
                drugAdapter.deleteItem(userDrugSchedule);
                drugAdapter.notifyDataSetChanged();
            }
        });
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
        TimeUtils.timePicker(CreateScheduleActivity.this, TimeUtils.convertTimeToLong(time), new TimePickerListener() {
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

        DialogUtils.selectDay(CreateScheduleActivity.this, "UỐNG TRONG VÒNG", currentDay, new SelectDayListener() {
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

        DialogUtils.selectDay(CreateScheduleActivity.this, "UỐNG CÁCH NGÀY", currentDay, new SelectDayListener() {
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
        DialogUtils.selectPeriodWeek(CreateScheduleActivity.this, scheduleDrug.getPeriod(), new SelectDayListener() {
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
        TimeUtils.datePicker(CreateScheduleActivity.this, edtDateCreate.getText().toString(), false, new DatePickerListener() {
            @Override
            public void onTimeSet(String date, long miliseconds) {
                edtDateCreate.setText(date);
                scheduleDrug.setDateCreateLong(miliseconds);
            }
        });
    }

    private void createSchedule() {
        scheduleDrug.setName(edtName.getText().toString().trim());
        scheduleDrug.setDateCreateLong(TimeUtils.convertDateToLong(edtDateCreate.getText().toString()));
        scheduleDrug.setScheduleTimePerDays(perDayAdapter.getListData());
        presener.createSchedule();
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
                intent.putExtra(Constants.OBJECT, presener.getHealthRecords().getId());
                startActivityForResult(intent, REQUEST_DRUG);
                break;
        }
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
        }
    }

    @Override
    public void onGetDataSuccess(boolean isSuccess) {
        if (!isSuccess) {
            showErrorGetData(null);
            return;
        }

        initToolbar(R.id.toolbar, "Thêm lịch nhắc");
        initDefaultData();
        initListTimePerDay();
        initTimePerDay();
        initRdListener();
        initRecycelerView();
        initActionListener();
        KeyboardUtils.autoHideKeboard(CreateScheduleActivity.this, findViewById(R.id.rootView));
    }

    @Override
    public void onGetListDrug(RealmList<UserDrugs> realmList) {
        drugAdapter.setListUserDrug(realmList);
    }

    @Override
    public void onAddScheduleSuccess(ScheduleDrug scheduleDrug) {
        closeProgressBar();

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, scheduleDrug.getId());
        setResult(RESULT_OK, intent);
        finish();

        showLongToast("Tạo lịch nhắc thành công");
    }

    @Override
    public void showProgressBar() {
        showProgressBar(false, false, "Đang tạo lịch nhắc…");
    }

    @Override
    public ScheduleDrug getScheduleDrug() {
        return scheduleDrug;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scheduleDrug = null;
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
                createSchedule();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_DRUG:
                if (resultCode == RESULT_OK) {
                    drugAdapter.setListData(scheduleDrug.getUserDrugSchedules());
                }
                break;
        }
    }
}