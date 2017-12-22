package com.xtelsolution.xmec.view.activity.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.ShareMbrEntity;
import com.xtelsolution.xmec.model.entity.UpdateShare;
import com.xtelsolution.xmec.presenter.updateShare.EditShareActivityPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.IEditShareView;
import com.xtelsolution.xmec.view.widget.RoundImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditShareActivity extends BasicActivity implements IEditShareView {

    @BindView(R.id.view_status)
    View viewState;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_avatar)
    RoundImage img_avatar;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_birthday)
    TextView txt_birthday;
    @BindView(R.id.rb_share_to_now)
    RadioButton rb_share_to_now;
    @BindView(R.id.rb_share_to_future)
    RadioButton rb_share_to_future;
    @BindView(R.id.rb_delete)
    RadioButton rb_delete;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    int shareTo;
    @BindView(R.id.btn_save)
    Button btn_save;


    ShareMbrEntity shareMbr;
    private int position;
    private EditShareActivityPresenter presenter;
    private int mbrID;

    @OnClick(R.id.btn_cancel)
    public void onCancel() {
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave() {
        if (rb_delete.isChecked()) {
            presenter.deleteShare(shareMbr.getShareId(), mbrID, shareMbr.getShareType());
        } else {
            if (rb_share_to_now.isChecked()){
                shareTo = 1;
            } else if (rb_share_to_future.isChecked()){
                shareTo = 2;
            }
            presenter.updateShare(shareMbr.getShareId(), shareTo, shareMbr.getShareType());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_share);
        ButterKnife.bind(this);
        presenter = new EditShareActivityPresenter(this);
        getData();
    }

    private void getData() {
        mbrID = getIntent().getIntExtra(Constants.MRB_ID, -1);
        shareMbr = (ShareMbrEntity) getIntent().getSerializableExtra(Constants.OBJECT);
        position = getIntent().getIntExtra(Constants.POSITION, -1);

        if (shareMbr != null) {
            setData(shareMbr);
        }

        boolean isChangeOnlyTime = false;

        try {
            isChangeOnlyTime = getIntent().getBooleanExtra(Constants.OBJECT_2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isChangeOnlyTime) {
            txtTitle.setText(("THỜI ĐIỂM CHIA SẺ"));
        }
    }

    private void setData(ShareMbrEntity shareMbr) {
        progressBar.setVisibility(View.INVISIBLE);
        if (TextUtils.isEmpty(shareMbr.getAvatar())) {
            WidgetUtils.setImageURL(img_avatar, shareMbr.getAvatar(), R.mipmap.ic_small_avatar_default);
        }
        txt_name.setText(shareMbr.getFullname());
        String day = shareMbr.getBirthdayLong() != null ? TimeUtils.convertLongToDate(shareMbr.getBirthdayLong()) : null;
        String birthday = day != null ? day : MyApplication.context.getString(R.string.layout_not_update);
        txt_birthday.setText(birthday);

        int select = shareMbr.getShareTo();
        switch (select) {
            case 1:
                rb_share_to_now.toggle();
                break;
            case 2:
                rb_share_to_future.toggle();
                break;
        }

        int drawable = R.drawable.background_circle_blue;

        switch (shareMbr.getShareState()) {
            case 0:
                drawable = R.drawable.background_circle_green;
                break;
            case 1:
                drawable = R.drawable.background_circle_blue;
                break;
            case 2:
                drawable = R.drawable.background_circle_red;
                break;
        }

        viewState.setBackground(ContextCompat.getDrawable(getApplicationContext(), drawable));
    }

    @Override
    public void onDeleteSuccess() {
        showLongToast("Đã xóa chia sẻ y bạ.");
    }

    @Override
    public void onUpdateSuccess(UpdateShare share) {
        showLongToast("Đã cập nhật thay đổi.");
        shareMbr.setShareTo(share.getShareTo());
        Intent intent = new Intent();
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.OBJECT, shareMbr);
        intent.putExtra(Constants.ACTION_SHARE, 2);
        setResult(91, intent);
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void finishAction() {
        Intent intent = new Intent();
        intent.putExtra(Constants.POSITION, position);
        intent.putExtra(Constants.ACTION_SHARE, 3);
        setResult(RESULT_OK, intent);
        finish();
    }
}
