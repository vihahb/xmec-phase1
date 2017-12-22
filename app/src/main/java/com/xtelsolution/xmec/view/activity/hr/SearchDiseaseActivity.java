package com.xtelsolution.xmec.view.activity.hr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.hr.SearchDiseaseAdapter;
import com.xtelsolution.xmec.model.entity.UserDiseases;
import com.xtelsolution.xmec.model.resp.RESP_User_Diseases;
import com.xtelsolution.xmec.presenter.hr.DiseaseSearchPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.hr.IDiseaseSearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchDiseaseActivity extends BasicActivity implements IDiseaseSearchView {
    private DiseaseSearchPresenter presenter;

    @BindView(R.id.edt_search)
    TextInputEditText edtSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private SearchDiseaseAdapter adapter;

    private String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_search);
        ButterKnife.bind(this);

        presenter = new DiseaseSearchPresenter(this);

        initRecyclerView();
        addTextChangeListener();
        initActionListener();

        KeyboardUtils.autoHideKeboard(this, findViewById(R.id.rootView));
        KeyboardUtils.showSoftKeyboard(this);
    }

    /**
     * Khởi tạo view hiển thị danh sách bệnh
     */
    private void initRecyclerView() {
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new SearchDiseaseAdapter();
        recyclerview.setAdapter(adapter);
    }

    /**
     * Lăng nghe sụ kiện người dùng nhập tên bệnh
     */
    private void addTextChangeListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String search = editable.toString();

                if (search.length() > 0) {
                    if (!key.equals(search)) {
                        presenter.searchDisease(search);
                    }
                } else {
                    adapter.clearData();
                    progressBar.setVisibility(View.INVISIBLE);
                }

                key = search;
            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    KeyboardUtils.hideSoftKeyboard(getActivity());
                }
                return false;
            }
        });
    }

    /**
     * Khởi tạo sụ kiện lắng nghe người dùng tương tác
     */
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
                add();
            }
        });
    }

    private void add() {
        List<UserDiseases> list = adapter.getListData();
        String diseaseName = edtSearch.getText().toString().trim();

        if (list.size() == 0 && TextUtils.isEmpty(diseaseName)) {
            showLongToast(getString(R.string.error_sd_select));
            return;
        }

        RESP_User_Diseases respUserDiseases = new RESP_User_Diseases();

        if (list.size() == 0) {
            UserDiseases userDiseases = new UserDiseases();
            userDiseases.setId(-1);
            userDiseases.setName(diseaseName);
            userDiseases.setHrId(-1);
            list.add(userDiseases);
        }

        respUserDiseases.setData(list);

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, respUserDiseases);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onGetDiseaseSuccess(List<UserDiseases> diseases, String key) {
        String currentKey = TextUtils.unicodeToKoDauLowerCase(edtSearch.getText().toString());
        currentKey = currentKey.replace(" ", "%20");

        if (key.equals(currentKey)) {
            adapter.setListData(diseases);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        if (code == 404) {
            adapter.clearData();
            showLongToast(getString(R.string.error_sd_not_found));
        }

        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}