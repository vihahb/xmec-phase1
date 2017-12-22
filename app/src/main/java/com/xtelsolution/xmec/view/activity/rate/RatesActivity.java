package com.xtelsolution.xmec.view.activity.rate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.RateObject;
import com.xtelsolution.xmec.model.entity.SelfRateObject;
import com.xtelsolution.xmec.presenter.Rate.RatePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.fragment.BasicFragment;
import com.xtelsolution.xmec.view.widget.HeightWrappingViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RatesActivity extends BasicActivity implements IRatesView, ICallbackRateView {

    private static final String TAG = "RatesActivity";

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.viewpager)
    HeightWrappingViewPager viewpager;
    SectionsPagerAdapter mSectionAdapter;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    int hospital_id = -1;
    int type;
    RatePresenter presenter;

    RateObject object;
    SelfRateObject selfRateObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selfRateObject = new SelfRateObject();
        setContentView(R.layout.activity_rates);
        ButterKnife.bind(this);
        presenter = new RatePresenter(this);
        initViewPager();
        getData();
    }

    private void initViewPager() {
        object = new RateObject();

        mSectionAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewpager.setOffscreenPageLimit(5);
        viewpager.setAdapter(mSectionAdapter);
        indicator.setViewPager(viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3){
                    showInputMethod();
                } else if (position < 3){
                    KeyboardUtils.hideSoftKeyboard(getActivity());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void rateSuccess() {
        closeProgressBar();
        Toast.makeText(getApplicationContext(), "Đánh giá thành công", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent("RELOAD"));
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void rateError(int errorCode) {
        closeProgressBar();
        finish();
        Toast.makeText(getApplicationContext(), "Không thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateSuccess() {
        closeProgressBar();
        Toast.makeText(getApplicationContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
        setResult(Activity.RESULT_OK, new Intent("RELOAD"));
        finish();
    }

    public void getData() {
        hospital_id = getIntent().getIntExtra(Constants.ID, -1);
        type = getIntent().getIntExtra(Constants.TYPE, -1);
        if (hospital_id > 0) {
            object.setHospitalId(hospital_id);
        }

        if (type == 1) {
            selfRateObject = (SelfRateObject) getIntent().getSerializableExtra(Constants.OBJECT);
            object.setId(selfRateObject.getId());
        }
    }

    @Override
    public void onSaveService(float service) {
        if (service > -1) {
            object.setServiceRate(Math.round(service));
        } else {
            object.setServiceRate(0);
        }
        viewpager.setCurrentItem(3);
        Log.e(TAG, "saveService: " + service + "/ total: " + object.toString());
    }

    @Override
    public void onSaveHygiene(float Hygiene) {
        if (Hygiene > -1) {
            object.setHygieneRate(Math.round(Hygiene));
        } else {
            object.setHygieneRate(0);
        }
        viewpager.setCurrentItem(2);
        Log.e(TAG, "saveHygiene: " + Hygiene + "/ total: " + object.toString());
    }

    @Override
    public void onSaveQuality(float quality) {
        if (quality > -1) {
            object.setQualityRate(Math.round(quality));
        } else {
            object.setQualityRate(0);
        }
        viewpager.setCurrentItem(1);
        Log.e(TAG, "saveQuality: " + quality + "/ total: " + object.toString());
    }

    @Override
    public void onSaveComment(String comment) {
        if (comment != null) {
            object.setComment(comment);
        }
        showProgressBar(false, false, "Đang gửi đánh giá");
        switch (type) {
            case 0:
                presenter.addRate(object);
                break;
            case 1:
                presenter.updateRate(object);
                break;
        }
        Log.e(TAG, "saveComment: " + comment + "/ total: " + object.toString());
    }

    /**
     * TODO showInputMethod
     */
    public void showInputMethod() {
        InputMethodManager imm = (InputMethodManager) MyApplication.context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onCloseDialog() {
        finish();
        KeyboardUtils.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onPriviousDialog() {
        viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RatesFragment extends BasicFragment implements View.OnClickListener {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static String TYPE_ACTION = "action";
        @BindView(R.id.btn_close)
        Button btnClose;
        @BindView(R.id.btn_action)
        Button btnAction;
        //        @BindView(R.id.tv_title)
        TextView tvTitle;
        //        @BindView(R.id.rt_rate)
        MaterialRatingBar rtRate;
        TextInputEditText edtRateComment;
        private ICallbackRateView mCallback;
        private int argSection, type;

        public RatesFragment() {
        }

        public static RatesFragment newInstance(int sectionNumber, int type) {
            RatesFragment fragment = new RatesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putInt(TYPE_ACTION, type);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                mCallback = (ICallbackRateView) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement ICallbackRateView");
            }
        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                argSection = getArguments().getInt(ARG_SECTION_NUMBER);
                type = getArguments().getInt(TYPE_ACTION);
            }
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (argSection == 3) {
                View view = inflater.inflate(R.layout.dialog_item_rate_2, container, false);
                edtRateComment = view.findViewById(R.id.edt_rate_comment);
                return view;
            } else {
                View view = inflater.inflate(R.layout.dialog_item_rate_1, container, false);
                rtRate = view.findViewById(R.id.rt_rate);
                rtRate.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                        if (rtRate.getRating() == 0.0) {
                            rtRate.setRating(1);
                        }
                    }
                });
                return view;
            }
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            tvTitle = view.findViewById(R.id.tv_title);
            btnClose = view.findViewById(R.id.btn_close);
            btnAction = view.findViewById(R.id.btn_action);
            btnClose.setOnClickListener(this);
            btnAction.setOnClickListener(this);
            bind();
        }

        private void bind() {
            switch (argSection) {
                case 0:
                    btnClose.setText("Đóng");
                    btnAction.setText("Tiếp");
                    break;
                case 1:
                    btnClose.setText("Quay lại");
                    btnAction.setText("Tiếp");
                    break;
                case 2:
                    btnClose.setText("Quay lại");
                    btnAction.setText("Tiếp");
                    break;
                case 3:
                    btnClose.setText("Quay lại");
                    btnAction.setText("Hoàn tất");
                    break;
            }

            if (getActivity() != null) {
                RatesActivity activity = (RatesActivity) getActivity();
                switch (argSection) {
                    case 0:
                        tvTitle.setText("Chất lượng");
                        if (type > 0) {
                            rtRate.setRating(activity.selfRateObject.getQualityRate());
                        }
                        break;
                    case 1:
                        tvTitle.setText("Vệ sinh");
                        if (type > 0) {
                            rtRate.setRating(activity.selfRateObject.getHygieneRate());
                        }
                        break;
                    case 2:
                        tvTitle.setText("Phục vụ");
                        if (type > 0) {
                            rtRate.setRating(activity.selfRateObject.getServiceRate());
                        }
                        break;
                    case 3:
                        tvTitle.setText("Ý kiến");
                        if (type > 0) {
                            if (activity.selfRateObject.getComment() != null)
                                edtRateComment.setText(activity.selfRateObject.getComment());
                        }
                        break;
                    default:
                        tvTitle.setText("Chất lượng");
                        if (type > 0) {
                            rtRate.setRating(activity.selfRateObject.getQualityRate());
                        }
                        break;
                }
            }
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.btn_close) {
                switch (argSection) {
                    case 0:
                        mCallback.onCloseDialog();
                        break;
                    case 1:
                        mCallback.onPriviousDialog();
                        break;
                    case 2:
                        mCallback.onPriviousDialog();
                        break;
                    case 3:
                        KeyboardUtils.hideSoftKeyboard(getActivity());
                        mCallback.onPriviousDialog();
                        break;
                }
            } else if (view.getId() == R.id.btn_action) {
                switch (argSection) {
                    case 0:
                        if (rtRate.getRating() >= 1L) {
                            mCallback.onSaveQuality(rtRate.getRating());
                        } else {
                            showLongToast("Vui lòng đánh giá từ 1 sao trở lên!");
                        }
                        break;
                    case 1:
                        if (rtRate.getRating() >= 1L) {
                            mCallback.onSaveHygiene(rtRate.getRating());
                        } else {
                            showLongToast("Vui lòng đánh giá từ 1 sao trở lên!");
                        }
                        break;
                    case 2:
                        if (rtRate.getRating() >= 1L) {
                            mCallback.onSaveService(rtRate.getRating());
                        } else {
                            showLongToast("Vui lòng đánh giá từ 1 sao trở lên!");
                        }
                        break;
                    case 3:
                        mCallback.onSaveComment(edtRateComment.getText().toString());
                        break;
                }
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a RatesFragment (defined as a static inner class below).
            return RatesFragment.newInstance(position, type);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }
}
