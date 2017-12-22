package com.xtelsolution.xmec.view.fragment.mbr;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.mbr.Avatar16Adapter;
import com.xtelsolution.xmec.model.database.GetObjectByKeyModel;
import com.xtelsolution.xmec.model.entity.HealthRecords;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.NotificationAction;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.presenter.mbr.MbrItemPresenter;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.activity.mbr.MbrUpdateActivity;
import com.xtelsolution.xmec.view.activity.sharelink.ShareLinkListActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.fragment.inf.MbrItemView;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/17/2017
 * Email: leconglongvu@gmail.com
 */
public class MbrItemFragment extends BasicFragment implements MbrItemView {

    @BindView(R.id.img_main_avatar)
    RoundImage imgMainAvatar;
    @BindView(R.id.img_edit)
    ImageButton imgEdit;
    @BindView(R.id.txt_share_to)
    TextView txtShareTo;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_birthday)
    TextView txtBirthday;
    @BindView(R.id.txt_gender)
    TextView txtGender;
    @BindView(R.id.recyclerview_link)
    RecyclerView recyclerviewLink;
    @BindView(R.id.recyclerview_share)
    RecyclerView recyclerviewShare;
    @BindView(R.id.img_avatar)
    RoundImage imgAvatarCreator;
    @BindView(R.id.viewList)
    View viewList;

    @OnClick(R.id.viewList)
    public void onClickView() {
        startActivity(ShareLinkListActivity.class, Constants.MRB_ID, mbr.getMrbId());
    }

    Unbinder unbinder;

    @BindView(R.id.ln_info)
    LinearLayout ln_info;

    @OnClick(R.id.ln_info)
    public void clickLink() {
        Log.e("MbrItemFragment", "clickLink: ");
        startActivity(ShareLinkListActivity.class, Constants.MRB_ID, mbr.getMrbId());
    }

    private Avatar16Adapter adapterShare;

    private Avatar16Adapter adapterLink;

    private Mbr mbr;

    private MbrItemPresenter presenter;
    /**
     * Listen receive
     */

    private BroadcastReceiver delete_receive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int mrbId = intent.getIntExtra(Constants.MRB_ID, -1);
                int share_id = intent.getIntExtra(Constants.SHARE_ID, -1);
                int shareType = intent.getIntExtra(Constants.TYPE, -1);
                if (mrbId > 0 && share_id > 0 && shareType > 0){
                    presenter.deleteShareLinkByID(share_id, shareType, mrbId);
                }
            }
        }
    };

    public static MbrItemFragment newInstance(int mbrId) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.OBJECT, mbrId);

        MbrItemFragment fragment = new MbrItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MbrItemPresenter(this);
        if (getActivity() != null) {
            getActivity().registerReceiver(delete_receive, new IntentFilter(Constants.ACTION_DELETE));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mbr_item, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    /**
     * Nhận dữ liệu truyền sang
     */
    private void getData() {
        int mbrId = getIntExtra(Constants.OBJECT);

        if (mbrId == -1)
            return;

        new GetObjectByKeyModel<Mbr>(Mbr.class, "mrbId", mbrId) {
            @Override
            protected void onSuccess(Mbr object) {
                mbr = object;
                if (mbr != null) {
                    setData();
                }
            }
        };
    }

    private void setData() {
        setTextData();
        initActionListener();
        separateShare();
    }

    /**
     * Set dữ liệu (dạng text) vào view
     */
    private void setTextData() {
        int resourceAvatar = mbr.getGender() == 1 ? R.mipmap.ic_female_avatar : R.mipmap.ic_male_avatar;

        WidgetUtils.setImageURL(imgMainAvatar, mbr.getAvatar(), resourceAvatar);
        WidgetUtils.setImageURL(imgAvatarCreator, mbr.getCreatorAvatar(), R.mipmap.ic_small_avatar_default);

        txtName.setText(mbr.getName());

        String birthday = TimeUtils.convertLongToDate(mbr.getBirthdayLong());
        if (birthday == null)
            birthday = getString(R.string.layout_not_update);

        txtBirthday.setText(birthday);

        int resourceGender = mbr.getGender() == 1 ? R.mipmap.ic_gender_female_black_14 : R.mipmap.ic_gender_male_black_14;
        txtGender.setText(TextUtils.getGender(mbr.getGender()));
        WidgetUtils.setTextViewDrawable(txtGender, 0, resourceGender);
    }

    private void initActionListener() {
        if (mbr.getShareType() == 2) {
            if (mbr.getShareTo() == 1) {
                imgEdit.setImageResource(R.mipmap.ic_view_blue_18);
                txtShareTo.setText(getString(R.string.layout_mbr_view_only));
            } else if (mbr.getShareTo() == 2) {
                txtShareTo.setText(getString(R.string.layout_mbr_view_and_edit));
            }
        }

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MbrUpdateActivity.class);
                intent.putExtra(Constants.OBJECT, mbr.getMrbId());
                getActivity().startActivityForResult(intent, HomeActivity.REQUEST_MBR_UPDATE);
            }
        });
    }

    /**
     * Khởi tạo danh sách Chia sẻ
     */
    private void initShare(final List<ShareAccounts> shareList) {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerviewShare.setHasFixedSize(false);
        recyclerviewShare.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        adapterShare = new Avatar16Adapter();
        adapterShare.setListData(shareList);
        recyclerviewShare.setAdapter(adapterShare);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int lastVisiblePosition = ((LinearLayoutManager) recyclerviewShare.getLayoutManager()).findLastVisibleItemPosition();

                    if (lastVisiblePosition != -1 && lastVisiblePosition < shareList.size() - 1) {
                        for (int i = shareList.size() - 1; i >= lastVisiblePosition; i--) {
                            shareList.remove(i);
                        }

                        shareList.add(null);
                        adapterShare.setListData(shareList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    /**
     * Khởi tạo danh sách Liên kết
     */
    private void initLink(final List<ShareAccounts> linkList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerviewLink.setHasFixedSize(false);
        recyclerviewLink.setLayoutManager(layoutManager);

        adapterLink = new Avatar16Adapter();
        adapterLink.setListData(linkList);
        recyclerviewLink.setAdapter(adapterLink);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int lastVisiblePosition = ((LinearLayoutManager) recyclerviewLink.getLayoutManager()).findLastVisibleItemPosition();

                    if (lastVisiblePosition != -1 && lastVisiblePosition < linkList.size() - 1) {
                        for (int i = linkList.size() - 1; i >= lastVisiblePosition; i--) {
                            linkList.remove(i);
                        }

                        linkList.add(null);
                        adapterLink.setListData(linkList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 500);
    }

    /**
     * Tách danh sách share và liên kết
     */
    private void separateShare() {
        List<ShareAccounts> shareList = new ArrayList<>();
        List<ShareAccounts> linkList = new ArrayList<>();

        for (ShareAccounts shareAccounts : mbr.getShareAccounts()) {
            if (shareAccounts.getShareType() == 1)
                shareList.add(shareAccounts);
            else
                linkList.add(shareAccounts);
        }

        initShare(shareList);
        initLink(linkList);
//        adapterLink.setListFriends(linkList);
//        adapterShare.setListFriends(shareList);
    }

    /**
     * Lấy danh sách Sổ khám sức khỏe
     *
     * @return danh sách
     */
    public List<HealthRecords> getHealthRecords() {
        return mbr != null ? mbr.getHealthRecords() : new ArrayList<HealthRecords>();
    }

    public Mbr getMbr() {
        return mbr;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            getActivity().registerReceiver(delete_receive, new IntentFilter(Constants.ACTION_DELETE));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getActivity() != null) {
            getActivity().unregisterReceiver(delete_receive);
        }
    }

    @Override
    public void removeShareLink(int id_share, int action_type, int mbrId) {
        if (mbr.getMrbId() == mbrId){
            if (action_type == 2){
                MyApplication.log("List Link", adapterLink.getList().toString());
                for (int i = 0; i < adapterLink.getList().size(); i++) {
                    if (adapterLink.getList().get(i).getId() == id_share){
                        adapterLink.removeItem(adapterLink.getList().get(i).getId());
                    }
                }
            } else if (action_type == 1){
                MyApplication.log("List Share", adapterShare.getList().toString());
                for (int i = 0; i < adapterShare.getList().size(); i++) {
                    if (adapterShare.getList().get(i).getId() == id_share){
                        adapterShare.removeItem(adapterShare.getList().get(i).getId());
                    }
                }
            }
        }
    }

    @Override
    public void notifyData(Mbr mbr) {
        this.mbr = mbr;
        setData();
    }
}