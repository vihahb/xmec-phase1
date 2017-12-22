package com.xtelsolution.xmec.view.fragment.searchDrug;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.OnLoadMoreListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.EndlessRecyclerViewScrollListener;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.hr.RecentDrugAdapter;
import com.xtelsolution.xmec.adapter.hr.SearchNameDrugAdapter;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.DrugSearchEntity;
import com.xtelsolution.xmec.model.resp.DrugRecent;
import com.xtelsolution.xmec.model.resp.RESP_SearchDrug;
import com.xtelsolution.xmec.presenter.search.DrugSearchPresenter;
import com.xtelsolution.xmec.view.activity.drug.DrugDetailActivity;
import com.xtelsolution.xmec.view.activity.inf.IClickItem;
import com.xtelsolution.xmec.view.activity.inf.search.IDrugSearch;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.widget.BaseWarning;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vivu on 05/11/2017
 */

public class DrugSearchFragment extends BasicFragment implements IDrugSearch, IClickItem {
    @BindView(R.id.edt_Search_Drug)
    TextInputEditText edtSearchDrug;
    @BindView(R.id.rcv_search_drug)
    RecyclerView rcvSearchDrug;
    @BindView(R.id.rcv_recent_drug)
    RecyclerView rcvRecentDrug;
    @BindView(R.id.tv_recent_drug)
    TextView recentSearch;
    @BindView(R.id.drug_searched)
    TextView drugSearched;
    @BindView(R.id.action_clear)
    ImageView actionClear;

    RecentDrugAdapter recentAdapter;
    List<DrugSearchEntity> recentDrug;
    Drug drug;
    BaseWarning warning;
    LinearLayoutManager linearLayoutManager;

    int page = 1;
    String key;
    boolean firstRun = true;
    private int oldPost = 0, newPost = 0;


    private DrugSearchPresenter presenter;
    private SearchNameDrugAdapter searchDrugAdapter;

