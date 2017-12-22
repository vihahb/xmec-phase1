package com.xtelsolution.xmec.view.activity.hr;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.AddImageAdapter;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.RESP_Image;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.detailhr.ViewImageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailDrugActivity extends BasicActivity {

    @BindView(R.id.edt_name)
    AppCompatAutoCompleteTextView edtName;
    @BindView(R.id.tl_note)
    TextInputLayout tlNote;
    @BindView(R.id.edt_note)
    TextInputEditText edtNote;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_cancel)
    Button btnCancel;

    private AddImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_drug);
        ButterKnife.bind(this);

        initRecyclerview();
        getData();
        initActionListener();
    }

    /**
     * Khởi tạo view hiển thị danh sách ảnh
     */
    private void initRecyclerview() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new AddImageAdapter(true);
        recyclerview.setAdapter(adapter);

        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                RESP_Image respImage = new RESP_Image(adapter.getListData());

                Intent intent = new Intent(DetailDrugActivity.this, ViewImageActivity.class);
                intent.putExtra(Constants.OBJECT, respImage);
                intent.putExtra(Constants.OBJECT_2, position);

                startActivity(intent);
            }
        });
    }

    private void getData() {
        int id = -1;

        try {
            id = getIntent().getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (id != -1) {
            getUserDrug(id);
        } else {
            showErrorGetData(null);
        }
    }

    private void getUserDrug(int id) {

        new GetObjectByKeyModel<UserDrugs>(UserDrugs.class, "id", id) {
            @Override
            protected void onSuccess(UserDrugs object) {
                if (object == null)
                    showErrorGetData(null);
                else
                    setData(object);
            }
        };
    }

    private void setData(UserDrugs userDrugs) {
        if (userDrugs == null)
            return;

        edtName.setText(userDrugs.getDrugName());
        edtNote.setText(userDrugs.getNote());

        int visibility = TextUtils.isEmpty(userDrugs.getNote()) ? View.GONE : View.VISIBLE;
        tlNote.setVisibility(visibility);

        if (userDrugs.getUserDrugImages() != null)
            adapter.setListDrugData(userDrugs.getUserDrugImages());
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