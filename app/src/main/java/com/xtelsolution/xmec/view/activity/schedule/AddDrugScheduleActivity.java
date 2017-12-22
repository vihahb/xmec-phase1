package com.xtelsolution.xmec.view.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.schedule.AddScheduleDrugAdapter;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.presenter.schedule.AddDrugSchedulePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.schedule.IAddDrugScheduleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class AddDrugScheduleActivity extends BasicActivity implements IAddDrugScheduleView {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.spinner_drug)
    Spinner spinnerDrug;
    @BindView(R.id.edt_amount)
    EditText edtAmount;
    @BindView(R.id.spinner_unit)
    Spinner spinnerUnit;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_add)
    Button btnAdd;
    private AddScheduleDrugAdapter drugAdapter;

    private int scheduleID = -1;
    private AddDrugSchedulePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug_schedule);
        ButterKnife.bind(this);

        presenter = new AddDrugSchedulePresenter(this);

        initDrugs();
        initUnit();
        initActionListener();
        getData();
    }

    private void getData() {
        int hrID = getIntExtra(Constants.OBJECT);
        scheduleID = getIntExtra(Constants.OBJECT_2);

        if (hrID == -1) {
            showErrorGetData(null);
        } else {
            presenter.getData(hrID);
        }
    }

    private void setData(RealmList<UserDrugs> realmList) {
        if (realmList.size() == 0) {
            showErrorGetData("Không có thuốc cho sổ khám bệnh");
            return;
        }

        drugAdapter.setListData(realmList);

        edtAmount.requestFocus();
        KeyboardUtils.showSoftKeyboard(this);
    }

    private void initDrugs() {
        drugAdapter = new AddScheduleDrugAdapter("Tên thuốc");
        spinnerDrug.setAdapter(drugAdapter);
    }

    private void initUnit() {
        List<String> listData = new ArrayList<>();
        String[] units = getResources().getStringArray(R.array.drug_unit);

        Collections.addAll(listData, units);

        AddScheduleDrugAdapter unitsAdapter = new AddScheduleDrugAdapter("Đơn vị");
        unitsAdapter.setListStringData(listData);
        spinnerUnit.setAdapter(unitsAdapter);
    }

    private void initActionListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDrugSchedule();
            }
        });
    }

    private void addDrugSchedule() {
        String input = edtAmount.getText().toString();
        UserDrugs userDrugs = (UserDrugs) spinnerDrug.getSelectedItem();
        int unit = spinnerUnit.getSelectedItemPosition();

        presenter.addDrugSchedule(input, userDrugs, unit);
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 301:
                edtAmount.requestFocus();
                edtAmount.setError("Vui lòng nhập liều lượng");
                break;
            case 302:
                edtAmount.requestFocus();
                edtAmount.setError("Liều lượng không hợp lệ");
                break;
            case 303:
                showLongToast("Thuốc đã được thêm vào lịch nhắc");
                break;
            case 304:
                showLongToast("Thuốc đã được thêm vào lịch nhắc");
                break;
        }
    }

    @Override
    public void showProgressBar() {
        showProgressBar(false, false, getString(R.string.doing_create_drug));
    }

    @Override
    public void onGetListDrugSuccess(RealmList<UserDrugs> realmList) {
        if (realmList == null) {
            showErrorGetData(null);
        } else {
            setData(realmList);
        }
    }

    @Override
    public void onAddDrugSuccess(UserDrugSchedule userDrugSchedule) {
        KeyboardUtils.hideSoftKeyboard(AddDrugScheduleActivity.this);

        closeProgressBar();

        if (scheduleID == -1) {
            CreateScheduleActivity.scheduleDrug.getUserDrugSchedules().add(userDrugSchedule);
        }

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, userDrugSchedule.getId());
        setResult(RESULT_OK, intent);
        finish();

        showLongToast("Thêm thuốc thành công");
    }

    @Override
    public int getScheduleDrugID() {
        return scheduleID;
    }
}