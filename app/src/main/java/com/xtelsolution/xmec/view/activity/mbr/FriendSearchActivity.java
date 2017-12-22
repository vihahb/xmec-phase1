package com.xtelsolution.xmec.view.activity.mbr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.xtelsolution.sdk.callback.NumberChangeListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.mbr.FriendSearchAdapter;
import com.xtelsolution.xmec.model.entity.Contact_Model;
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.resp.RESP_Friends;
import com.xtelsolution.xmec.presenter.mbr.SearchFriendPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.mbr.ISearchFriendView;
import com.xtelsolution.xmec.view.fragment.sharelink.AdapterAutoComplete;
import com.xtelsolution.xmec.view.widget.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendSearchActivity extends BasicActivity implements ISearchFriendView {
    private SearchFriendPresenter presenter;

    @BindView(R.id.edtSearch)
    CustomAutoCompleteTextView edtSearch;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.rb_view_only)
    RadioButton rbViewOnly;
    @BindView(R.id.rb_view_and_edit)
    RadioButton rbViewAndEdit;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_link)
    Button btnLink;

    List<Contact_Model> list;

    private boolean isGetOldFriend = true;

    private FriendSearchAdapter adapter;
    private AdapterAutoComplete autoCompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);
        ButterKnife.bind(this);

        presenter = new SearchFriendPresenter(this);
        list = new ArrayList<>();
        getData();
        initActionListener();
        initAutoComplete();
        presenter.getSharedFriends();
    }

    private void getData() {
        int current = 0;

        try {
            current = getIntent().getIntExtra(Constants.OBJECT, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initListFriends((5 - current));
    }

    /**
     * Initializing auto complete contacts */
    private void initAutoComplete() {
        list = presenter.getContact();
        autoCompleteAdapter = new AdapterAutoComplete(this, list);
        edtSearch.setAdapter(autoCompleteAdapter);
        edtSearch.setThreshold(4);
    }

    /**
     * Khởi tạo danh sách bạn bè
     */
    private void initListFriends(int max) {
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        adapter = new FriendSearchAdapter(max);
        recyclerview.setAdapter(adapter);

        adapter.setChagneListener(new NumberChangeListener() {
            @Override
            public void onChange(int itemCount) {
                if (adapter.getChooseCount() > 0) {
                    btnLink.setText(getString(R.string.layout_sf_link));
                } else {
                    btnLink.setText(getString(R.string.layout_sf_search));
                }
            }
        });
    }

//    /**
//     * Lắng nghe người dùng nhập số điện thoại
//     */
//    private void initTextChangeListener() {
//        edtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//    }

    /**
     * Lắng nghe sự kiện người dùng tương tác
     */
    private void initActionListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAction();
            }
        });
    }

    private void checkAction() {
        KeyboardUtils.hideSoftKeyboard(this);
        String action = btnLink.getText().toString();

        if (action.equals(getString(R.string.layout_sf_link))) {
            linkAccount();
        } else {
            presenter.searchFriend(edtSearch.getText().toString());
        }
    }

    private void linkAccount() {
        RESP_Friends resp_friends = adapter.getListSelected(rbViewOnly.isChecked());

        if (resp_friends.getData().size() == 0) {
            showLongToast(getString(R.string.error_sf_select_friend));
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, resp_friends);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onGetFriendSuccess(List<Friends> friendsList) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setListData(friendsList);
        isGetOldFriend = false;
    }

    /**
     * Tìm kiếm bạn thành công
     *
     * @param friends kết quả
     */
    @Override
    public void onSearchSuccess(Friends friends) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.clearItem();
        adapter.addItem(friends);
    }

    @Override
    public void onShowProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        progressBar.setVisibility(View.INVISIBLE);

        switch (code) {
            case 300:
                showLongToast(getString(R.string.error_sf_input_phone));
                break;
            case 301:
                showLongToast(getString(R.string.error_sf_invalid_phone));
                break;
            case 404:
                if (!isGetOldFriend)
                    showLongToast(getString(R.string.error_sf_not_found));
                else
                    isGetOldFriend = false;
                break;
        }
    }

    /**
     * Tìm kiếm bạn thất bại
     */


    @Override
    public Activity getActivity() {
        return this;
    }
}