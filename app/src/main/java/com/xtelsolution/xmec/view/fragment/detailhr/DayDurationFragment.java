package com.xtelsolution.xmec.view.fragment.detailhr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/2/2017
 * Email: leconglongvu@gmail.com
 */
public class DayDurationFragment extends DialogFragment {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edt_day)
    TextInputEditText edtDay;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_add)
    Button btnAdd;
    Unbinder unbinder;

    public static DayDurationFragment newInstance() {
        Bundle args = new Bundle();
        DayDurationFragment fragment = new DayDurationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_day, container, false);

//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAction();
    }

    private void initAction() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });
    }

    private void add() {
        String input = edtDay.getText().toString();

        if (TextUtils.isEmpty(input)) {
            showError("Vui lòng nhập số ngày");
            return;
        }

        int day = getDay(input);

        if (day == -1) {
            showError("Ngày không hợp lệ");
            return;
        }

        if (day == 0) {
            showError("Số ngày phải từ 1 trở lên");
            return;
        }

//        Activity activity = getActivity();

//        if (activity instanceof CreateScheduleActivity) {
//            CreateScheduleActivity scheduleActivity = (CreateScheduleActivity) activity;
//            scheduleActivity.setDayDuration(day);
//        }

//        dismiss();
    }

    private int getDay(String day) {
        try {
            return Integer.parseInt(day);
        } catch (Exception e) {
            return -1;
        }
    }

    private void showError(String message) {
        edtDay.requestFocus();
        edtDay.setError(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}