    public static DrugSearchFragment newInstance() {
        Bundle args = new Bundle();
        DrugSearchFragment fragment = new DrugSearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick(R.id.action_clear)
    public void clear() {
        edtSearchDrug.setText(null);
        actionClear.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drug_search, null, false);
        ButterKnife.bind(this, view);
        warning = new BaseWarning(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new DrugSearchPresenter(this);
        drug = new Drug();
        initRecyclerView();
        initSearchDrugName();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onHiddenData();
                initRecentList();
            }
        }, 1000);
        setupUI(view.findViewById(R.id.rootLayout));
    }

    private void initRecentList() {
        drugSearched.setVisibility(View.GONE);
        rcvSearchDrug.setVisibility(View.GONE);
        try {
            recentSearch.setVisibility(View.VISIBLE);
            rcvRecentDrug.setVisibility(View.VISIBLE);
            String result = SharedUtils.getInstance().getStringValue(Constants.LAST_DRUG);
            RESP_SearchDrug resp_drug = JsonHelper.getObjectNoException(result, RESP_SearchDrug.class);
            Log.e("RESP_Drug Frag", "initRecentList: " + (resp_drug != null ? resp_drug.toString() : null));

            if ((resp_drug != null ? resp_drug.getData().size() : 0) == 0) {
                recentSearch.setVisibility(View.GONE);
                warning.showDataWarning(-1, getString(R.string.mes_drug_recent_empty));
            } else {
                onHiddenData();
                if (recentDrug.size() == 0) {
                    recentDrug.addAll(resp_drug.getData());
                }
                recentAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.e("RESP_Drug Frag", "Exception: " + e.toString());
        }
    }

    private void initRecyclerView() {
        warning.showDataLoading("");
        searchDrugAdapter = new SearchNameDrugAdapter(this, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                searchDrugAdapter.setLoadMore(true);
                firstRun = false;
                page++;
                presenter.searchDrug(key, page);
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rcvSearchDrug.setLayoutManager(linearLayoutManager);
        rcvSearchDrug.setAdapter(searchDrugAdapter);
        rcvSearchDrug.setHasFixedSize(false);
        rcvSearchDrug.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager,page) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                searchDrugAdapter.setLoadMore(true);
            }
        });

        recentDrug = new ArrayList<>();
        recentAdapter = new RecentDrugAdapter(this, recentDrug);
        rcvRecentDrug.setHasFixedSize(false);
        rcvRecentDrug.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvRecentDrug.setAdapter(recentAdapter);
    }

    private void initSearchDrugName() {
        recentSearch.setVisibility(View.GONE);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                drugSearched.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(edtSearchDrug.getText().toString())){
                    initRecentList();
                    actionClear.setVisibility(View.GONE);
                    searchDrugAdapter.clearData();
                }
                if (editable.toString().isEmpty()) {
                    initRecentList();
                    actionClear.setVisibility(View.GONE);
                } else {
                    firstRun = true;
                    page = 1;
                    actionClear.setVisibility(View.VISIBLE);
                    recentSearch.setVisibility(View.GONE);
                    rcvRecentDrug.setVisibility(View.GONE);
                    key = editable.toString().replace(" ", "%");
                    onProgressBarLoading();
                    presenter.searchDrug(key, page);
                }
            }
        };
        edtSearchDrug.addTextChangedListener(textWatcher);
        edtSearchDrug.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_not_share, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("LogConditional")
    @Override
    public void onSearchSuccess(final List<DrugSearchEntity> list) {
        if (!firstRun){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (list.size() > 0){
                        oldPost = searchDrugAdapter.getItemCount()-1;
                        searchDrugAdapter.setListData(list, false);
                        newPost = (searchDrugAdapter.getItemCount()-1) - oldPost;
                        Log.e("onGetDiseaseSuccess", "load more: " + newPost);
                    } else {
                        searchDrugAdapter.setLoadMore(false);
                        searchDrugAdapter.notifyDataSetChanged();
                        showLongToast("Không có thuốc mới hơn!");
                    }
                }
            }, 500);
        } else {
            firstRun= false;
            oldPost = searchDrugAdapter.getItemCount()-1;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onHiddenData();
                    rcvRecentDrug.setVisibility(View.GONE);
                    recentSearch.setVisibility(View.GONE);
                    drugSearched.setVisibility(View.VISIBLE);
                    rcvSearchDrug.setVisibility(View.VISIBLE);
                    searchDrugAdapter.setListData(list, true);
                }
            }, 1000);
        }
    }

    @Override
    public void onProgressBarLoading() {
        rcvSearchDrug.setVisibility(View.GONE);
        rcvRecentDrug.setVisibility(View.GONE);
        warning.showDataLoading("Đang tải...");
    }

    @Override
    public void onHiddenData() {
        warning.hiddenData();
    }

    @Override
    public void onHiddenDataAndShowWarning(String message, int resource) {
        warning.showDataWarning(resource, message);
    }

    @Override
    public void listEmpty(String s) {
        rcvRecentDrug.setVisibility(View.GONE);
        recentSearch.setVisibility(View.GONE);
        rcvSearchDrug.setVisibility(View.GONE);
        warning.showDataWarning(-1, s);
    }

    @Override
    public void onClickItem(int position, DrugSearchEntity drug) {
        saveLastList(position, drug);
        startActivity(DrugDetailActivity.class, Constants.OBJECT, drug);
    }

    @Override
    public void onDeleteItem(int position, DrugSearchEntity drug) {
        DrugRecent respDrug = new DrugRecent(recentDrug);
        String last_result = JsonHelper.toJson(respDrug);
        SharedUtils.getInstance().putStringValue(Constants.LAST_DRUG, last_result);
        if (recentAdapter.getList().size() == 0){
            warning.showDataWarning(-1, getString(R.string.mes_drug_recent_empty));
        }
    }

    private void saveLastList(int position, DrugSearchEntity drug) {
        if (drug != null) {
            for (int i = 0; i < recentDrug.size(); i++) {
                if (recentDrug.get(i).getId() == drug.getId()) {
                    recentDrug.remove(i);
                }
            }
            if (recentDrug.size() >= 10) {
                recentDrug.remove(recentDrug.size() - 1);
                recentDrug.add(0, drug);
            } else {
                recentDrug.add(0, drug);
            }
        }
        DrugRecent respDrug = new DrugRecent(recentDrug);
        String last_result = JsonHelper.toJson(respDrug);
        SharedUtils.getInstance().putStringValue(Constants.LAST_DRUG, last_result);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
