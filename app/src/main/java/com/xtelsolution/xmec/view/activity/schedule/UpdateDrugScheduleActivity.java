package com.xtelsolution.xmec.view.activity.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.schedule.AddScheduleDrugAdapter;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.presenter.schedule.UpdateDrugSchedulePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.schedule.IUpdateDrugScheduleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmList;

public class UpdateDrugScheduleActivity extends BasicActivity implements IUpdateDrugScheduleView {
    private UpdateDrugSchedulePresenter presenter;

    @BindView(R.id.spinner_drug)
    Spinner spinnerDrug;
    @BindView(R.id.edt_amount)
    TextInputEditText edtAmount;
    @BindView(R.id.spinner_unit)
    Spinner spinnerUnit;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private AddScheduleDrugAdapter drugAdapter;

    private UserDrugSchedule userDrugSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug_schedule);
        ButterKnife.bind(this);

        presenter = new UpdateDrugSchedulePresenter(this);

        initDrugs();
        initUnit();
        getData();
        initActionListener();
    }

    private void getData() {
        int id = getIntExtra(Constants.OBJECT);

        if (id != -1) {
            presenter.getData(id);
        } else {
            showErrorGetData(null);
        }
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

    private void setData(UserDrugSchedule object) {
        userDrugSchedule = object;

        btnAdd.setText(("Cập nhật"));

        spinnerDrug.setSelection(userDrugSchedule.getUnit());
        edtAmount.setText(String.valueOf(userDrugSchedule.getAmount()));
    }

    private void setDrugData(RealmList<UserDrugs> realmList) {
        int drugSelection = 0;

        for (int i = realmList.size() - 1; i >= 0; i--) {
            if (realmList.get(i).getId() == userDrugSchedule.getUserDrugId()) {
                drugSelection = i;
                break;
            }
        }

        drugAdapter.setListData(realmList);
        spinnerDrug.setSelection(drugSelection);
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
                updateDrug();
            }
        });
    }

    private void updateDrug() {
        String input = edtAmount.getText().toString();
        UserDrugs userDrugs = (UserDrugs) spinnerDrug.getSelectedItem();
        int unit = spinnerUnit.getSelectedItemPosition();

        presenter.updateDrug(input, userDrugs, unit);
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

        }
    }

    @Override
    public void showProgressBar() {
        showProgressBar(false, false, getString(R.string.doing_update_drug));
    }

    @Override
    public void onGetDrugSuccess(UserDrugSchedule userDrugSchedule) {
        if (userDrugSchedule == null) {
            showErrorGetData(null);
        } else {
            setData(userDrugSchedule);
        }
    }

    @Override
    public void onGetListDrugSuccess(RealmList<UserDrugs> realmList) {
        if (realmList != null) {
            if (realmList.size() > 0) {
                setDrugData(realmList);
            } else {
                showErrorGetData("Không có thuốc cho sổ khám bệnh");
            }
        } else {
            showErrorGetData(null);
        }
    }

    @Override
    public void onUpdateSuccess() {
        closeProgressBar();
        showLongToast(("Cập nhật thuốc thành công"));

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, userDrugSchedule.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public UserDrugSchedule getDrugSchedule() {
        return userDrugSchedule;
    }
}