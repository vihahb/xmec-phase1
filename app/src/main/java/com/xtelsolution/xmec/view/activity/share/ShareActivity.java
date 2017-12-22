package com.xtelsolution.xmec.view.activity.share;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.ContactsCallback;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.ContactsUtils;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Contact_Model;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.presenter.shareAndLink.ShareLinkActivityPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.fragment.sharelink.LinkFragment;
import com.xtelsolution.xmec.view.fragment.sharelink.OnFragmentInteractionListener;
import com.xtelsolution.xmec.view.fragment.sharelink.ShareFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.santeri.wvp.WrappingFragmentStatePagerAdapter;
import xyz.santeri.wvp.WrappingViewPager;

public class ShareActivity extends BasicActivity implements OnFragmentInteractionListener, IShareLinkView {

    private static final String TAG = "ShareActivity";

    private ShareLinkActivityPresenter presenter;

    @BindView(R.id.viewpager)
    WrappingViewPager viewPager;
    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    private FragmentsAdapter adapter;
    private int mrb_id = -1;
    private static int REQUEST_CONTACT = 91;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ButterKnife.bind(this);
        presenter = new ShareLinkActivityPresenter(this);
        getData();
        initViewPager();
    }

    private void getData() {
        try {
            mrb_id = getIntent().getIntExtra(Constants.MRB_ID, -1);
        } catch (Exception e) {
            Log.e(TAG, "getInt: " + e);
        }

        if (PermissionHelper.checkPermission(Manifest.permission.READ_CONTACTS, getActivity(), REQUEST_CONTACT)) {
            new ReadingContacts().execute();
        }
    }

    private void initViewPager() {
        List<Fragments> list = new ArrayList<>();
        if (mrb_id > 0) {
            list.add(new Fragments(ShareFragment.newInstance(mrb_id), "CHIA SẺ"));
            list.add(new Fragments(LinkFragment.newInstance(mrb_id), "LIÊN KẾT"));
        }
        adapter = new FragmentsAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onFragmentInteraction() {

    }

    public void cancelAction() {
        finish();
    }

    @Override
    public void saveContactsSuccess() {

    }

    @Override
    public void saveContactsError(String message) {
        MyApplication.log("save contacts: ", "SUCCESS!");
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    public void finishAction(String ACTION) {
        Intent intent = new Intent(ACTION);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACT) {
            if (!PermissionHelper.checkResult(grantResults)) {
                closeProgressBar();
//                Snackbar.make(getActivity().getWindow().getDecorView().getRootView(), getString(R.string.message_need_allow_permission), Snackbar.LENGTH_LONG)
//                        .setAction("Cấp quyền", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                PermissionHelper.checkPermission(Manifest.permission.READ_CONTACTS, getActivity(), REQUEST_CONTACT);
//                            }
//                        }).show();
                showLongToast(getString(R.string.message_need_allow_permission));
            } else {
//                showProgressBar(true, true, "Đang tải liên hệ...");
                new ReadingContacts().execute();
            }
        }
    }

    class FragmentsAdapter extends WrappingFragmentStatePagerAdapter {

        private List<Fragments> listData;

        FragmentsAdapter(FragmentManager fm, List<Fragments> listData) {
            super(fm);
            this.listData = listData;
        }

        @Override
        public Fragment getItem(int position) {
            return listData.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = listData.get(position).getTitle();
            return title != null ? title : super.getPageTitle(position);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class ReadingContacts extends AsyncTask<Void, Void, List<Contact_Model>> {

        @Override
        protected List<Contact_Model> doInBackground(Void... voids) {
            final List<Contact_Model> modelList = new ArrayList<>();
            try {
                ContactsUtils.readContacts(new ContactsCallback() {
                    @Override
                    public void processSuccess(List<Contact_Model> list) {
                        modelList.addAll(list);
                    }

                    @Override
                    public void processError(String message) {
                        MyApplication.log("Reading contacts error", " message " + message);
                    }
                });
            } catch (Exception e) {
                MyApplication.log("Thread read contacts", "Exception " + e.toString());
            }
            return modelList;
        }

        @Override
        protected void onPostExecute(List<Contact_Model> list) {
            super.onPostExecute(list);
            if (list != null && list.size() > 0) {
                presenter.saveListContacts(list);
            }
        }
    }
}
