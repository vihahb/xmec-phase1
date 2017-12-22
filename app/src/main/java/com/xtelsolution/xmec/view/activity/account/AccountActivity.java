package com.xtelsolution.xmec.view.activity.account;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.ImagePicker;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.Utils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.FileObject;
import com.xtelsolution.xmec.presenter.account.AccountPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.ResizeImageActivity;
import com.xtelsolution.xmec.view.activity.inf.account.IAccountView;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends BasicActivity implements IAccountView {
    private AccountPresenter presenter;

    @BindView(R.id.img_avatar_blur)
    ImageView imgAvatarBlur;
    @BindView(R.id.img_avatar)
    RoundImage imgAvatar;
    @BindView(R.id.img_take_picture)
    ImageButton imgTakePicture;
    @BindView(R.id.edt_name)
    TextInputEditText edtName;
    @BindView(R.id.edt_phone)
    TextInputEditText edtPhone;
    @BindView(R.id.btn_change_pass)
    Button btnChangePass;

    private final int PERMISSION_CAMERA = 1;
    private final int REQUEST_IMAGE = 1;
    private final int REQUEST_RESIZE_IMAGE = 2;

    private String[] permission_camera = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        presenter = new AccountPresenter(this);

        initToolbar(R.id.toolbar, getString(R.string.title_activity_account));
        initData();
    }

    private void initData() {
        String name = SharedUtils.getInstance().getStringValue(Constants.USER_FULLNAME);
        String phone = SharedUtils.getInstance().getStringValue(Constants.USER_PHONENUMBER);
        final String avatar = SharedUtils.getInstance().getStringValue(Constants.USER_AVATAR);

        String showPhone;

        if (phone.length() > 0 && phone.charAt(0) == '8') {
            showPhone = "+".concat(phone);
        } else {
            showPhone = phone;
        }

        edtName.setText(name);
        edtPhone.setText(showPhone);
        edtPhone.setEnabled(false);
        edtPhone.setTextColor(ContextCompat.getColor(this, R.color.black_55));

        imgAvatarBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(avatar)){
                    Utils.showImage(getActivity(), avatar);
                } else {
                    Utils.showImageDrawable(getActivity(), R.mipmap.ic_small_avatar_default);
                }
            }
        });

        WidgetUtils.setImageURL(imgAvatar, avatar, R.mipmap.ic_small_avatar_default, new Callback() {
            @Override
            public void onSuccess() {
                WidgetUtils.setImageFileBlur(imgAvatarBlur, imgAvatar, R.mipmap.ic_small_avatar_default);
            }

            @Override
            public void onError() {
                WidgetUtils.setImageFileBlur(imgAvatarBlur, imgAvatar, R.mipmap.ic_small_avatar_default);
            }
        });
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

    private void updateAccount() {
        String name = edtName.getText().toString();

        presenter.updateAccount(name);
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 301:
                edtName.setError(getString(R.string.error_account_name));
                edtName.requestFocus();
                break;
        }
    }

    @OnClick({R.id.img_avatar, R.id.img_take_picture, R.id.btn_change_pass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
                takePicture();
                break;
            case R.id.img_take_picture:
                takePicture();
                break;
            case R.id.btn_change_pass:
                startActivity(ChangePassActivity.class);
                break;
        }
    }

    @Override
    public void onUploadAvatarSuccess(File file) {
        closeProgressBar();

        if (file == null) {
            showLongToast(getString(R.string.error_account_upload_image));
            return;
        }

        showLongToast(getString(R.string.success_account_upload_image));

        WidgetUtils.setSmallImageFile(imgAvatar, file, R.mipmap.ic_small_avatar_default);
        WidgetUtils.setImageFileBlur(imgAvatarBlur, file, R.mipmap.ic_small_avatar_default);
    }

    @Override
    public void onUpdateInfoSuccess(String filePath) {
        closeProgressBar();
        showLongToast(getString(R.string.success_account_info));

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, filePath);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void showProgressView(int type) {
        String message;

        switch (type) {
            case 1:
                message = getString(R.string.doing_upload_image);
                break;
            case 2:
                message = getString(R.string.doing_account_update);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CAMERA) {
            if (PermissionHelper.checkResult(grantResults)) {
                takePicture();
            } else {
                showLongToast(getString(R.string.error_not_allow_permission));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final MenuItem menuItem = menu.findItem(R.id.action_done);
        FrameLayout rootView1 = (FrameLayout) menuItem.getActionView();
        TextView textView1 = rootView1.findViewById(R.id.txt_title);
        textView1.setText(getString(R.string.menu_schedule_save));

        rootView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_done:
                updateAccount();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
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

                    presenter.uploadImage(fileObject != null ? fileObject.getFile() : null);
//                    setAvatar(fileObject);
                }
                break;
        }
    }
}