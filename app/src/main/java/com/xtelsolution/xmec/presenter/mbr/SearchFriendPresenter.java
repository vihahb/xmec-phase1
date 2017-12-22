package com.xtelsolution.xmec.presenter.mbr;

import android.util.Log;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.entity.Contact_Model;
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.resp.RESP_Friend;
import com.xtelsolution.xmec.model.resp.RESP_Friends;
import com.xtelsolution.xmec.model.server.GetSharedFriendsModel;
import com.xtelsolution.xmec.model.server.SearchFriendModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.inf.mbr.ISearchFriendView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/18/2017
 * Email: leconglongvu@gmail.com
 */
public class SearchFriendPresenter extends BasicPresenter {
    private static final String TAG = "SearchFriendPresenter";
    private ISearchFriendView view;

    public SearchFriendPresenter(ISearchFriendView view) {
        super(view);
        this.view = view;
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:

                new GetSharedFriendsModel() {
                    @Override
                    protected void onSuccess(RESP_Friends respFriend) {
                        view.onGetFriendSuccess(respFriend.getData());
                    }

                    @Override
                    protected void onErrror(int code) {
                        showError(code, params);
                    }
                };

                break;
            case 2:

                String phone = (String) params[1];

                new SearchFriendModel(phone) {
                    @Override
                    protected void onSuccess(RESP_Friend respFriend) {
                        Friends friends = new Friends(respFriend);

                        view.onSearchSuccess(friends);
                    }

                    @Override
                    protected void onErrror(int code) {
                        if (code == Constants.ERROR_SESSION)
                            getNewSession(params);
                        else
                            showError(404, params);
                    }
                };

                break;
        }
    }

    public void getSharedFriends() {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.onShowProgressBar();
        iCmd(1);
    }

    public void searchFriend(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showError(300);
            return;
        }

        if (phone.length() < 10 || phone.length() > 15) {
            showError(301);
            return;
        }

        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.onShowProgressBar();
        iCmd(2, phone);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

    public List<Contact_Model> getContact() {
        final List<Contact_Model> contactList = new ArrayList<>();
        new GetListByKeyModel<Contact_Model>(Contact_Model.class, null, -1) {
            @Override
            protected void onSuccess(RealmList<Contact_Model> realmList) {
                contactList.addAll(realmList);
                Log.e(TAG, "realmList: " + Arrays.toString(realmList.toArray()));
            }
        };
        return contactList;
    }
}