package com.xtelsolution.xmec.view.activity.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.xtelsolution.xmec.presenter.updateShare.EditLinkActivityPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.IEditLinkView;
import com.xtelsolution.xmec.view.widget.RoundImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditLinkActivity extends BasicActivity implements IEditLinkView {

    @BindView(R.id.img_avatar)
    RoundImage img_avatar;
    @BindView(R.id.txt_name)
    TextView txt_name;
    @BindView(R.id.txt_birthday)
    TextView txt_birthday;
    @BindView(R.id.rb_view_and_edit)
    RadioButton rb_view_and_edit;
    @BindView(R.id.rb_view_only)
    RadioButton rb_view_only;
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
    private EditLinkActivityPresenter presenter;
    private int position;
    private int mbrID;

    @OnClick(R.id.btn_cancel)
    public void onCancel() {
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onSave() {
        if (rb_delete.isChecked()) {
            presenter.deleteLink(shareMbr.getShareId(), mbrID, shareMbr.getShareType());
        } else {
            if (rb_view_only.isChecked()) {
                shareTo = 1;
            } else if (rb_view_and_edit.isChecked()) {
                shareTo = 2;
            }
            presenter.updateShare(shareMbr.getShareId(), shareTo, shareMbr.getShareType());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_link);
        ButterKnife.bind(this);
        presenter = new EditLinkActivityPresenter(this);
        getData();
    }

    private void getData() {
        mbrID = getIntent().getIntExtra(Constants.MRB_ID, -1);
        shareMbr = (ShareMbrEntity) getIntent().getSerializableExtra(Constants.OBJECT);
        position = getIntent().getIntExtra(Constants.POSITION, -1);
        if (shareMbr != null) {
            setData(shareMbr);
        }
    }

    private void setData(ShareMbrEntity shareMbr) {
        progressBar.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(shareMbr.getAvatar())) {
            WidgetUtils.setImageURL(img_avatar, shareMbr.getAvatar(), R.mipmap.ic_small_avatar_default);
        }
        txt_name.setText(shareMbr.getFullname());
        String day = shareMbr.getBirthdayLong() != null ? TimeUtils.convertLongToDate(shareMbr.getBirthdayLong()) : null;
        String birthday = day != null ? day : MyApplication.context.getString(R.string.layout_not_update);
        txt_birthday.setText(birthday);

        int select = shareMbr.getShareTo();
        switch (select) {
            case 1:
                rb_view_only.toggle();
                break;
            case 2:
                rb_view_and_edit.toggle();
                break;
        }
    }

    @Override
    public void onDeleteSuccess() {
        showLongToast("Đã xóa liên kết y bạ.");
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
