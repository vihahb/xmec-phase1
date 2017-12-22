package com.xtelsolution.xmec.presenter;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.database.LogoutModel;
import com.xtelsolution.xmec.model.database.SaveContactModel;
import com.xtelsolution.xmec.model.database.SaveListModel;
import com.xtelsolution.xmec.model.entity.CategoryEntity;
import com.xtelsolution.xmec.model.entity.Contact_Model;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.Shared;
import com.xtelsolution.xmec.model.req.REQ_User;
import com.xtelsolution.xmec.model.resp.RESP_CategoryList;
import com.xtelsolution.xmec.model.resp.RESP_ImageUrl;
import com.xtelsolution.xmec.model.resp.RESP_Notification;
import com.xtelsolution.xmec.model.resp.RESP_Upload_Image;
import com.xtelsolution.xmec.model.server.GetCategoryNewsfeed;
import com.xtelsolution.xmec.model.server.GetImageUrl;
import com.xtelsolution.xmec.model.server.GetMbrByIdModel;
import com.xtelsolution.xmec.model.server.NotificationModel;
import com.xtelsolution.xmec.model.server.UpdateFcmKeyModel;
import com.xtelsolution.xmec.model.server.UpdateUserModel;
import com.xtelsolution.xmec.model.server.UploadUserAvatarModel;
import com.xtelsolution.xmec.view.activity.inf.IHomeView;
import com.xtelsolution.xmec.view.fragment.diseases.DiseaseListFragment;
import com.xtelsolution.xmec.view.fragment.hospital.HospitalMapFragment;
import com.xtelsolution.xmec.view.fragment.mbr.MbrFragment;
import com.xtelsolution.xmec.view.fragment.news.NewsFragment;
import com.xtelsolution.xmec.view.fragment.searchDrug.DrugSearchFragment;
import com.xtelsolution.xmec.view.fragment.share.ShareMainFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/17/2017
 * Email: leconglongvu@gmail.com
 */
public class HomePresenter extends BasicPresenter {
    private IHomeView view;

    private static final String TAG = "HomePresenter";

    public HomePresenter(IHomeView view) {
        super(view);
        this.view = view;
        updateBaseImageUrl();
        updateFcm();
        updateCollectionNewsFeed();
    }

    private void updateBaseImageUrl() {
        iCmd(8);
    }

    private void iCmd(final Object... params) {
        switch ((int) params[0]) {
            case 1:
                final int active = (int) params[1];
                if (active == 0){
                    if (!NetworkUtils.isConnected2(view.getActivity())){
                        return;
                    }
                }
                new UpdateFcmKeyModel(active) {
                    @Override
                    protected void onSuccess() {
                        if (view.getActivity() != null) {
                            if (active > 0) {
                                SharedUtils.getInstance().putBooleanValue(Constants.IS_UPDATE_FCM, true);
                            } else {
                                SharedUtils.getInstance().putBooleanValue(Constants.IS_UPDATE_FCM, false);
                                iCmd(9);
                            }
                        }
                    }

                    @Override
                    protected void onError(int errorCode) {
                        Log.e("UpdateFcmKey", "onError: " + errorCode);
                    }
                };
                break;

            case 2:
                final int type;
                if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
                    type = 1;
                } else {
                    type = 0;
                }
                new NotificationModel(type) {
                    @Override
                    protected void onSuccess(RESP_Notification obj) {
                        int count = 0;
                        for (int i = 0; i < obj.getData().size(); i++) {
                            if (obj.getData().get(i).getNotifyState() == 0) {
                                count++;
                            }
                        }
                        SharedUtils.getInstance().putIntValue(Constants.BADGE_COUNT, count);
                        view.setCount(count);
                    }

                    @Override
                    protected void onError(int error) {
                        MyApplication.log("Err get Count", "Code: " + error);
                    }
                };
                break;
            case 4:
                final File file = (File) params[1];
                new UploadUserAvatarModel(file) {
                    @Override
                    protected void onSuccess(RESP_Upload_Image obj) {
                        iCmd(5, obj.getFull_path_server(), file.getPath());
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };
                break;
            case 5:
                int uid = SharedUtils.getInstance().getIntValue(Constants.USER_UID);
                final String avatar = (String) params[1];
                final String filePath = (String) params[2];

                final REQ_User reqUser = new REQ_User();
                reqUser.setUid(uid);
                reqUser.setAvatar(avatar);

                new UpdateUserModel(JsonHelper.toJson(reqUser)) {
                    @Override
                    protected void onSuccess() {
                        SharedUtils.getInstance().putStringValue(Constants.USER_AVATAR, avatar);
                        view.onUpdateAvatarSuccess(filePath);
                    }

                    @Override
                    protected void onError(int errorCode) {
                        showError(errorCode);
                    }
                };

                break;

            case 6:
                List<Integer> MbrIdList = (List<Integer>) params[1];
                if (MbrIdList.size() > 0) {
                    for (int i = 0; i < MbrIdList.size(); i++) {
                        new GetMbrByIdModel(MbrIdList.get(i)) {
                            @Override
                            protected void onSuccess(RealmList<Mbr> mbrList) {
                                Log.e("Presenter notification", "onSuccess: " + mbrList.get(0).toString());
                                view.addToMbrList(mbrList.get(0));
                            }

                            @Override
                            protected void onErrror(int code) {
                                if (code == Constants.ERROR_SESSION) {
                                    getNewSession(params);
                                } else {
                                    showError(606);
                                }
                            }
                        };
                    }
                }
                break;

            case 7:
                new GetCategoryNewsfeed() {
                    @Override
                    protected void onSuccess(RESP_CategoryList object) {
                        final List<CategoryEntity> entityList = object.getData();
                        final RealmList<CategoryEntity> categoryRealmList = new RealmList<>();
                        Log.e(TAG, "onSuccess: " + entityList.toString());
                        MyApplication.log("onSuccess", "category: " + JsonHelper.toJson(entityList));
                        categoryRealmList.addAll(entityList);

                        new SaveListModel<CategoryEntity>(categoryRealmList) {
                            @Override
                            protected void onSuccess() {
                                MyApplication.log("Size category", "Size: " + categoryRealmList.size());
                                view.saveCategorySuccess();
                            }

                            @Override
                            protected void onError() {
                                view.saveCategoryError();
                            }
                        };
                    }

                    @Override
                    protected void onErrror(int code) {
                        MyApplication.log("Home Presenter", "" + TextUtils.getErrorCode(code));
                    }
                };
                break;

            case 8:
                new GetImageUrl() {
                    @Override
                    protected void onSuccess(RESP_ImageUrl url) {
                        Log.e(TAG, "onSuccess - save URL...: " + url.getUrl());
                        SharedUtils.getInstance().putStringValue(Constants.BASE_IMAGE_URL, url.getUrl());
                    }

                    @Override
                    protected void onErrror(int code) {

                    }
                };
                break;

            case 9:
                new LogoutModel() {
                    @Override
                    protected void onSuccess(boolean isSuccess) {
                        view.onLogoutSuccess(isSuccess);
                    }
                };
                break;
        }
    }

