package com.xtelsolution.xmec.view.fragment.detailhr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.callback.NumberChangeListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.AddImageAdapter;
import com.xtelsolution.xmec.adapter.TagViewAdapter;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.RESP_Image;
import com.xtelsolution.xmec.presenter.detailhr.OverviewHrPresenter;
import com.xtelsolution.xmec.view.activity.detailhr.DetailHrActivity;
import com.xtelsolution.xmec.view.activity.detailhr.ViewImageActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.detailhr.IOverviewHrView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/31/2017
 * Email: leconglongvu@gmail.com
 */
public class OverviewHrFragment extends BasicFragment implements IOverviewHrView {
    private OverviewHrPresenter presenter;

    @BindView(R.id.edt_disease)
    TextInputEditText edtDisease;
    @BindView(R.id.view_1)
    View lineDisease;
    @BindView(R.id.recyclerview_disease)
    RecyclerView recyclerViewDisease;
    @BindView(R.id.edt_start_date)
    TextInputEditText edtStartDate;
    @BindView(R.id.edt_note)
    TextInputEditText edtNote;
    @BindView(R.id.edt_hospital)
    AppCompatAutoCompleteTextView edtHospital;
    @BindView(R.id.edt_doctor)
    TextInputEditText edtDoctor;
    @BindView(R.id.txt_image_title)
    TextView txtImageTitle;
    @BindView(R.id.recyclerview_image)
    RecyclerView recyclerviewImage;
    Unbinder unbinder;

    private TagViewAdapter tagViewAdapter;
    private AddImageAdapter adapterImage;

    private int hrID;

    public static OverviewHrFragment newInstance() {
        return new OverviewHrFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview_hr, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new OverviewHrPresenter(this);

        setDisease();
        setImages();
        getData();
    }

    private void setDisease() {
        recyclerViewDisease.setHasFixedSize(false);
        recyclerViewDisease.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        tagViewAdapter = new TagViewAdapter();
        tagViewAdapter.disableDelete();
        recyclerViewDisease.setAdapter(tagViewAdapter);
    }

    /**
     * Khởi tạo danh sách ảnh
     */
    private void setImages() {
        recyclerviewImage.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        adapterImage = new AddImageAdapter(true);
        recyclerviewImage.setAdapter(adapterImage);

        adapterImage.setNumberChangeListener(new NumberChangeListener() {
            @Override
            public void onChange(int itemCount) {
                int state = itemCount > 0 ? View.VISIBLE : View.GONE;
                txtImageTitle.setVisibility(state);
            }
        });

        adapterImage.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                RESP_Image respImage = new RESP_Image(adapterImage.getListData());

                Intent intent = new Intent(getActivity(), ViewImageActivity.class);
                intent.putExtra(Constants.OBJECT, respImage);
                intent.putExtra(Constants.OBJECT_2, position);

                startActivity(intent);
            }
        });
    }

    private void getData() {
        hrID = ((DetailHrActivity) getActivity()).getHealthRecordsID();

        presenter.getHealthRecord(hrID);
    }

    public void update() {
        presenter.getHealthRecord(hrID);
    }

    private void setData(HealthRecords healthRecords) {
        edtStartDate.setText(TimeUtils.convertLongToDate(healthRecords.getDateCreateLong()));
        edtNote.setText(healthRecords.getNote());
        edtHospital.setText(healthRecords.getHospitalName());
        edtDoctor.setText(healthRecords.getDoctorName());

        adapterImage.setListHrData(healthRecords.getHealthRecordImages());
        tagViewAdapter.setListDisease(healthRecords.getUserDiseases());

        if (tagViewAdapter.getItemCount() > 0) {
            edtDisease.setText(" ");
        } else {
            edtDisease.setText(null);
        }
    }

    @Override
    public void onGetHealthRecordSuccess(HealthRecords healthRecords) {
        setData(healthRecords);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}