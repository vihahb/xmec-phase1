package com.xtelsolution.xmec.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.ImagePicker;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.FragmentsAdapter;
import com.xtelsolution.xmec.model.entity.FileObject;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.presenter.HomePresenter;
import com.xtelsolution.xmec.view.activity.account.AccountActivity;
import com.xtelsolution.xmec.view.activity.inf.IHomeView;
import com.xtelsolution.xmec.view.activity.login.LoginActivity;
import com.xtelsolution.xmec.view.activity.login.SlideViewActivity;
import com.xtelsolution.xmec.view.activity.notification.NotificationActivity;
import com.xtelsolution.xmec.view.activity.setting.SettingActivity;
import com.xtelsolution.xmec.view.fragment.hospital.HospitalMapFragment;
import com.xtelsolution.xmec.view.fragment.mbr.MbrFragment;
import com.xtelsolution.xmec.view.widget.CountDrawable;
import com.xtelsolution.xmec.view.widget.RoundImage;
import com.xtelsolution.xmec.view.widget.ViewPagerNoScroll;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BasicActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, IHomeView {

    public static final int REQUEST_MBR_CREATE = 1;
    public static final int REQUEST_MBR_UPDATE = 2;
    public static final int REQUEST_ACCOUNT = 3;
    public static final int REQUEST_NOTIFICATION = 503;
    private static int REQUEST_CONTACT = 91;
    private final int PERMISSION_CAMERA = 4;
    private final int REQUEST_IMAGE = 5;
    private final int REQUEST_RESIZE_IMAGE = 6;
    private final int REQUEST_LOGIN = 10;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.viewpager)
    ViewPagerNoScroll viewpager;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    MenuItem share;
    int oldCheckItem;

    private static final String TAG = "HomeActivity";

    private HomePresenter presenter;
    private TextView txtUsername, tv_action_login;
    private RoundImage imgAvatar;
    private ImageButton imgQr;
    private FragmentsAdapter fragmentsAdapter;
    private Menu main_menu;
    private BroadcastReceiver receiverNotify;
    private String[] permission_camera = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private AppBarLayout.LayoutParams params;
    private int item_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        presenter = new HomePresenter(this);
        getDataPosition();
        initDrawer();
        initHeaderView();
        initViewpager();
        initActionListener();
        listenReceive();
        registerReceiver(receiverNotify, new IntentFilter("ACTION_NOTIFY"));
        if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
            checkPermission();
        }
        setupUI(findViewById(R.id.drawer_layout), HomeActivity.this);
    }

    private void getDataPosition() {
        item_position = getIntent().getIntExtra(Constants.GO_TO_ITEM, -1);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                showConfirmDialog(null, getString(R.string.message_overlays), () -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 111);
                    }
                });
            }
        }
    }

    public ViewPagerNoScroll getViewpager() {
        return viewpager;
    }

    /**
     * Khởi tạo slide menu
     */
    private void initDrawer() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        toggle.setToolbarNavigationClickListener(v -> {
            Log.e(TAG, "setToolbarNavigationClickListener: ");
            KeyboardUtils.hideSoftKeyboard(getActivity());
        });

        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationIcon(R.mipmap.ic_menu_white_24);
        actionBar.setDisplayShowTitleEnabled(false);

        navView.setNavigationItemSelectedListener(this);
    }

    /**
     * Khởi tạo header của slide menu
     */
    private void initHeaderView() {
        View headerView = navView.getHeaderView(0);

        txtUsername = headerView.findViewById(R.id.txt_username);
        tv_action_login = headerView.findViewById(R.id.tv_action_login);
        imgAvatar = headerView.findViewById(R.id.img_avatar);
        imgQr = headerView.findViewById(R.id.img_qr);
        ImageButton imgCamera = headerView.findViewById(R.id.img_camera);

        String userName = SharedUtils.getInstance().getStringValue(Constants.USER_FULLNAME);
        String avatar = SharedUtils.getInstance().getStringValue(Constants.USER_AVATAR);

        userName = userName != null ? userName : getString(R.string.layout_not_update);

        txtUsername.setText(userName);
        WidgetUtils.setImageURL(imgAvatar, avatar, R.mipmap.im_docter);

        txtUsername.setOnClickListener(this);
        imgAvatar.setOnClickListener(this);
        imgCamera.setOnClickListener(this);

        if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
            tv_action_login.setVisibility(View.GONE);
            txtUsername.setVisibility(View.VISIBLE);
        } else {
            tv_action_login.setVisibility(View.VISIBLE);
            txtUsername.setVisibility(View.GONE);
            tv_action_login.setOnClickListener(this);
        }
    }

    private void initViewpager() {
        params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager(), presenter.getListTab());
        viewpager.setAdapter(fragmentsAdapter);
        viewpager.setPagingEnabled(false);
        if (SharedUtils.getInstance().getStringValue(Constants.SESSION) == null) {
            viewpager.setCurrentItem(1);
            toolbarTitle.setText(getString(R.string.title_news));
            navView.setCheckedItem(R.id.nav_infomation);
            oldCheckItem = R.id.nav_infomation;
            navView.getMenu().getItem(7).setTitle("Thoát");
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            if (item_position > -1) {
                int item_id = -1;
                viewpager.setCurrentItem(item_position);
                switch (item_position) {
                    case 0:
                        item_id = R.id.nav_mbr;
                        break;
                    case 5:
                        item_id = R.id.nav_share;
                        break;
                }
                if (item_id != -1)
                    navView.setCheckedItem(item_id);
            }
        }
    }

    /**
     * Khởi tạo lắng sự kiện click của người dùng
     */
    private void initActionListener() {
        imgQr.setOnClickListener(this);
    }

    //set count notification badge
    public void setCountBadge(Context context, int count) {
        MenuItem menuItem = main_menu.findItem(R.id.action_notify);
        if (menuItem != null) {
            LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

            CountDrawable badge;

            // Reuse drawable if possible
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
            if (reuse != null && reuse instanceof CountDrawable) {
                badge = (CountDrawable) reuse;
            } else {
                badge = new CountDrawable(context);
            }
            if (count > 99) {
                badge.setCount(String.valueOf(count) + "+");
            } else {
                badge.setCount(String.valueOf(count));
            }
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_group_count, badge);
        }
    }

    /**
     * Listening receive notification count badge
     */
    private void listenReceive() {
        receiverNotify = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    if (main_menu != null) {
                        int badge_count = intent.getIntExtra(Constants.BADGE_COUNT, 0);
                        setCountBadge(HomeActivity.this, badge_count);
                    }
                }
            }
        };
    }

    private void detailAccount() {
        startActivityForResult(AccountActivity.class, REQUEST_ACCOUNT);
    }

    /**
     * Chọn ảnh từ gallery hoặc chụp ảnh
     */
    private void takePicture() {
        if (!PermissionHelper.checkPermission(permission_camera, this, PERMISSION_CAMERA)) {
            return;
        }

        Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
        startActivityForResult(chooseImageIntent, REQUEST_IMAGE);
    }

    /**
     * Resize lại kích thước ảnh
     *
     * @param file ảnh đã chọn
     */
    private void resizeImage(File file) {
        FileObject fileObject = new FileObject();
        fileObject.setFile(file);

        startActivityForResult(ResizeImageActivity.class, Constants.OBJECT, fileObject, REQUEST_RESIZE_IMAGE);
    }

    @Override
    public void onLogoutSuccess(boolean isSuccess) {
        SharedUtils.getInstance().putBooleanValue(Constants.FLAG_NOTIFICATION, false);
        if (isSuccess) {
            startActivityAndFinish(SlideViewActivity.class);
        } else {
            showLongToast(getString(R.string.error_home_logout));
        }
    }

    @Override
    public void onUpdateAvatarSuccess(String filePath) {
        closeProgressBar();
        showLongToast(getString(R.string.success_home_update_avatar));
        WidgetUtils.setSmallImageFile(imgAvatar, filePath, R.mipmap.im_docter);
    }

    @Override
    public void setCount(int count) {
        setCountBadge(this, count);
    }

    @Override
    public void showProgressView(int type) {
        String message;

        switch (type) {
            case 1:
                message = getString(R.string.doing_update);
                break;
            default:
                message = getString(R.string.doing_do);
                break;
        }

        showProgressBar(false, false, message);
    }

    @Override
    public Activity getActivity() {
        return this;
    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.getCountNotify();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_qr:
                break;
            case R.id.txt_username:
                detailAccount();
                break;
            case R.id.tv_action_login:
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra(Constants.ENABLE, true);
                startActivity(intent);
                break;
            case R.id.img_avatar:
                if (checkSession())
                    detailAccount();
                else
                    showShortToast(getString(R.string.need_login_use_feature));
                break;
            case R.id.img_camera:
                detailAccount();
                break;
        }

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private boolean checkSession() {
        return SharedUtils.getInstance().getStringValue(Constants.SESSION) != null;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_mbr:
                params.setScrollFlags(0);
                if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
                    viewpager.setCurrentItem(0);
                    toolbarTitle.setText(getString(R.string.title_activity_mbr));
                } else {
                    startActivityForResult(LoginActivity.class, Constants.ENABLE, true, Constants.POSITION, 0, REQUEST_LOGIN);
                }
                break;
            case R.id.nav_infomation:
                oldCheckItem = R.id.nav_infomation;
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
                viewpager.setCurrentItem(1);
                toolbarTitle.setText(getString(R.string.title_news));
                break;
            case R.id.nav_searching_disease:
                oldCheckItem = R.id.nav_searching_disease;
                params.setScrollFlags(0);
                viewpager.setCurrentItem(2);
                toolbarTitle.setText(getString(R.string.title_search_disease));
                break;
            case R.id.nav_searching_medicine:
                oldCheckItem = R.id.nav_searching_medicine;
                params.setScrollFlags(0);
                viewpager.setCurrentItem(3);
                toolbarTitle.setText(getString(R.string.title_search));
                break;
            case R.id.nav_medical_facility:
                oldCheckItem = R.id.nav_medical_facility;
                params.setScrollFlags(0);
                viewpager.setCurrentItem(4);
                toolbarTitle.setText(getString(R.string.title_search_hospital));
                break;
            case R.id.nav_share:
                params.setScrollFlags(0);
                if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
                    viewpager.setCurrentItem(5);
                    toolbarTitle.setText(getString(R.string.title_share_an));
                } else {
                    startActivityForResult(LoginActivity.class, Constants.ENABLE, true, Constants.POSITION, 0, REQUEST_LOGIN);
                }
                break;
            case R.id.nav_setting:
                oldCheckItem = R.id.nav_setting;
