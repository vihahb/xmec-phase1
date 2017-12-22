package com.xtelsolution.xmec.view.fragment.diseases;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.OnLoadMoreListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.EndlessRecyclerViewScrollListener;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.DiseaseObject;
import com.xtelsolution.xmec.model.resp.RESP_Disease;
import com.xtelsolution.xmec.presenter.disease.DiseaseListPresenter;
import com.xtelsolution.xmec.view.activity.disease.DiseaseDetailActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.widget.BaseWarning;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiseaseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiseaseListFragment extends BasicFragment implements IDiseaseListFragment, OnItemCLick {
    @BindView(R.id.edt_Search_Disease)
    EditText edtSearchDisease;
    @BindView(R.id.disease_searched)
    TextView diseaseSearched;
    @BindView(R.id.tv_recent_disease)
    TextView tvRecentDisease;
    @BindView(R.id.rcv_recent_disease)
    RecyclerView rcvRecentDisease;
    @BindView(R.id.rcv_search_disease)
    RecyclerView rcvSearchDisease;
    Unbinder unbinder;
    @BindView(R.id.action_clear)
    ImageView actionClear;
    DiseaseListPresenter presenter;
    private List<DiseaseObject> recentDisease;
    private AdapterDisease adapter;
    private RecentDiseaseAdapter adapterRecent;
    BaseWarning warning;
    LinearLayoutManager linearLayoutManager;

    int page = 1;
    String key;
    boolean firstRun = true;
    private int oldPost = 0, newPost = 0;

    public DiseaseListFragment() {
        // Required empty public constructor
    }

    public static DiseaseListFragment newInstance() {
        DiseaseListFragment fragment = new DiseaseListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.action_clear)
    public void clear() {
        edtSearchDisease.setText(null);
        actionClear.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DiseaseListPresenter(this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disease_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        warning = new BaseWarning(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initDiseaseName();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onHiddesnData();
                initRecentList();
            }
        }, 1000);
        setupUI(view.findViewById(R.id.rootLayout));

    }

    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        rcvSearchDisease.setLayoutManager(linearLayoutManager);
        adapter = new AdapterDisease(this, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.setLoadMore(true);
                firstRun = false;
                page++;
                presenter.searchDisease(key, page);
            }
        });
        rcvSearchDisease.setHasFixedSize(false);
        rcvSearchDisease.setAdapter(adapter);
        rcvSearchDisease.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager, page) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                adapter.setLoadMore(true);
            }
        });
        warning.showDataLoading("");
        recentDisease = new ArrayList<>();
        rcvRecentDisease.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRecent = new RecentDiseaseAdapter(this, recentDisease);
        rcvRecentDisease.setHasFixedSize(false);
        rcvRecentDisease.setAdapter(adapterRecent);
    }

    private void initRecentList() {
        diseaseSearched.setVisibility(View.GONE);
        rcvSearchDisease.setVisibility(View.GONE);
        try {
            tvRecentDisease.setVisibility(View.VISIBLE);
            rcvRecentDisease.setVisibility(View.VISIBLE);
            String result = SharedUtils.getInstance().getStringValue(Constants.LAST_DISEASE);
            RESP_Disease resp_disease = JsonHelper.getObjectNoException(result, RESP_Disease.class);
            Log.e("RESP_Drug Frag", "initRecentList: " + (resp_disease != null ? resp_disease.toString() : null));

            if ((resp_disease != null ? resp_disease.getData().size() : 0) == 0) {
                tvRecentDisease.setVisibility(View.GONE);
                warning.showDataWarning(-1, getString(R.string.mes_disease_recent_empty));
            } else {
                //Hidden data
                onHiddesnData();
                if (recentDisease.size() == 0) {
                    recentDisease.addAll(resp_disease.getData());
                }
                adapterRecent.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("RESP_Drug Frag", "Exception: " + e.toString());
        }
    }

    private void initDiseaseName() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                diseaseSearched.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                firstRun = true;
                if (editable.toString().equals("") || editable.toString().isEmpty() || editable.length() == 0) {
                    initRecentList();
                    actionClear.setVisibility(View.GONE);
                } else {
                    page = 1;
                    adapter.getList().clear();
                    actionClear.setVisibility(View.VISIBLE);
                    tvRecentDisease.setVisibility(View.GONE);
                    rcvRecentDisease.setVisibility(View.GONE);
                    key = editable.toString().replace(" ", "%");
                    showLoading();
                    presenter.searchDisease(key, page);
                }
            }
        };
        edtSearchDisease.addTextChangedListener(watcher);
        edtSearchDisease.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Send the user message
                    handled = true;
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return handled;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_not_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void showLoading() {
        rcvSearchDisease.setVisibility(View.GONE);
        rcvRecentDisease.setVisibility(View.GONE);
        warning.showDataLoading("Đang tải...");
    }

    @Override
    public void listEmpty(String message) {
        warning.showDataWarning(-1, message);
        rcvRecentDisease.setVisibility(View.GONE);
        rcvSearchDisease.setVisibility(View.GONE);
        tvRecentDisease.setVisibility(View.GONE);
    }

    @Override
    public void onHiddesnData() {
        warning.hiddenData();
    }

    @Override
    public void onHiddesnDataAndShowWarning(String mesage, int resource) {
        warning.showDataWarning(resource, mesage);
    }

    @Override
    public void onGetDiseaseSuccess(final List<DiseaseObject> diseases, final String disease) {
        if (!firstRun) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (diseases.size() > 0) {
                        if (diseases.size() < 20) {
                            adapter.setLoadMore(false);
                        } else {
                            adapter.setLoadMore(true);
                        }
                        oldPost = adapter.getItemCount() - 1;
                        adapter.updateCollection(diseases, false);
                        Log.e("onGetDiseaseSuccess", "load mỏe: " + newPost);
                        newPost = (adapter.getItemCount() - 1) - oldPost;
                    } else {
                        adapter.setLoadMore(false);
                        adapter.notifyDataSetChanged();
                        showLongToast("Không có bệnh mới hơn!");
                    }
                }
            }, 500);
        } else {
            firstRun = false;
            oldPost = adapter.getItemCount() - 1;
            if (diseases.size() < 20) {
                adapter.setLoadMore(false);
            } else {
                adapter.setLoadMore(true);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onHiddesnData();
                    adapter.updateCollection(diseases, true);
                    MyApplication.log("onGetDiseaseSuccess", diseases.toString());
                    rcvRecentDisease.setVisibility(View.GONE);
                    tvRecentDisease.setVisibility(View.GONE);
                    diseaseSearched.setVisibility(View.VISIBLE);
                    rcvSearchDisease.setVisibility(View.VISIBLE);
                }
            }, 1000);
        }
    }

    @Override
    public void onClickItem(int position, DiseaseObject diseaseObject) {
        saveLastList(diseaseObject);
        startActivity(DiseaseDetailActivity.class, Constants.OBJECT, diseaseObject);
    }

    @Override
    public void onDeleteItem() {
        RESP_Disease respDisease = new RESP_Disease(recentDisease);
        String last_result = JsonHelper.toJson(respDisease);
        SharedUtils.getInstance().putStringValue(Constants.LAST_DISEASE, last_result);
        if (adapterRecent.getList().size() == 0) {
            warning.showDataWarning(-1, getString(R.string.mes_disease_recent_empty));
        }
    }

    private void saveLastList(DiseaseObject diseaseObject) {

        if (diseaseObject != null) {
            for (int i = 0; i < recentDisease.size(); i++) {
                if (recentDisease.get(i).getId() == diseaseObject.getId()) {
                    recentDisease.remove(i);
                }
            }

            if (recentDisease.size() >= 10) {
                recentDisease.remove(recentDisease.size() - 1);
                recentDisease.add(0, diseaseObject);
            } else {
                recentDisease.add(0, diseaseObject);
            }
        }

        RESP_Disease respDisease = new RESP_Disease(recentDisease);
        String last_result = JsonHelper.toJson(respDisease);
        SharedUtils.getInstance().putStringValue(Constants.LAST_DISEASE, last_result);
    }
}
