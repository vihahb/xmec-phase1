package com.xtelsolution.xmec.view.fragment.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.news.NewsCategoryTabAdapter;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.presenter.news.NewsPresenter;
import com.xtelsolution.xmec.view.activity.inf.news.NewsCategoryPostsView;
import com.nshmura.recyclertablayout.RecyclerTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ThanhChung on 03/11/2017.
 * hiển thị các tab thể loại bài viết
 * Edit by Vivh - 12/12/2017
 */

public class NewsFragment extends Fragment implements NewsCategoryPostsView {
    private static final String TAG = "NewsFragment";
    @BindView(R.id.tab_new)
    RecyclerTabLayout tabNew;
    @BindView(R.id.pageNews)
    ViewPager viewPager;
    Unbinder unbinder;
    @BindView(R.id.layoutNew)
    LinearLayout layoutNew;

    private NewsPresenter presenter;
    private NewsCategoryAdapter categoryAdapter;

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private NewsCategoryTabAdapter adapter;
    private List<Fragments> list;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        presenter = new NewsPresenter(getActivity(), this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list = new ArrayList<>();
        adapter = new NewsCategoryTabAdapter(getChildFragmentManager());
        categoryAdapter = new NewsCategoryAdapter(viewPager, getContext());
        viewPager.setAdapter(adapter);
        tabNew.setUpWithViewPager(viewPager);
        presenter.getData();
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onError(int code) {
        Log.e(TAG, "onError: " + code);
    }

    @Override
    public void showLongToast(String message) {

    }

    @Override
    public void onEndSession() {

    }

    @Override
    public void onSuccess(List<Fragments> list) {
        if (list != null && list.size() > 0) {
            categoryAdapter.refreshData(list);
            adapter.synchronideCollection(list);
        }
    }

    @Override
    public void loading() {
    }
}