//                NewsPortsModel model = new NewsPortsModel();
//                model.setTitle("abc title");
//                model.setDescription("abc");
//                model.setUrl("http://x-mec.com/api/system/post/detail?id=1");
//                DetailNewsActivity.start(getActivity(), model);
                startActivity(SettingActivity.class);
                break;
            case R.id.nav_logout:
                if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null) {
                    presenter.logout();
                } else {
                    finish();
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (viewpager.getCurrentItem() == 4) {
                ((HospitalMapFragment) fragmentsAdapter.getItem(4)).onBackPressed();
            } else
                super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        this.main_menu = menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        share = main_menu.findItem(R.id.action_share);
        int count = SharedUtils.getInstance().getIntValueDefault(Constants.BADGE_COUNT);
        setCountBadge(this, count);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            if (viewpager.getCurrentItem() == 0) {
                ((MbrFragment) fragmentsAdapter.getItem(0)).ShareAndLinkMbr();
            }
            return true;
        } else if (id == R.id.action_notify) {
            startActivityForResult(NotificationActivity.class, REQUEST_NOTIFICATION);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA) {
            if (PermissionHelper.checkResult(grantResults)) {
                takePicture();
            } else {
                showLongToast(getString(R.string.error_not_allow_permission));
            }
        }
//        if (requestCode == PERMISSION_ALERT) {
//            if (!PermissionHelper.checkResult(grantResults)) {
//                showLongToast(getString(R.string.message_not_allow_permission));
//            }
//        }
//        if (viewpager.getCurrentItem() == 4)
//            for (String permission : permissions) {
//                if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                    int type;
//                    Log.e(TAG, "onRequestPermissionsResult: " + permission);
//                    if (PermissionHelper.isAllowPermission(Manifest.permission.ACCESS_COARSE_LOCATION, getActivity()))
//                        type = 1;
//                    else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
////                    Toast.makeText(getActivity(), "Go to Settings and Grant the permission to use this feature.", Toast.LENGTH_SHORT).show();
//                        type = -1;
//                        // User selected the Never Ask Again Option
//                    } else {
//                        type = 0;
//                    }
//                    // type == 1: quyền được cho phép
//                    // type == 0: quyền bị từ chối
//                    // type == -1: quyền từ chối mãi mãi
//                    if
//                    sendBroadcast(new Intent(Manifest.permission.ACCESS_COARSE_LOCATION).putExtra("type", type));
//
//                }
//            }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        switch (requestCode) {
            case REQUEST_MBR_CREATE:
                fragmentsAdapter.getItem(0).onActivityResult(requestCode, resultCode, data);
                break;
            case REQUEST_MBR_UPDATE:
                fragmentsAdapter.getItem(0).onActivityResult(requestCode, resultCode, data);
                break;
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    File file = ImagePicker.getFileFromResult(this, data);

                    if (file != null) {
                        resizeImage(file);
                    } else {
                        onError(-1);
                    }
                }
                break;
            case REQUEST_RESIZE_IMAGE:
                if (resultCode == RESULT_OK) {
                    FileObject fileObject = null;

                    try {
                        fileObject = (FileObject) data.getSerializableExtra(Constants.OBJECT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    presenter.updateAvatar(fileObject != null ? fileObject.getFile() : null);
                }
                break;
            case REQUEST_ACCOUNT:
                if (resultCode == RESULT_OK) {
                    String fullname = SharedUtils.getInstance().getStringValue(Constants.USER_FULLNAME);
                    String avatar = SharedUtils.getInstance().getStringValue(Constants.USER_AVATAR);
                    String filePath = null;

                    try {
                        filePath = data.getStringExtra(Constants.OBJECT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    txtUsername.setText(fullname);

                    if (filePath != null)
                        WidgetUtils.setSmallImageFile(imgAvatar, filePath, R.mipmap.im_docter);
                    else
                        WidgetUtils.setImageURL(imgAvatar, avatar, R.mipmap.im_docter);
                }
                break;

            case REQUEST_NOTIFICATION:
                if (resultCode == Activity.RESULT_OK) {
                    List<Integer> mbrID = (List<Integer>) data.getSerializableExtra(Constants.ID_LIST);
                    if (mbrID.size() > 0) {
                        Log.e(TAG, "mbrID size: " + mbrID.size());
                        ((MbrFragment) fragmentsAdapter.getItem(0)).addToList(null);
                    }
                }

                fragmentsAdapter.getItem(viewpager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
                break;
            case Constants.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        // All required changes were successfully made
                        Log.e(TAG, "onActivityResult: ok");
                        sendBroadcast(new Intent("REQUEST_SETTINGS_GPS").putExtra("action", Activity.RESULT_OK));
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        Log.e(TAG, "onActivityResult: cancel");
                        sendBroadcast(new Intent("REQUEST_SETTINGS_GPS").putExtra("action", Activity.RESULT_CANCELED));
                        break;
                    default:
                        break;
                }
                break;

            case REQUEST_LOGIN:
                if (resultCode == Activity.RESULT_CANCELED) {
                    navView.setCheckedItem(oldCheckItem);
                }
                break;
        }
    }

    @Override
    public void addToMbrList(Mbr mbr) {
        if (viewpager.getCurrentItem() == 0) {
            ((MbrFragment) fragmentsAdapter.getItem(0)).addToList(mbr);
        }
    }

    @Override
    public void saveCategorySuccess() {
        SharedUtils.getInstance().putBooleanValue(Constants.CATEGORY_SAVE, true);
        MyApplication.log("Home Activity", "Save category success!");
    }

    @Override
    public void saveCategoryError() {
        SharedUtils.getInstance().putBooleanValue(Constants.CATEGORY_SAVE, false);
        MyApplication.log("Home Activity", "Save category Error!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiverNotify);
    }
}