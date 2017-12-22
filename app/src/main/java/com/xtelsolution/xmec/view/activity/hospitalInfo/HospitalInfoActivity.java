package com.xtelsolution.xmec.view.activity.hospitalInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.DisplayUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.Utils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.MapInfo;
import com.xtelsolution.xmec.model.entity.RatingObject;
import com.xtelsolution.xmec.model.entity.SelfRateObject;
import com.xtelsolution.xmec.model.resp.RESP_Hospital_Info;
import com.xtelsolution.xmec.presenter.hospitaldetail.HospitalDetailPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.mapview.MapsActivity;
import com.xtelsolution.xmec.view.activity.rate.RatesActivity;
import com.xtelsolution.xmec.view.widget.BubbleTransformation;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.util.ArrayList;
import java.util.List;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.xtelsolution.sdk.commons.Constants.ID;

public class HospitalInfoActivity extends BasicActivity implements IHospitalDetailView, AdapterRateList.OnItemClickListener, View.OnClickListener, OnMapReadyCallback {
    private static final String TAG = "HospitalInfoActivity";
    @BindView(R.id.view_map)
    View viewMap;
    @BindView(R.id.ln_address)
    LinearLayout lnAddress;
    @BindView(R.id.ln_phone)
    LinearLayout lnPhone;
    @BindView(R.id.ln_website)
    LinearLayout lnWebsite;
    @BindView(R.id.img_action_expand)
    ImageView imgActionExpand;
    @BindView(R.id.v_destination_hospital_image)
    View vDestinationHospitalImage;
    @BindView(R.id.fr_avatar)
    FrameLayout fr_avatar;
    private Target hospitalTarget;
    Marker hospital_marker;

    @OnClick(R.id.view_map)
    public void inflateMap() {
        if (mapInfo != null) {
            startActivity(MapsActivity.class, Constants.MAP_INFO, mapInfo);
        }
    }

    public static void start(Context context, int id) {
        Intent starter = new Intent(context, HospitalInfoActivity.class);
        starter.putExtra(ID, id);
        Log.e(TAG, "start: " + id);
        context.startActivity(starter);
    }

    private static final int RATE_CODE = 502;
    @BindView(R.id.bg_hospital)
    ImageView bgHospital;

    @BindView(R.id.tv_hospital_name)
    TextView tvHospitalName;
    @BindView(R.id.tv_hospital_address)
    TextView tvHospitalAddress;
    @BindView(R.id.tv_hospital_phone)
    TextView tvHospitalPhone;
    @BindView(R.id.tv_hospital_website)
    TextView tvHospitalWebsite;
    @BindView(R.id.tv_hospital_time_working)
    TextView tvHospitalTimeWorking;
    @BindView(R.id.rt_hospital_rate)
    MaterialRatingBar rtHospitalRate;
    @BindView(R.id.tv_hospital_sumary)
    ExpandableTextView tvHospitalSumary;
    @BindView(R.id.img_avatar_rate)
    RoundImage imgAvatarRate;
    @BindView(R.id.tv_name_rate)
    TextView tvNameRate;
    @BindView(R.id.rt_user_rate)
    MaterialRatingBar rtUserRate;
    @BindView(R.id.rcl_rated)
    RecyclerView rclRated;
    @BindView(R.id.img_avatar_hospital)
    RoundImage img_avatar_hospital;

    @BindView(R.id.tv_expand)
    TextView tvExpand;
    @BindView(R.id.ln_action_rate)
    FrameLayout lnActionRate;

    Toolbar toolbar;
    @BindView(R.id.tv_hospital_sumary_title)
    TextView tvHospitalSumaryTitle;
    @BindView(R.id.ln_rate_info)
    LinearLayout lnRateInfo;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;
    @BindView(R.id.btn_action_rate)
    Button btnActionRate;

    @OnClick(R.id.ln_rate_info)
    public void getInfo() {
        if (object != null) {
            startActivity(RatedInfoActivity.class, Constants.OBJECT, object);
        }
    }

    @OnClick(R.id.tv_load_more)
    public void loadMore() {
        page++;
        presenter.getRateHospitalById(id, page, 10);
    }

    @OnClick(R.id.tv_expand)
    public void expadnContent() {
        tvHospitalSumary.toggle();
    }

    @OnClick(R.id.btn_action_rate)
    public void actionBtnRate() {
        if (isExists) {
            startActivityForResult(RatesActivity.class, Constants.ID, id, Constants.OBJECT, object, Constants.TYPE, 1, RATE_CODE);
        } else {
            startActivityForResult(RatesActivity.class, Constants.ID, id, Constants.TYPE, 0, RATE_CODE);
        }
    }

