package com.xtelsolution.xmec.view.activity.mbr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.xtelsolution.sdk.callback.ConfirmListener;
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
import com.xtelsolution.xmec.model.entity.Friends;
import com.xtelsolution.xmec.model.entity.Mbr;
import com.xtelsolution.xmec.model.entity.ShareAccounts;
import com.xtelsolution.xmec.model.resp.RESP_Friends;
import com.xtelsolution.xmec.presenter.mbr.MbrUpdatePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.ResizeImageActivity;
import com.xtelsolution.xmec.view.activity.inf.mbr.IMbrUpdateView;
import com.xtelsolution.xmec.view.widget.RoundImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 */
public class MbrUpdateActivity extends BasicActivity implements View.OnClickListener, IMbrUpdateView {
    private MbrUpdatePresenter presenter;

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
    @BindView(R.id.btn_update)
    Button btnUpdate;

    private MenuItem menuItem;

    private TagViewAdapter tagViewAdapter;
    private MbrSpinnerAdapter relationshipAdapter;
    private File avatar;
    private Mbr mbr;

    private boolean isViewOnly = false;
    private final int PERMISSION_CAMERA = 1;
    private final int REQUEST_IMAGE = 2;
    private final int REQUEST_RESIZE_IMAGE = 3;
    private final int REQUEST_LINK_FRIEND = 4;

