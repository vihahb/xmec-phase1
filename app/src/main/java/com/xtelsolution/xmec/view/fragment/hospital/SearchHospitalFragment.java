package com.xtelsolution.xmec.view.fragment.hospital;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.OnItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.hospital.HospitalSearchAdapter;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.presenter.hospital.HospitalPresenter;
import com.xtelsolution.xmec.view.activity.hospitalInfo.HospitalInfoActivity;
import com.xtelsolution.xmec.view.activity.inf.hospital.IHospitalView;
import com.xtelsolution.xmec.view.widget.EndlessRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xtelsolution.sdk.commons.Constants.SEARCH_KEY;

/**
 * Created by ThanhChung on 12/7/17.
 */

public class SearchHospitalFragment extends Fragment implements IHospitalView {
    @BindView(R.id.recyclerview_progress_view)
    RecyclerView recyclerviewProgressView;
    @BindView(R.id.img_progress_view_data)
    ImageView imgProgressViewData;
    @BindView(R.id.txt_progress_view_data)
    TextView txtProgressViewData;
    @BindView(R.id.layout_progress_view_data)
    LinearLayout layoutProgressViewData;
    @BindView(R.id.swipe_progress_view)
    SwipeRefreshLayout swipeProgressView;
    Unbinder unbinder;
    private CountDownTimer timer;
    private int cPage = 1;
    private static final String TAG = "SearchHospitalFragment";
    private EndlessRecyclerViewScrollListener scrollListener;


    public static SearchHospitalFragment newInstance() {

        Bundle args = new Bundle();

        SearchHospitalFragment fragment = new SearchHospitalFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private HospitalPresenter presenter;
    private List<Hospital> listSearch;
    private HospitalSearchAdapter adapterSearch;

    private BroadcastReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listSearch = new ArrayList<>();
        adapterSearch = new HospitalSearchAdapter(listSearch, getActivity());
        presenter = new HospitalPresenter(this);
        timer = new CountDownTimer(400, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!key.equals("")) {
                    cPage = 1;
                    scrollListener.resetState();
                    adapterSearch.setLoadmore(true);
                    presenter.setcPage(0);
                    presenter.searchHospital(key, cPage);
                }
            }
        };
        initReceiver();

    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(SEARCH_KEY)) {
                    searchKey(intent.getStringExtra(SEARCH_KEY));
                }
            }
        };
        IntentFilter filter = new IntentFilter(SEARCH_KEY);
        getActivity().registerReceiver(receiver, filter);
    }

    String key = "";

    private void searchKey(final String stringExtra) {
        key = stringExtra;
        if (timer != null) {
            timer.cancel();
            timer.start();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_hospital, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initUI();
    }

    private LinearLayoutManager manager;

    private void initUI() {
        manager = new LinearLayoutManager(getContext());
        recyclerviewProgressView.setLayoutManager(manager);
        recyclerviewProgressView.setNestedScrollingEnabled(false);
        recyclerviewProgressView.setAdapter(adapterSearch);
        swipeProgressView.setEnabled(false);

        adapterSearch.setItemClickListener(new OnItemClickListener<Hospital>() {
            @Override
            public void onClick(int position, Hospital item) {
                HospitalInfoActivity.start(getContext(), item.getId());
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(manager, cPage) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                cPage = page;
                Log.e(TAG, "onLoadMore: " + page);
                presenter.searchHospital(key, page);
            }
        };
        scrollListener.setCurrentPage(cPage);

        recyclerviewProgressView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onError(int code) {
        try {
            if (listSearch.size() == 0) {
                txtProgressViewData.setText(Html.fromHtml(Constants.getMessageErrorByCode(code)));
                txtProgressViewData.setVisibility(View.VISIBLE);
                layoutProgressViewData.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            //        tránh trường hợp khi fragment destroy khi chưa load dữ liệu xong => view null
            e.printStackTrace();
        }
    }

    @Override
    public void showLongToast(String message) {

    }

    @Override
    public void onEndSession() {

    }

    @Override
    public void onSearchHospitalSuccess(List<Hospital> hospitalList, String key) {
        try {
//            if (key.equals(this.key)) {
                if (cPage == 1) {
                    listSearch.clear();
                }
                if (hospitalList.size() < 20) {
                    adapterSearch.setLoadmore(false);
                    scrollListener.setLoadMore(false);
                }
                listSearch.addAll(hospitalList);
//            if (myLatLng != null)
//                try {
//                    Collections.sort(listSearch, new Comparator<Hospital>() {
//                        @Override
//                        public int compare(Hospital o1, Hospital o2) {
//                            try {
//                                if (o1.getLat() == null && o2.getLat() == null) return 1;
//                                if (o1.getLat() == null && o2.getLat() != null) return -1;
//                                if (o1.getLat() != null && o2.getLat() == null) return 1;
//                                double d = (MapUtil.calculationByDistanceM(myLatLng, new LatLng(o1.getLat(), o1.getLng())) - MapUtil.calculationByDistanceM(myLatLng, new LatLng(o2.getLat(), o2.getLng())));
//                                return (d == 0) ? 0 : (d > 0) ? 1 : -1;
//                            } catch (Exception e) {
//                                return 1;
//                            }
//                        }
//                    });
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
                adapterSearch.notifyDataSetChanged();
                if (listSearch.size() > 0) {
                    layoutProgressViewData.setVisibility(View.GONE);
                    recyclerviewProgressView.setVisibility(View.VISIBLE);
                } else if (listSearch.size() == 0 && cPage == 1) {
                    layoutProgressViewData.setVisibility(View.VISIBLE);
                    txtProgressViewData.setVisibility(View.VISIBLE);
                    txtProgressViewData.setText("Không tìm thấy kết quả nào");
                    recyclerviewProgressView.setVisibility(View.GONE);
                } else if (hospitalList.size() == 0) {
                    adapterSearch.setLoadmore(false);
                }
//            }
        } catch (Exception e) {
            //        tránh trường hợp khi fragment destroy khi chưa load dữ liệu xong => view null
            e.printStackTrace();
        }
    }

    @Override
    public void onGetHospitalAroundSuccess(List<Hospital> hospitalList) {

    }

    @Override
    public void showProgressBar(int type) {
        if (type == Constants.SEARCH_HOSPITALBY_KEY) {
            layoutProgressViewData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    public int size() {
        return listSearch.size();
    }
}
