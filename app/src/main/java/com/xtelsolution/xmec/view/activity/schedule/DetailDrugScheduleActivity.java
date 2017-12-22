package com.xtelsolution.xmec.view.activity.schedule;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.UserDrugSchedule;
import com.xtelsolution.xmec.view.activity.BasicActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailDrugScheduleActivity extends BasicActivity {

    @BindView(R.id.edt_name)
    TextInputEditText edtName;
    @BindView(R.id.edt_amount)
    TextInputEditText edtAmount;
    @BindView(R.id.edt_unit)
    TextInputEditText edtUnit;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private UserDrugSchedule userDrugSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_drug_schedule);
        ButterKnife.bind(this);

        getData();
        initActionListener();
    }

    private void getData() {
        int id = getIntExtra(Constants.OBJECT);

        if (id == -1) {
            showErrorGetData(null);
        } else {
            getUserDrugSchedule(id);
        }
    }

    private void getUserDrugSchedule(int id) {

        new GetObjectByKeyModel<UserDrugSchedule>(UserDrugSchedule.class, "id", id) {
            @Override
            protected void onSuccess(UserDrugSchedule object) {
                if (object != null) {
                    userDrugSchedule = object;
                    setData();
                } else {
                    showErrorGetData(null);
                }
            }
        };
    }

    private void setData() {
        edtName.setText(userDrugSchedule.getDrugName());
        edtAmount.setText(String.valueOf(userDrugSchedule.getAmount()));

        String[] drugUnits = getResources().getStringArray(R.array.drug_unit);
        edtUnit.setText(drugUnits[userDrugSchedule.getUnit()]);
    }

    private void initActionListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}