    SupportMapFragment mapFragment;
    int id;
    int page = 1;
    private HospitalDetailPresenter presenter;
    private AdapterRateList adapterRateList;
    private List<RatingObject> list;
    private boolean isExpand = false;
    private boolean isExists = false;
    private SelfRateObject object;
    private GoogleMap mMap;
    private MapInfo mapInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_info);
        ButterKnife.bind(this);
        presenter = new HospitalDetailPresenter(this);
        initRecyclerView();
        getData();
    }

    private void initRecyclerView() {
        mapInfo = new MapInfo();
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        list = new ArrayList<>();
        rclRated.setLayoutManager(new LinearLayoutManager(this));
        adapterRateList = new AdapterRateList(this, list, this);
        rclRated.setAdapter(adapterRateList);
    }

    private void getData() {
        showProgressBar(false, false, "Đang tải...");
        id = getIntent().getIntExtra(ID, -1);
        Log.e(TAG, "getData: " + id);
        if (id > -1) {
            presenter.getHospitalById(id);
        }
    }

    @Override
    public void getHospitalDetailSuccess(RESP_Hospital_Info info) {
        if (info.getSelfRate() != null) {
            object = info.getSelfRate();
            list.add(new RatingObject(info.getSelfRate()));
            btnActionRate.setText("Cật nhật đánh giá");
            isExists = true;
            lnActionRate.setVisibility(View.VISIBLE);
            btnActionRate.setVisibility(View.VISIBLE);
            setMyRate(info.getSelfRate());
        } else {
            btnActionRate.setText("Đánh giá");
            if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
                isExists = false;
                lnActionRate.setVisibility(View.GONE);
                btnActionRate.setVisibility(View.VISIBLE);
            } else {
                lnActionRate.setVisibility(View.GONE);
                btnActionRate.setVisibility(View.GONE);
            }


        }

        setData(info);
        closeProgress();
        presenter.getRateHospitalById(id, page, 10);
    }

    private void setData(final RESP_Hospital_Info info) {
        if (info != null) {
            initToolbars(info.getName());
            MyApplication.log("SET DATA", info.toString());

            setField(info);
            setMap(info.getLat(), info.getLng(), info.getName(), info.getImage(), info.getAddress());
            img_avatar_hospital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImage(info.getImage());
                }
            });
            bgHospital.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImage(info.getImage());
                }
            });
        }
    }

    public void showImage(String url) {
        if (url != null) {
            Utils.showImage(this, url);
        }
    }

    private void setListRate(List<RatingObject> data) {
        list.addAll(data);
        Log.e(TAG, "setListRate: " + list.toString());
        adapterRateList.notifyDataSetChanged();
        if (list.size() > 0) {
            int total_score = 0;
            for (int i = 0; i < list.size(); i++) {
                total_score += (list.get(i).getHygieneRate()
                        + list.get(i).getServiceRate()
                        + list.get(i).getQualityRate());
            }
            int totalSize = list.size();
            float rateStar = ((total_score / totalSize) / 3);
            rtHospitalRate.setRating(Math.round(rateStar));
        }
    }

    private void setField(RESP_Hospital_Info info) {
        if (info.getImage() == null || info.getImage().equals("")) {
            img_avatar_hospital.setVisibility(View.GONE);
            vDestinationHospitalImage.setVisibility(View.GONE);
            fr_avatar.setVisibility(View.GONE);
        } else {

            WidgetUtils.setImageURL(img_avatar_hospital, info.getImage(), -1);
            WidgetUtils.setImageURL(bgHospital, info.getImage(), -1);
        }
        if (info.getAddress() != null && !info.getAddress().isEmpty()) {
            tvHospitalAddress.setText("Địa chỉ: " + info.getAddress());
        } else {
            tvHospitalAddress.setText(getString(R.string.not_yet));
            lnAddress.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(info.getName())) {
            tvHospitalName.setText(info.getName());
        } else {
            tvHospitalName.setText(getString(R.string.not_yet));
        }

        if (info.getPhone() != null && !TextUtils.isEmpty(info.getPhone())) {
            tvHospitalPhone.setText("Điện thoại: " + info.getPhone());
        } else {
            tvHospitalPhone.setText(getString(R.string.not_yet));
            lnPhone.setVisibility(View.GONE);
        }

        if (info.getWorkingTime() != null && !TextUtils.isEmpty(info.getWorkingTime())) {
            tvHospitalTimeWorking.setText("Giờ làm việc: \n" + Html.fromHtml(info.getWorkingTime().replace("<p>", "<br>* ").replace("</p>", "")));
        } else {
            tvHospitalTimeWorking.setText(getString(R.string.not_yet));
        }

        lnWebsite.setVisibility(View.GONE);

        if (info.getDescription() != null && !TextUtils.isEmpty(info.getDescription())) {
            tvHospitalSumaryTitle.setText("Giới thiệu");
            tvHospitalSumary.setText(Html.fromHtml(info.getDescription()));
            tvExpand.setVisibility(View.VISIBLE);
            tvExpand.setOnClickListener(this);
            if (tvHospitalSumary.getLineCount() < 3) {
                tvExpand.setVisibility(View.GONE);
                imgActionExpand.setVisibility(View.GONE);
            } else {
                tvExpand.setVisibility(View.VISIBLE);
                imgActionExpand.setVisibility(View.VISIBLE);
            }
        } else {
            tvHospitalSumaryTitle.setText(getString(R.string.not_yet));
            tvHospitalSumary.setVisibility(View.GONE);
            tvExpand.setVisibility(View.GONE);
            imgActionExpand.setVisibility(View.VISIBLE);
        }
    }

    private void setMyRate(SelfRateObject selfRate) {
        WidgetUtils.setImageURL(imgAvatarRate, selfRate.getAvatar(), R.mipmap.ic_small_avatar_default);
        float total_rate = (selfRate.getHygieneRate() + selfRate.getQualityRate() + selfRate.getServiceRate()) / 3;
        rtUserRate.setRating(Math.round(total_rate));
        tvNameRate.setText(selfRate.getFullname());
    }

    private void setMap(double lat, double lng, String hospital_name, String image, String address) {
        mapInfo.setLat(lat);
        mapInfo.setLng(lng);
        mapInfo.setName(hospital_name);
        mapInfo.setImage(image);
        mapInfo.setAddress(address);
        if (mMap != null) {
            hospitalTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    hospital_marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(mapInfo.getLat(), mapInfo.getLng()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    );
                    hospital_marker.showInfoWindow();
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.d("picasso", "onBitmapFailed");
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            String mapImage = com.xtelsolution.sdk.utils.TextUtils.appendImageUrlToPath(mapInfo.getImage());
            if (mapImage != null) {
                Picasso.with(this)
                        .load(mapImage)
                        .resize(DisplayUtils.convertDpToPixels(64, getApplicationContext()), DisplayUtils.convertDpToPixels(64, getApplicationContext()))
                        .centerCrop()
                        .placeholder(R.mipmap.ic_small_avatar_default)
                        .error(R.mipmap.ic_small_avatar_default)
                        .transform(new BubbleTransformation(3))
                        .into(hospitalTarget);
            } else {
                Picasso.with(this)
                        .load(R.mipmap.ic_small_avatar_default)
                        .resize(DisplayUtils.convertDpToPixels(64, getApplicationContext()), DisplayUtils.convertDpToPixels(64, getApplicationContext()))
                        .centerCrop()
                        .placeholder(R.mipmap.ic_small_avatar_default)
                        .error(R.mipmap.ic_small_avatar_default)
                        .transform(new BubbleTransformation(3))
                        .into(hospitalTarget);
            }

            LatLng hospital_position = new LatLng(lat, lng);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hospital_position, 15));
            mMap.getUiSettings().setScrollGesturesEnabled(false);
        }
    }

    @Override
    public void getHospitalDetailError(String mes) {
        Snackbar.make(getWindow().getDecorView().getRootView(), mes, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void closeProgress() {
        closeProgressBar();
    }

    @Override
    public void showProgress() {
        showProgressBar(true, true, "Đang tải...");
    }

    @Override
    public void getRateSuccess(List<RatingObject> data) {
        tvState.setVisibility(View.GONE);
        if (data.size() > 8) {
            tvLoadMore.setVisibility(View.VISIBLE);
        } else {
            tvLoadMore.setVisibility(View.GONE);
        }
        setListRate(data);
    }

    @Override
    public void emptyListRate(String s) {
        tvState.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_expand) {
            if (isExpand) {
                imgActionExpand.setRotation(0);
                tvExpand.setText("Mở rộng");
                isExpand = false;
                tvHospitalSumary.collapse();
            } else {
                imgActionExpand.setRotation(180);
                isExpand = true;
                tvExpand.setText("Thu nhỏ");
                tvHospitalSumary.expand();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in HANOI and move the camera
        LatLng HANOI = new LatLng(21.033333, 105.849998);
        mMap.addMarker(new MarkerOptions().position(HANOI).title("Marker in Hanoi"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HANOI, 15));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void initToolbars(String title) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (title != null)
            actionBar.setTitle(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RATE_CODE) {
            if (data != null) {
                if (data.getAction().equals("RELOAD")) {
                    list.clear();
                    presenter.getHospitalById(id);
                }
            }
        }
    }
}