    private String[] permission_camera = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mbr_update);
        ButterKnife.bind(this);

        presenter = new MbrUpdatePresenter(this);

        initToolbar(R.id.toolbar, getString(R.string.title_activity_mbr_update));
        initRelationship();
        initLinkAccount();
        initGender();
        getData();
        initActionListener();
        KeyboardUtils.autoHideKeboard(MbrUpdateActivity.this, findViewById(R.id.rootView));
    }

    /**
     * Set Avatar default
     */
    private void initImageDefault() {
        if (mbr != null && mbr.getAvatar() != null || avatar != null)
            return;

        int resource = spinnerGender.getSelectedItemPosition() == 0 ? R.mipmap.ic_female_avatar : R.mipmap.ic_male_avatar;

        imgAvatar.setImageResource(resource);
        WidgetUtils.setImageFileBlur(imgAvatarBlur, imgAvatar, resource);
    }

    /**
     * Khởi tạo view chọn Giới tính
     */
    private void initGender() {
        String[] listGender = getResources().getStringArray(R.array.gender);
        final MbrSpinnerAdapter adapter = new MbrSpinnerAdapter();
        adapter.setList(listGender);

        final String[] male_re = getResources().getStringArray(R.array.male_relationship);
        final String[] female_re = getResources().getStringArray(R.array.female_relationship);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {
                    if (!Arrays.equals(relationshipAdapter.getList(), male_re)) {
                        relationshipAdapter.setList(male_re);
                    }
                } else {
                    if (!Arrays.equals(relationshipAdapter.getList(), female_re)) {
                        relationshipAdapter.setList(female_re);
                    }
                }

                if (mbr.getGender() - 1 == i) {
                    spinnerRelationship.setSelection(mbr.getRelationshipType());
                    edtRelationship.setText(relationshipAdapter.getItemContent(mbr.getRelationshipType()));
                } else {
                    spinnerRelationship.setSelection(0);
                }

                edtGender.setText(adapter.getItemContent(i));
                initImageDefault();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerGender.setAdapter(adapter);
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

    /**
     * Khởi tạo danh sách chia sẻ
     */
    private void initLinkAccount() {
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        tagViewAdapter = new TagViewAdapter();
        recyclerView.setAdapter(tagViewAdapter);

        tagViewAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (!canShare()) {
                    showLongToast(getString(R.string.message_not_perrmission_to_use));
                } else {
                    tagViewAdapter.removeItem(position);

                    if (tagViewAdapter.getItemCount() == 0) {
                        edtLink.setText(null);
                        lineLink.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void changeList(int itemCount) {
        if (itemCount > 0) {
            edtLink.setText(" ");
            lineLink.setVisibility(View.GONE);
        } else {
            edtLink.setText(null);
            lineLink.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Nhận dữ liệu truyền vào
     */
    private void getData() {
        int mbrID = -1;

        try {
            isViewOnly = getIntent().getBooleanExtra(Constants.OBJECT_2, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mbrID = getIntent().getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mbrID == -1) {
            showErrorGetData(null);
        }

        presenter.getMbr(mbrID);
    }

    /**
     * Load dữ liệu vào view
     */
    private void setData() {
        edtName.setText(mbr.getName());
        spinnerGender.setSelection((mbr.getGender() - 1));
        edtBirthday.setText(TimeUtils.convertLongToDate(mbr.getBirthdayLong()));

        setDefaultAvatar();

        for (int i = mbr.getShareAccounts().size() - 1; i >= 0; i--) {
            if (mbr.getShareAccounts().get(i).getShareType() != 2) {
                presenter.addShareAccount(mbr.getShareAccounts().get(i));
                mbr.getShareAccounts().remove(i);
            }
        }

        tagViewAdapter.setListShare(mbr.getShareAccounts());
        changeList(tagViewAdapter.getItemCount());
    }

    private void checkMbr() {
        if (!canEdit()) {
            edtName.setEnabled(false);
            edtBirthday.setEnabled(false);
            spinnerGender.setEnabled(false);
            spinnerRelationship.setEnabled(false);

            imgTakePicture.setVisibility(View.INVISIBLE);
            imgAdd.setVisibility(View.INVISIBLE);
            edtLink.setEnabled(false);
            tagViewAdapter.disableDelete();

            if (menuItem != null)
                menuItem.setVisible(false);

            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle("Chi tiết y bạ");

            btnUpdate.setText(getString(R.string.layout_alarm_brn_close));
        }

        if (!canDelete()) {
            if (menuItem != null)
                menuItem.setVisible(false);
        }
    }

    private boolean canEdit() {
        return !isViewOnly && (mbr == null || mbr.getShareType() == 0 && mbr.getShareTo() == 0 || mbr.getShareType() == 2 && mbr.getShareTo() == 2);
    }

    private boolean canDelete() {
        return !isViewOnly && (mbr == null || mbr.getShareType() == 0 && mbr.getShareTo() == 0);
    }

    private boolean canShare() {
        return mbr.getShareType() == 0 && mbr.getShareTo() == 0;
    }

    private void setDefaultAvatar() {
        final int resource = spinnerGender.getSelectedItemPosition() == 0 ? R.mipmap.ic_female_avatar : R.mipmap.ic_male_avatar;

        WidgetUtils.setImageURL(imgAvatar, mbr.getAvatar(), resource, new Callback() {
            @Override
            public void onSuccess() {
                WidgetUtils.setImageFileBlur(imgAvatarBlur, imgAvatar, resource);
            }

            @Override
            public void onError() {
                WidgetUtils.setImageFileBlur(imgAvatarBlur, imgAvatar, resource);
            }
        });
    }

    /**
     * Khởi tạo lắng nghe khi người dùng tương tác
     */
    private void initActionListener() {
        imgTakePicture.setOnClickListener(this);
        edtBirthday.setOnClickListener(this);
        edtLink.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
//        btnDelete.setOnClickListener(this);
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

        TimeUtils.datePicker(MbrUpdateActivity.this, birthday, true, new DatePickerListener() {
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
//        if (adapterLink.getItemCount() == 5) {
        if (tagViewAdapter.getItemCount() == 6) {
            showLongToast(getString(R.string.error_mbr_share_over_5));
            return;
        }

        startActivityForResult(FriendSearchActivity.class, Constants.OBJECT, tagViewAdapter.getItemCount(), REQUEST_LINK_FRIEND);
    }

    /**
     * Tạo y bạ
     */
    private void update() {
        String name = edtName.getText().toString().trim();
        int gender = spinnerGender.getSelectedItemPosition();
        String birthDay = edtBirthday.getText().toString();
        int relationshipType = spinnerRelationship.getSelectedItemPosition();

        mbr.setName(name);
        mbr.setGender((gender + 1));
        mbr.setBirthdayLong(TimeUtils.convertDateToLong(birthDay));
        mbr.setRelationshipType(relationshipType);

        presenter.updateMbr(avatar, mbr);
    }

    /**
     * Insert bạn bè vào danh sách liên kết
     */
    private void insertLinkAccount(RESP_Friends respFriends) {
//        if (respFriends == null) {
//            onError(Constants.ERROR_UNKOW);
//            return;
//        }
//
//        List<Tag> tags = tagGroup.getTags();
//        SparseBooleanArray array = new SparseBooleanArray();
//
//        for (int i = tags.size() - 1; i >= 0; i--) {
//            array.put(tags.get(i).getId(), true);
//        }
//
//        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.background_link_account);
//
//        for (Friends friends : respFriends.getData()) {
//            if (!array.get(friends.getUid())) {
//                ShareAccounts shareAccounts = new ShareAccounts(-1, friends, 2);
//                tags.add(new Tag(shareAccounts.getUid(), shareAccounts.getFullname(), drawable, shareAccounts));
//            }
//        }
//
//        tagGroup.addTags(tags);

        List<ShareAccounts> list = new ArrayList<>();

        for (Friends friends : respFriends.getData()) {
            ShareAccounts shareAccounts = new ShareAccounts(-1, friends, 2);
            list.add(shareAccounts);
        }

        tagViewAdapter.addListShare(list);

        if (tagViewAdapter.getItemCount() > 0) {
            edtLink.setText(" ");
            lineLink.setVisibility(View.GONE);
        }
    }

    /**
     * Thông báo liên kết tài khoản không tồn tại
     */
    private void onShareError() {
        showMaterialDialog(false, false, null, getString(R.string.message_ca_error_link_try_again), getString(R.string.layout_disagree), getString(R.string.layout_agree), new DialogListener() {
            @Override
            public void negativeClicked() {
                finish();
            }

            @Override
            public void positiveClicked() {
                showProgressBar(false, false, getString(R.string.doing_mbr_link));
                presenter.separateShare();
            }
        });
    }

    /**
     * Xác nhân người dùng muốn xóa y bạ
     */
    private void confirmDeleteMbr() {
        showConfirmDialog(null, getString(R.string.message_mbr_delete), new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                presenter.deleteMbr();
            }
        });
    }

    private void goBack(int type) {
        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, mbr.getMrbId());
        intent.putExtra(Constants.OBJECT_2, type);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onGetMbrSuccess(Mbr mbr) {
        if (mbr == null) {
            showErrorGetData(null);
            return;
        }

        this.mbr = mbr;
        setData();
        checkMbr();
    }

    /**
     * Tạo y bạ thành công
     */
    @Override
    public void onUpdateSuccess() {
        closeProgressBar();
        showLongToast(getString(R.string.success_mbr_update));
        goBack(1);
    }

    @Override
    public void onDeleteSuccess() {
        goBack(2);
    }

    @Override
    public void showProgressBar(int type) {
        String message;

        if (type == 1) {
            message = "Đang cập nhật y bạ";
        } else {
            message = getString(R.string.doing_mbr_delete);
        }

        showProgressBar(false, false, message);
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
    public Mbr getMbr() {
        return mbr;
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
                if (!canShare()) {
                    showLongToast(getString(R.string.message_not_perrmission_to_use));
                } else {
                    shareAccount();
                }
                break;
            case R.id.btn_update:
                if (!canEdit()) {
                    finish();
                } else {
                    update();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update_mbr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menuItem = menu.findItem(R.id.action_delete);
        FrameLayout rootView1 = (FrameLayout) menuItem.getActionView();
        TextView textView1 = rootView1.findViewById(R.id.txt_title);
        textView1.setText(getString(R.string.menu_mbr_delete));

        if (!canEdit() || !canDelete()) {
            menuItem.setVisible(false);
        }

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
            case R.id.action_delete:
                if (!canEdit()) {
                    showLongToast(getString(R.string.message_not_perrmission_to_use));
                } else {
                    confirmDeleteMbr();
                }
                break;
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