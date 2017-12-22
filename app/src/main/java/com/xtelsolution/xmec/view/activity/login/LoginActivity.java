package com.xtelsolution.xmec.view.activity.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.FragmentsAdapter;
import com.xtelsolution.xmec.model.entity.Fragments;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.fragment.login.LoginFragment;
import com.xtelsolution.xmec.view.fragment.login.RegisterFragment;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BasicActivity {

    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.img_back_stack)
    ImageButton imgBackStack;
    @OnClick(R.id.img_back_stack)
    public void setBack(){
        finish();
        SharedUtils.getInstance().putIntValue(Constants.GO_TO_ITEM, -1);
    }

    private FragmentsAdapter adapter;
    private boolean showBack;
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initViewPager();
        setListenerToRootView();

        KeyboardUtils.autoHideKeboard(LoginActivity.this, findViewById(R.id.rootView));
        getData();
//        autoHideKeboard(findViewById(R.id.rootView));
    }

    private void getData() {
        showBack = getIntent().getBooleanExtra(Constants.ENABLE, false);
        position = getIntent().getIntExtra(Constants.POSITION, -1);
        SharedUtils.getInstance().putIntValue(Constants.GO_TO_ITEM, position);
        if (showBack){
            imgBackStack.setVisibility(View.VISIBLE);
        } else {
            imgBackStack.setVisibility(View.GONE);
        }
    }

    private void initViewPager() {
        List<Fragments> list = new ArrayList<>();

        list.add(new Fragments(LoginFragment.newInstance(), getString(R.string.layout_lg_login)));
        list.add(new Fragments(RegisterFragment.newInstance(), getString(R.string.layout_lg_register)));

        adapter = new FragmentsAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);

        tablayout.setupWithViewPager(viewpager);
    }

    public void setDataToLogin(String phone, String password) {
        ((LoginFragment) adapter.getItem(0)).setData(phone, password);
    }

    public void goToLogin(boolean isLogin) {
        viewpager.setCurrentItem(0);

        if (isLogin)
            ((LoginFragment) adapter.getItem(0)).autoLogin();
    }

    public void setListenerToRootView() {

        KeyboardVisibilityEvent.setEventListener(
                this,
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        // some code depending on keyboard visiblity status

                        if (isOpen)
                            imgLogo.setVisibility(View.GONE);
                        else
                            imgLogo.setVisibility(View.VISIBLE);
                    }
                });
    }

//    public static void hideSoftKeyboard(Activity activity) {
//        try {
//            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//            //noinspection ConstantConditions
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void autoHideKeboard(View view) {
//
//        //Set up touch listener for non-text box views to hide keyboard.
//        if (!(view instanceof TextInputEditText)) {
//
//            view.setOnTouchListener(new View.OnTouchListener() {
//
//                public boolean onTouch(View v, MotionEvent event) {
//                    hideSoftKeyboard(LoginActivity.this);
//                    return false;
//                }
//
//            });
//        }
//
//        //If a layout container, iterate over children and seed recursion.
//        if (view instanceof ViewGroup) {
//
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//
//                View innerView = ((ViewGroup) view).getChildAt(i);
//
//                autoHideKeboard(innerView);
//            }
//        }
//    }
}