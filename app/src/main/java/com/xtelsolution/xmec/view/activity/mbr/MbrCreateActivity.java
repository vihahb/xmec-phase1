package com.xtelsolution.xmec.view.activity.mbr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.xtelsolution.sdk.callback.DatePickerListener;
import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.ImagePicker;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.TagViewAdapter;
import com.xtelsolution.xmec.adapter.mbr.MbrSpinnerAdapter;
import com.xtelsolution.xmec.model.entity.FileObject;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.resp.RESP_Friends;
import com.xtelsolution.xmec.presenter.mbr.MbrCreatePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.ResizeImageActivity;
import com.xtelsolution.xmec.view.activity.inf.mbr.IMbrCreateView;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class MbrCreateActivity extends BasicActivity implements View.OnClickListener, IMbrCreateView {
    private MbrCreatePresenter presenter;

    @BindView(R.id.img_avatar_blur)
    ImageView imgAvatarBlur;
    @BindView(R.id.img_avatar)
    RoundImage imgAvatar;
    @BindView(R.id.img_take_picture)
    ImageButton imgTakePicture;
    @BindView(R.id.edt_name)
    TextInputEditText edtName;
    @BindView(R.id.edt_gender)
    TextInputEditText edtGender;
    @BindView(R.id.spinner_gender)
    Spinner spinnerGender;
    @BindView(R.id.edt_birthday)
    TextInputEditText edtBirthday;
    @BindView(R.id.edt_relationship)
    TextInputEditText edtRelationship;
    @BindView(R.id.edt_link)
    TextInputEditText edtLink;
    @BindView(R.id.spinner_relationship)
    Spinner spinnerRelationship;
    @BindView(R.id.view_1)
    View lineLink;
    @BindView(R.id.img_add)
    ImageButton imgAdd;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.btn_create)
    Button btnCreate;

    private TagViewAdapter tagViewAdapter;
    private MbrSpinnerAdapter relationshipAdapter;

    private File avatar;

    private final int PERMISSION_CAMERA = 1;
    private final int REQUEST_IMAGE = 2;
    private final int REQUEST_RESIZE_IMAGE = 3;
    private final int REQUEST_LINK_FRIEND = 4;

    private String[] permission_camera = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbr_create);
        ButterKnife.bind(this);

        initToolbar(R.id.toolbar, getString(R.string.title_activity_mbr_create));
        initDefaultData();
        initRelationship();
        initLinkAccount();
        initGender();
        initActionListener();

        KeyboardUtils.autoHideKeboard(MbrCreateActivity.this, findViewById(R.id.rootView));
    }

    private void initDefaultData() {
        presenter = new MbrCreatePresenter(this);

        edtBirthday.setText(TimeUtils.getToday());
    }

    /**
     * Khởi tạo view chọn giới tính
     */
    private void initGender() {
        String[] listGender = getResources().getStringArray(R.array.gender);
        final MbrSpinnerAdapter adapter = new MbrSpinnerAdapter();
        adapter.setList(listGender);

        final String[] male_re = getResources().getStringArray(R.array.male_relationship);
        final String[] female_re = getResources().getStringArray(R.array.female_relationship);

        spinnerGender.setAdapter(adapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    if (!Arrays.equals(relationshipAdapter.getList(), male_re)) {
                        relationshipAdapter.setList(male_re);
                        edtRelationship.setText(male_re[0]);
                    }
                } else {
                    if (!Arrays.equals(relationshipAdapter.getList(), female_re)) {
                        relationshipAdapter.setList(female_re);
                        edtRelationship.setText(female_re[0]);
                    }
                }

                edtGender.setText(adapter.getItemContent(i));
                initImageDefault();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerGender.setSelection(1);
    }

    /**
     * Set Avatar default
     */
    private void initImageDefault() {
        if (avatar != null)
            return;

        int resource = spinnerGender.getSelectedItemPosition() == 0 ? R.mipmap.ic_female_avatar : R.mipmap.ic_male_avatar;

        imgAvatar.setImageResource(resource);
        WidgetUtils.setImageFileBlur(imgAvatarBlur, imgAvatar, resource);
    }

    /**
     * Khởi tạo view chọn Quan hệ
     */
    private void initRelationship() {
        relationshipAdapter = new MbrSpinnerAdapter();
        spinnerRelationship.setAdapter(relationshipAdapter);

        spinnerRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                edtRelationship.setText(relationshipAdapter.getItemContent(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initLinkAccount() {
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        tagViewAdapter = new TagViewAdapter();
        recyclerView.setAdapter(tagViewAdapter);

        tagViewAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                tagViewAdapter.removeItem(position);

                if (tagViewAdapter.getItemCount() == 0) {
                    edtLink.setText(null);
                    lineLink.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * Khởi tạo lắng nghe khi người dùng tương tác
     */
    private void initActionListener() {
        imgAvatar.setOnClickListener(this);
        imgTakePicture.setOnClickListener(this);
        edtBirthday.setOnClickListener(this);
        edtLink.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
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

    /**
     * Load ảnh đã chọn
     */
    private void setAvatar(FileObject fileObject) {
        if (fileObject == null) {
            onError(Constants.ERROR_UNKOW);
            return;
        }

        avatar = fileObject.getFile();

        WidgetUtils.setSmallImageFile(imgAvatar, avatar, R.mipmap.ic_small_avatar_default);
        WidgetUtils.setImageFileBlur(imgAvatarBlur, avatar, R.mipmap.ic_small_avatar_default);
    }

    /**
     * Chọn ngày sinh
     */
    private void selectBirthday() {
        String birthday = edtBirthday.getText().toString();

        TimeUtils.datePicker(MbrCreateActivity.this, birthday, true, new DatePickerListener() {
            @Override
            public void onTimeSet(String date, long miliseconds) {
                edtBirthday.setText(date);
            }
        });
    }

    /**
     * Thêm account chia sẻ
     */
    private void shareAccount() {
        int count = tagViewAdapter.getItemCount();

        if (count == 6) {
            showLongToast(getString(R.string.error_mbr_share_over_5));
            return;
        }

        startActivityForResult(FriendSearchActivity.class, Constants.OBJECT, count, REQUEST_LINK_FRIEND);
    }

    /**
     * Tạo y bạ
     */
    private void createMbr() {
        String name = edtName.getText().toString().trim();
        int gender = spinnerGender.getSelectedItemPosition() + 1;
        String birthDay = edtBirthday.getText().toString();
        int relationshipType = spinnerRelationship.getSelectedItemPosition();

        presenter.createMbr(avatar, name, gender, birthDay, relationshipType);
    }

    /**
     * Insert bạn bè vào danh sách liên kết
     */
    private void insertLinkAccount(RESP_Friends respFriends) {
        if (respFriends == null) {
            onError(Constants.ERROR_UNKOW);
            return;
        }

        tagViewAdapter.addListData(respFriends.getData());

        if (tagViewAdapter.getListData().size() > 0) {
            edtLink.setText(" ");
            lineLink.setVisibility(View.GONE);
        }
    }

    private void onShareError() {
        showMaterialDialog(false, false, null, getString(R.string.message_ca_error_link_try_again), getString(R.string.layout_disagree), getString(R.string.layout_agree), new DialogListener() {
            @Override
            public void negativeClicked() {
                finish();
            }

            @Override
            public void positiveClicked() {
                showProgressBar(false, false, getString(R.string.doing_mbr_link));
                presenter.shareLink();
            }
        });
    }

    /**
     * Tạo y bạ thành công
     */
    @Override
    public void onCreateSuccess(Mbr mbr) {
        closeProgressBar();
        showLongToast(getString(R.string.success_mbr_create));

        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, mbr.getMrbId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 300:
                showLongToast(getString(R.string.error_mbr_create_avatar));
                break;
            case 301:
                edtName.setError(getString(R.string.error_mbr_create_name));
                edtName.requestFocus();
                break;
            case 302:
                showLongToast(getString(R.string.error_mbr_create_gender));
                break;
            case 303:
                edtBirthday.setError(getString(R.string.error_mbr_create_birthday));
                edtBirthday.requestFocus();
                break;
            case 304:
                showLongToast(getString(R.string.error_mbr_create_relationship));
                break;
            case 305:
                onShareError();
                break;
            case 306:
                showLongToast(getString(R.string.error_mbr_create_invalide_birthday));
                break;
        }
    }

    @Override
    public void showProgressBarCreate() {
        showProgressBar(false, false, getString(R.string.doing_mbr_create));
    }

    @Override
    public void onDeleteFriend() {
        tagViewAdapter.removeItem(0);
    }

    @Override
    public List<Object> getFriendsList() {
        return tagViewAdapter.getListData();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
                takePicture();
                break;
            case R.id.img_take_picture:
                takePicture();
                break;
            case R.id.edt_birthday:
                selectBirthday();
                break;
            case R.id.edt_link:
                shareAccount();
                break;
            case R.id.img_add:
                shareAccount();
                break;
            case R.id.btn_create:
                createMbr();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
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

                    setAvatar(fileObject);
                }
                break;
            case REQUEST_LINK_FRIEND:
                if (resultCode == RESULT_OK) {
                    RESP_Friends friends = null;

                    try {
                        friends = (RESP_Friends) data.getSerializableExtra(Constants.OBJECT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    insertLinkAccount(friends);
                }
                break;
        }
    }
}