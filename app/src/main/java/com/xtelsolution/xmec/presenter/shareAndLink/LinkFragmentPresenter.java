package com.xtelsolution.xmec.presenter.shareAndLink;

import android.util.Log;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.model.database.GetListByKeyModel;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.database.SaveObjectModel;
import com.xtelsolution.xmec.model.entity.Contact_Model;
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.req.REQ_ShareLink;
import com.xtelsolution.xmec.model.resp.RESP_Friend;
import com.xtelsolution.xmec.model.resp.RESP_Friends;
import com.xtelsolution.xmec.model.resp.RESP_Id;
import com.xtelsolution.xmec.model.server.GetSharedFriendsModel;
import com.xtelsolution.xmec.model.server.SearchFriendModel;
import com.xtelsolution.xmec.model.server.ShareModel;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.fragment.inf.shareAndLink.ILinkFragmentView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by xtel on 11/13/17.
 */

public class LinkFragmentPresenter extends BasicPresenter {

    private static final String TAG = "LinkFragmentPresenter";
    private ILinkFragmentView view;

    public LinkFragmentPresenter(ILinkFragmentView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(final Object... params){
        switch ((int)params[0]){
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

            case 3:
                final Friends friends = (Friends) params[1];
                final int mbr_id = (int) params[2];
                final REQ_ShareLink shareLink = new REQ_ShareLink();
                shareLink.setUid(friends.getUid());
                shareLink.setMrbId(mbr_id);
                shareLink.setShareType(2);
                shareLink.setShareTo(friends.getShareTo());

                new ShareModel(JsonHelper.toJson(shareLink)) {
                    @Override
                    protected void onSuccess(RESP_Id obj) {
                        view.linkMbrSuccess();
                        final ShareAccounts accounts =new ShareAccounts();
                        accounts.setAvatar(friends.getAvatar());
                        accounts.setFullname(friends.getFullname());
                        accounts.setId(obj.getId());
                        accounts.setShareTo(shareLink.getShareTo());
                        accounts.setShareType(shareLink.getShareType());
                        accounts.setUid(friends.getUid());
                        //Get Mbr by id
                        new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbr_id) {
                            @Override
                            protected void onSuccess(Mbr object) {
                                object.getShareAccounts().add(accounts);
                                new SaveObjectModel<Mbr>(object) {
                                    @Override
                                    protected void onSuccess() {
                                        view.saveObjectSuccess();
                                    }

                                    @Override
                                    protected void onError() {

                                    }
                                };
                            }
                        };
                    }

                    @Override
                    protected void onError(int errorCode) {
                        if (errorCode == Constants.ERROR_SESSION)
                            showError(errorCode, params);
                        if (errorCode == 4)
                            showError(4);
                        else
                            showError(305);
                    }
                };

                break;
        }
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
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

    public void linkMbr(Friends friends, int mrb_id) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.onError(Constants.ERROR_NO_INTERNET);
            return;
        }
        iCmd(3, friends, mrb_id);
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