    public List<Fragments> getListTab() {
        List<Fragments> list = new ArrayList<>();

        list.add(new Fragments(MbrFragment.newInstance()));
        list.add(new Fragments(NewsFragment.newInstance()));
        list.add(new Fragments(DiseaseListFragment.newInstance()));
        list.add(new Fragments(DrugSearchFragment.newInstance()));
        list.add(new Fragments(HospitalMapFragment.newInstance()));
        list.add(new Fragments(ShareMainFragment.newInstance()));

        return list;
    }

    private void updateFcm() {
        if (!SharedUtils.getInstance().getBooleanValue(Constants.IS_UPDATE_FCM)) {
            iCmd(1, 1);
        }

        MyApplication.log("HomePresenter", "fcm key " + FirebaseInstanceId.getInstance().getToken());
    }

    public void getCountNotify() {
        if (!NetworkUtils.isConnected(MyApplication.context)) {
//            view.showLongToast(view.getActivity().getString(R.string.error_internet));
            return;
        } else {
            iCmd(2);
        }
    }

    public void updateAvatar(File file) {
        if (file == null) {
            showError(Constants.ERROR_UNKOW);
            return;
        }

        if (!NetworkUtils.isConnected(view.getActivity())) {
            showError(Constants.ERROR_NO_INTERNET);
            return;
        }

        view.showProgressView(1);
        iCmd(4, file);
    }

    public void getMbr(List<Integer> mbrId) {
        iCmd(6, mbrId);
    }

    public void logout() {
        iCmd(1, 0);
    }

    @Override
    protected void getSessionSuccess(Object... params) {
        iCmd(params);
    }

    public void saveListContacts(List<Contact_Model> list) {
        RealmList<Contact_Model> realmList = new RealmList<>();
        realmList.addAll(list);
        new SaveContactModel<Contact_Model>(realmList) {
            @Override
            protected void onSuccess() {
                MyApplication.log("onPostExecute", "SUCCESS");
            }

            @Override
            protected void onError(String message) {
                MyApplication.log("onPostExecute", "onError " + message);
            }
        };
    }

    private void updateCollectionNewsFeed() {
        if (!SharedUtils.getInstance().getBooleanValue(Constants.CATEGORY_SAVE)) {
            if (!NetworkUtils.isConnected(view.getActivity())) {
                MyApplication.log("Home get category", TextUtils.getErrorCode(-3));
                return;
            } else {
                iCmd(7);
            }
        }
    }
}