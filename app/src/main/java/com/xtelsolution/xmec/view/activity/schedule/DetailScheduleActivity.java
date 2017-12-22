package com.xtelsolution.xmec.view.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.detailhr.ReminderTimePerDayAdapter;
import com.xtelsolution.xmec.adapter.schedule.UserDrugScheduleAdapter;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.ScheduleDrug;
import com.xtelsolution.xmec.model.entity.ScheduleTimePerDay;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.activity.BasicActivity;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class DetailScheduleActivity extends BasicActivity {

    @BindView(R.id.edt_name)
    TextInputEditText txtName;
    @BindView(R.id.edt_date)
    TextInputEditText txtDate;
    @BindView(R.id.txt_time)
    AppCompatTextView txtThoiGian;
    @BindView(R.id.txt_ngay_uong)
    AppCompatTextView txtNgayUong;
    @BindView(R.id.txt_luu_y)
    AppCompatTextView txtLuuY;
    @BindView(R.id.recyclerview_time)
    RecyclerView recyclerviewTime;
    @BindView(R.id.txt_title)
    AppCompatTextView txtTitle;
    @BindView(R.id.recyclerview_drug)
    RecyclerView recyclerviewDrug;

    private MenuItem menuItem;
    private long endDateLong = -1;
    private ScheduleDrug scheduleDrug;

    private ReminderTimePerDayAdapter timePerDayAdapter;
    private UserDrugScheduleAdapter drugAdapter;

//    private boolean isViewOnly;
    private final int REQUEST_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_schedule);
        ButterKnife.bind(this);

        initToolbar(R.id.toolbar, "Chi tiết lịch nhắc");
        getData();
    }

    private void getData() {
        int id = getIntExtra(Constants.OBJECT);

        try {
            endDateLong = getIntent().getLongExtra(Constants.OBJECT_2, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id != -1) {
            initListPerDay();
            initListDrug();
            getSchedule(id);
        } else {
            showErrorGetData(null);
        }
    }

    private void getSchedule(int id) {

        new GetObjectByKeyModel<ScheduleDrug>(ScheduleDrug.class, "id", id) {
            @Override
            protected void onSuccess(ScheduleDrug object) {
                if (object == null) {
                    showErrorGetData(null);
                } else {
                    scheduleDrug = object;
                    setData();
                    setTimePerDay();
                    getUserDrug(scheduleDrug.getHealthRecordId());

                    if (menuItem != null) {
                        if (endDateLong > 0) {
                            menuItem.setVisible(false);
                        } else {
                            menuItem.setVisible(true);
                        }
                    }
                }
            }
        };
    }

    private void getUserDrug(int hrID) {

        new GetListByKeyModel<UserDrugs>(UserDrugs.class, "healthRecordId", hrID) {
            @Override
            protected void onSuccess(RealmList<UserDrugs> realmList) {
                if (realmList != null) {
                    drugAdapter.setListUserDrug(realmList);
                }
            }
        };
    }

    private void initListPerDay() {
        recyclerviewTime.setHasFixedSize(false);
        recyclerviewTime.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        timePerDayAdapter = new ReminderTimePerDayAdapter(endDateLong);
        timePerDayAdapter.setShowAction(false);
        recyclerviewTime.setAdapter(timePerDayAdapter);
    }

    private void initListDrug() {
        recyclerviewDrug.setHasFixedSize(false);
        recyclerviewDrug.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        drugAdapter = new UserDrugScheduleAdapter();
        recyclerviewDrug.setAdapter(drugAdapter);

        drugAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (position != -1) {
                    UserDrugSchedule userDrugSchedule = (UserDrugSchedule) object;
                    startActivity(DetailDrugScheduleActivity.class, Constants.OBJECT, userDrugSchedule.getId());
                }
            }
        });
    }

    private void setData() {
        txtName.setText(scheduleDrug.getName());
        txtDate.setText(TimeUtils.convertLongToDate(scheduleDrug.getDateCreateLong()));

        String thoiGian;
        if (scheduleDrug.getDayDuration() == 0) {
            thoiGian = "Không giới hạn";
        } else {
            thoiGian = "Trong số ngày: " + scheduleDrug.getDayDuration() + " ngày";
        }
        txtThoiGian.setText(thoiGian);

        String ngayUong = null;
        switch (scheduleDrug.getPeriodType()) {
            case 1:
                ngayUong = "Hằng ngày";
                break;
            case 2:
                ngayUong = "Cách ngày: " + scheduleDrug.getPeriod() + " ngày";
                break;
            case 3:
                String period = scheduleDrug.getPeriod().replace("1", "CN");
                ngayUong = "Uống vào các thứ: " + period;
                break;
        }
        txtNgayUong.setText(ngayUong);

        String luuY = null;
        switch (scheduleDrug.getNotice()) {
            case 1:
                luuY = "Uống sau khi ăn";
                break;
            case 2:
                luuY = "Uống trước khi ăn";
                break;
            case 3:
                luuY = "Uống sau khi ăn 1 tiếng";
                break;
        }
        txtLuuY.setText(luuY);

        if (scheduleDrug.getUserDrugSchedules().size() > 0) {
            txtTitle.setVisibility(View.VISIBLE);
            recyclerviewDrug.setVisibility(View.VISIBLE);
            drugAdapter.setListData(scheduleDrug.getUserDrugSchedules());
        } else {
            txtTitle.setVisibility(View.GONE);
            recyclerviewDrug.setVisibility(View.GONE);
        }
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

        timePerDayAdapter.setListData(scheduleDrug.getScheduleTimePerDays());
    }

    private void updatedSuccess(Intent data) {
        int id = getIntData(Constants.OBJECT, data);

        if (id != -1) {
            getSchedule(id);
        }

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, scheduleDrug.getId());
        setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail_hr, menu);
//        if (isViewOnly)
//            menu.findItem(R.id.action_edit).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menuItem = menu.findItem(R.id.action_edit);

        if (endDateLong > 0) {
            menuItem.setVisible(false);
        } else {
            menuItem.setVisible(true);
        }

        FrameLayout rootView1 = (FrameLayout) menuItem.getActionView();
        TextView textView1 = rootView1.findViewById(R.id.txt_title);
        textView1.setText(getString(R.string.menu_hr_edit));

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
            case R.id.action_edit:
                startActivityForResult(UpdateScheduleActivity.class, Constants.OBJECT, scheduleDrug.getId(), REQUEST_UPDATE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_UPDATE) {
            if (resultCode == RESULT_OK) {
                updatedSuccess(data);
            }
        }
    }
}