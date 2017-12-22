package com.xtelsolution.xmec.view.activity.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.FragmentsAdapter;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.presenter.login.SlidePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.activity.inf.ISlideView;
import com.xtelsolution.xmec.view.fragment.login.SlideViewFragment;
import com.xtelsolution.xmec.view.widget.FixedSpeedScroller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlideViewActivity extends BasicActivity implements ISlideView {

    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.btn_phone_login)
    Button btnPhoneLogin;
    @BindView(R.id.tv_lates)
    TextView tvLates;

    @OnClick(R.id.tv_lates)
    public void startNewfeeds() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private Timer timer;

    SlidePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideview);
        ButterKnife.bind(this);
        presenter = new SlidePresenter(this);
        presenter.getBaseUrl();
        setStatusBarColor(R.color.sv_background_1);
        tvLates.setPaintFlags(tvLates.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        boolean show_first = SharedUtils.getInstance().getBooleanValue(Constants.FIRST_SHOW);

        if (show_first) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            SharedUtils.getInstance().putBooleanValue(Constants.FIRST_SHOW, true);
        }

        initViewPager();
        initViewPagerListener();
        initActionListener();
    }

    /**
     * Khởi tạo slide view
     */
    private void initViewPager() {
        List<Fragments> list = new ArrayList<>();

        list.add(new Fragments(SlideViewFragment.newInstance(1)));
        list.add(new Fragments(SlideViewFragment.newInstance(2)));
        list.add(new Fragments(SlideViewFragment.newInstance(3)));
        list.add(new Fragments(SlideViewFragment.newInstance(4)));
        list.add(new Fragments(SlideViewFragment.newInstance(5)));

        FragmentsAdapter adapter = new FragmentsAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);

        tablayout.setupWithViewPager(viewpager);

        timer = new Timer();
        setSchedule();
    }

    /**
     * Khởi tạo sự kiện lắng nghe slide view
     * Đổi màu khi thay đổi tab
     * Dừng auto slide khi user vuốt
     * Set thời gian chuyển tab
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initViewPagerListener() {
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int buttonResource = 0;
                int statusBarColor = 0;

                switch (position) {
                    case 0:
                        buttonResource = R.drawable.button_slide_view_1;
                        statusBarColor = R.color.sv_background_1;
                        break;
                    case 1:
                        buttonResource = R.drawable.button_slide_view_2;
                        statusBarColor = R.color.sv_background_2;
                        break;
                    case 2:
                        buttonResource = R.drawable.button_slide_view_3;
                        statusBarColor = R.color.sv_background_3;
                        break;
                    case 3:
                        buttonResource = R.drawable.button_slide_view_4;
                        statusBarColor = R.color.sv_background_4;
                        break;
                    case 4:
                        buttonResource = R.drawable.button_slide_view_5;
                        statusBarColor = R.color.sv_background_5;
                        break;
                    default:
                        return;
                }

                btnPhoneLogin.setBackgroundResource(buttonResource);
                setStatusBarColor(statusBarColor);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewpager.setOnTouchListener(new View.OnTouchListener() {

            private float downX; //initialized at the start of the swipe action
            private float upX; //initialized at the end of the swipe action

            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();

                        return false;
                    case MotionEvent.ACTION_UP:
                        upX = event.getX();
                        final float deltaX = downX - upX;

                        if (deltaX > 0 && deltaX > 100) {
                            //DETECTED SWIPE TO RIGHT
                            cancelSchedule();
                        }
                        if (deltaX < 0 && -deltaX > 100) {
                            //DETECTED SWIPE TO LEFT
                            cancelSchedule();
                        }

                        return false;
                }

                return false;
            }
        });

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewpager.getContext());
            // scroller.setFixedDuration(5000);
            mScroller.set(viewpager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ignored) {
        }
    }

    /**
     * Khởi tạo sự kiện action khi click vào nút login
     */
    private void initActionListener() {

        btnPhoneLogin.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra(Constants.ENABLE, true);
            startActivity(intent);
        });
    }

    /**
     * Đổi màu của thanh Status Bar
     *
     * @param color màu
     */
    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this, color));
        }
    }

    /**
     * Dừng sự kiện auto chuyển slide
     */
    private void cancelSchedule() {
        if (timer != null)
            timer.cancel();
    }

    /**
     * Hẹn giờ chuyển slide
     */
    private void setSchedule() {
        if (timer != null)
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    new Slide().execute();
                }
            }, 4000);
    }

    /**
     * Chuyển slide sang tab khác
     */
    private class Slide extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            int position = viewpager.getCurrentItem() + 1;

            if (position == 5) {
                viewpager.setCurrentItem(0);
            } else {
                viewpager.setCurrentItem(position);
            }

            setSchedule();
        }
    }
}
