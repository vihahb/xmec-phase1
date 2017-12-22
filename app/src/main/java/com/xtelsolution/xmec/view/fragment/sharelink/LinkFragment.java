package com.xtelsolution.xmec.view.fragment.sharelink;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.xtelsolution.sdk.callback.NumberChangeListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.mbr.FriendSearchAdapter;
import com.xtelsolution.xmec.model.entity.Contact_Model;
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.resp.RESP_Friends;
import com.xtelsolution.xmec.presenter.shareAndLink.LinkFragmentPresenter;
import com.xtelsolution.xmec.view.activity.share.ShareActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.shareAndLink.ILinkFragmentView;
import com.xtelsolution.xmec.view.widget.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link LinkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LinkFragment extends BasicFragment implements ILinkFragmentView {

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

    private OnFragmentInteractionListener mListener;

    private int mrb_id;

    private boolean isGetOldFriend = true;

    private FriendSearchAdapter adapter;
    private LinkFragmentPresenter presenter;

    private AdapterAutoComplete autoCompleteAdapter;

    private List<Contact_Model> list;

    public LinkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param mrb_id
     * @return A new instance of fragment LinkFragment.
     */
    public static LinkFragment newInstance(int mrb_id) {
        LinkFragment fragment = new LinkFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.MRB_ID, mrb_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mrb_id = getArguments().getInt(Constants.MRB_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_link, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar.setVisibility(View.INVISIBLE);
        presenter = new LinkFragmentPresenter(this);
        presenter.getSharedFriends();
        list = new ArrayList<>();
        initAutoComplete();
        initFriendList(5);
    }

    private void initAutoComplete() {
        list = presenter.getContact();
        autoCompleteAdapter = new AdapterAutoComplete(getContext(), list);
        edtSearch.setAdapter(autoCompleteAdapter);
        edtSearch.setThreshold(4);
    }

    private void initFriendList(int max) {
        recyclerview.setHasFixedSize(false);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new FriendSearchAdapter(max);
        recyclerview.setAdapter(adapter);

        adapter.setChagneListener(itemCount -> {
            if (adapter.getChooseCount() > 0) {
                btnLink.setText(getString(R.string.layout_sf_link));
            } else {
                btnLink.setText(getString(R.string.layout_sf_search));
            }
        });
    }

    @OnClick(R.id.btn_cancel)
    public void onCancel() {
        if (getActivity() instanceof ShareActivity) {
            ShareActivity activity = (ShareActivity) getActivity();
            activity.cancelAction();
        }
    }

    @OnClick(R.id.btn_link)
    public void checkAction() {
        String action = btnLink.getText().toString();
        if (action.equals(getString(R.string.layout_sf_link))) {
            linkMbr();
        } else {
            presenter.searchFriend(edtSearch.getText().toString());
        }
    }

    private void linkMbr() {
        RESP_Friends resp_friends = adapter.getListSelected(rbViewOnly.isChecked());
        if (resp_friends.getData().size() == 0) {
            showLongToast(getString(R.string.error_sf_select_friend));
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        presenter.linkMbr(resp_friends.getData().get(0), mrb_id);
    }

//    public void onButtonPressed() {
//        if (mListener != null) {
//            mListener.onFragmentInteraction();
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onShowProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchSuccess(Friends friends) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.clearItem();
        adapter.addItem(friends);
    }

    @Override
    public void onGetFriendSuccess(List<Friends> friendsList) {
        progressBar.setVisibility(View.INVISIBLE);
        adapter.setListData(friendsList);
        isGetOldFriend = false;
    }

    @Override
    public void linkMbrSuccess() {
        progressBar.setVisibility(View.INVISIBLE);
        showLongToast("Đã liên kết y bạ");
    }

    @Override
    public void saveObjectSuccess() {
        if (getActivity() instanceof ShareActivity) {
            ShareActivity activity = (ShareActivity) getActivity();
            activity.finishAction(Constants.ACTION_ADD);
        }
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        progressBar.setVisibility(View.INVISIBLE);

        switch (code) {
            case 4:
                showLongToast("Y bạ này đã được liên kết trước đó.");
                break;
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
}
