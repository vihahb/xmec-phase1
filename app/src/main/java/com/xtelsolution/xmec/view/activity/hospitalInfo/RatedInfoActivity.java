package com.xtelsolution.xmec.view.activity.hospitalInfo;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.SelfRateObject;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.widget.RoundImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RatedInfoActivity extends BasicActivity {

    SelfRateObject object;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_avatar_rate)
    RoundImage imgAvatarRate;
    @BindView(R.id.tv_name_rate)
    TextView tvNameRate;
    @BindView(R.id.rt_user_rate)
    MaterialRatingBar rtUserRate;
    @BindView(R.id.rt_rate_quality)
    MaterialRatingBar rtRateQuality;
    @BindView(R.id.rt_rate_hygiene)
    MaterialRatingBar rtRateHygiene;
    @BindView(R.id.rt_rate_service)
    MaterialRatingBar rtRateService;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.btn_close)
    Button btnClose;

    @OnClick(R.id.btn_close)
    public void close() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rated_info);
        ButterKnife.bind(this);
        txtTitle.setText("Chi tiết đánh giá");
        getData();
    }

    private void getData() {
        object = (SelfRateObject) getIntent().getSerializableExtra(Constants.OBJECT);
        if (object != null) {
            setData(object);
        }
    }

    private void setData(SelfRateObject object) {
        float total_rate = (object.getHygieneRate() + object.getQualityRate() + object.getServiceRate()) / 3;
        rtUserRate.setRating(total_rate);

        if (object.getFullname() != null)
            tvNameRate.setText(object.getFullname());
        else
            tvNameRate.setText("Chưa cập nhật!");

        if (object.getComment() != null)
            tvComment.setText(object.getComment());
        else
            tvComment.setText("Chưa có ý kiến!");

        if (!TextUtils.isEmpty(object.getAvatar())){
            WidgetUtils.setImageURL(imgAvatarRate, object.getAvatar(), R.mipmap.ic_small_avatar_default);
        }
        rtRateHygiene.setRating(object.getHygieneRate());
        rtRateService.setRating(object.getServiceRate());
        rtRateQuality.setRating(object.getQualityRate());
    }
}
