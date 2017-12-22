package com.xtelsolution.xmec.view.fragment.news;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtelsolution.sdk.callback.OnItemClickListener;
import com.xtelsolution.sdk.callback.OnLoadMoreListener;
import com.xtelsolution.sdk.callback.ReloadListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.EndlessRecyclerViewScrollListener;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.CategoryListEntity;
import com.xtelsolution.xmec.presenter.news.CategoryFragmentPresenter;
import com.xtelsolution.xmec.view.activity.news.DetailNewsActivity;
import com.xtelsolution.xmec.view.activity.news.DetailNewsPostActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.widget.BaseRecyclerViewRefresh;

import java.util.List;

/**
 * Created by vivu on 12/13/17
 * xtel.vn
 */

public class CategoryFragment
        extends BasicFragment
        implements ICategoryFragmentView,
        OnItemClickListener<CategoryListEntity> {

    private int id_category = -1;
    private BaseRecyclerViewRefresh baseRecyclerView;
    private CategoryFragmentPresenter presenter;
    private int main_page, newPost, oldPost;
    CategoryListAdapter adapter;
    EndlessRecyclerViewScrollListener scrollListener;
    boolean firstRun = true;

    private static final String TAG = "CategoryFragment";
    private boolean scrolled = false;

    // Required empty public constructor
    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(int id_category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.ID, id_category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CategoryFragmentPresenter(this);
        if (getArguments() != null) {
            id_category = getArguments().getInt(Constants.ID);
            Log.e("id_category", "onCreate: " + id_category);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_category, container, false);
        baseRecyclerView = new BaseRecyclerViewRefresh(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new CategoryListAdapter(getContext(),
                this, new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.e(TAG, "onLoadMore: ");
                if (scrolled) {
                    presenter.getCategoryById(id_category, main_page);
                }
            }
        });
        baseRecyclerView.getRecyclerView().setNestedScrollingEnabled(true);
        baseRecyclerView.getRecyclerView().setAdapter(adapter);
        if (id_category > 0) {
            baseRecyclerView.loading();
            if (adapter.getItemCount() > 0) {
                adapter.getList().clear();
                main_page = 1;
            }
            presenter.getCategoryById(id_category, main_page);
        }

        baseRecyclerView.clickReload(new ReloadListener() {
            @Override
            public void OnClick() {
                firstRun = true;
                main_page = 1;
                presenter.getCategoryById(id_category, main_page);
            }
        });

        baseRecyclerView.onRefresh(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firstRun = true;
                main_page = 1;
                presenter.getCategoryById(id_category, main_page);
            }
        });
//        scrollListener = new EndlessRecyclerViewScrollListener(baseRecyclerView.getLinearLayoutManager(), main_page) {
//            @Override
//            public void onLoadMore(final int page, int totalItemsCount, RecyclerView view) {
//                firstRun = false;
//                main_page = page;
//                adapter.setLoadMore(true);
//                scrolled = true;
//            }
//        };
        baseRecyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = baseRecyclerView.getLinearLayoutManager().getChildCount();
                int totalItemCount = baseRecyclerView.getLinearLayoutManager().getItemCount();
                int pastVisibleItems = baseRecyclerView.getLinearLayoutManager().findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    //End of list
                    firstRun = false;
                    main_page++;
                    adapter.setLoadMore(true);
                    scrolled = true;
                }

            }
        });

//        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                if (categoryList.size() <= oldPost) {
//                    main_page++;
//                    new Handler().post(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    });
//
//                } else {
//                    Toast.makeText(getContext(), "Tải thêm " + newPost + " tin mới.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void setDataWarning(String s) {
        baseRecyclerView.showError(s);
    }

    @Override
    public void getCategoryListSuccess(final List<CategoryListEntity> data) {
        if (!firstRun) {
            firstRun = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (data.size() > 0) {
                        for (int i = 0; i < data.size(); i++) {
                            for (int x = 0; x < adapter.getItemCount() - 1; x++) {
                                if (data.get(i).id == adapter.getList().get(x).id) {
                                    data.remove(i);
                                }
                            }
                        }
                        oldPost = adapter.getItemCount() - 1;
                        adapter.refreshData(data, false);
                        newPost = (adapter.getItemCount() - 1) - oldPost;
//                        if (newPost > 0) {
//                            showLongToast("Tải thêm " + newPost + " bản tin mới!");
//                        }
                        Log.e(TAG, "refreshData by ID: " + id_category);
                    } else {
                        adapter.setLoadMore(false);
                        adapter.removeLastItem();
                        showLongToast("Không có tin tức mới hơn!");
                    }
                }
            }, 1000);
        } else {
            firstRun = false;
            oldPost = adapter.getItemCount() - 1;
            adapter.refreshData(data, true);
            Log.e(TAG, "refreshData by ID: " + id_category);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                baseRecyclerView.showData();
            }
        }, 1000);
    }

    @Override
    public void onClick(int position, CategoryListEntity item) {
        Log.e(TAG, "onClick: " + item.toString());
//        DetailNewsActivity.start(getActivity(), item);
        DetailNewsPostActivity.start(getActivity());
        Intent intent = new Intent(getActivity(), DetailNewsActivity.class);
        intent.putExtra(Constants.ID, item.getId());
        intent.putExtra(Constants.NAME, item.getTitle());
//        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
