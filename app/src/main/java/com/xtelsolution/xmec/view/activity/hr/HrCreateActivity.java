package com.xtelsolution.xmec.view.activity.hr;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.DatePickerListener;
import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.ImagePicker;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.sdk.utils.TimeUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.AddImageAdapter;
import com.xtelsolution.xmec.adapter.TagViewAdapter;
import com.xtelsolution.xmec.adapter.hr.SearchHospitalAdapter;
import com.xtelsolution.xmec.model.entity.FileObject;
import com.xtelsolution.xmec.model.entity.HealthRecordImages;
import com.xtelsolution.xmec.model.entity.Hospital;
import com.xtelsolution.xmec.model.entity.RESP_Image;
import com.xtelsolution.xmec.model.entity.UserDiseases;
import com.xtelsolution.xmec.model.resp.RESP_User_Diseases;
import com.xtelsolution.xmec.presenter.hr.HrCreatePresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.ResizeImageActivity;
import com.xtelsolution.xmec.view.activity.detailhr.ViewImageActivity;
import com.xtelsolution.xmec.view.activity.inf.hr.IHrCreateView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HrCreateActivity extends BasicActivity implements View.OnClickListener, IHrCreateView {
    private HrCreatePresenter presenter;

    @BindView(R.id.edt_hr_name)
    TextInputEditText edtHrName;
    @BindView(R.id.edt_disease)
    TextInputEditText edtDisease;
    @BindView(R.id.layout_1)
    TextInputLayout layout1;
    @BindView(R.id.img_add)
    ImageButton imgAdd;
    @BindView(R.id.recyclerview_disease)
    RecyclerView recyclerViewDisease;
    @BindView(R.id.edt_start_date)
    TextInputEditText edtStartDate;
    @BindView(R.id.txt_note)
    TextView txtNote;
    @BindView(R.id.edt_note)
    TextInputEditText edtNote;
    @BindView(R.id.edt_hospital)
    AppCompatAutoCompleteTextView edtHospital;
    @BindView(R.id.edt_doctor)
    TextInputEditText edtDoctor;
    @BindView(R.id.layout_morre)
    LinearLayout layoutMore;
    @BindView(R.id.img_expand_state)
    ImageView imgExpandState;
    @BindView(R.id.txt_expand_state)
    TextView txtExpandState;
    @BindView(R.id.recyclerview_image)
    RecyclerView recyclerviewImage;
    @BindView(R.id.expandable_layout)
    ExpandableLayout expandableLayout;

    private TagViewAdapter tagViewAdapter;
    private AddImageAdapter adapterImage;
    private SearchHospitalAdapter adapterHospital;

    private int mbrID;
    private Hospital hospital;

    private final int PERMISSION_CAMERA = 1;
    private final int REQUEST_DISEASE = 2;
    private final int REQUEST_IMAGE = 3;
    private final int REQUEST_RESIZE_IMAGE = 4;
    private final int REQUEST_ADD_DRUG = 5;

    private String[] permission_camera = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_create);
        ButterKnife.bind(this);

        initToolbar(R.id.toolbar, getString(R.string.title_activity_hr_create));

        initDefaultData();
        getData();
        initDisease();
        initImages();
        initHospital();
        initActionListerner();
        KeyboardUtils.autoHideKeboard(this, findViewById(R.id.rootView));
    }

    private void initDefaultData() {
        presenter = new HrCreatePresenter(this);
        edtStartDate.setText(TimeUtils.getToday());
    }

    private void getData() {
        try {
            mbrID = getIntent().getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (mbrID == -1) {
            showErrorGetData(null);
        }
    }

    /**
     * Khởi tạo danh sách hiển thị bệnh đã chọn
     */
    private void initDisease() {
        recyclerViewDisease.setHasFixedSize(false);
        recyclerViewDisease.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        tagViewAdapter = new TagViewAdapter();
        recyclerViewDisease.setAdapter(tagViewAdapter);

        tagViewAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                tagViewAdapter.removeItem(position);

                if (tagViewAdapter.getItemCount() == 0) {
                    edtDisease.setText(null);
                }
            }
        });
    }

    /**
     * Khởi tạo view nhập Cơ sở y tes
     */
    private void initHospital() {
        adapterHospital = new SearchHospitalAdapter(getApplicationContext(), 0);
        edtHospital.setAdapter(adapterHospital);

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (hospital != null) {
                    if (!hospital.getName().equals(editable.toString())) {
                        hospital.setId(-1);
                    }
                }

                if (editable.toString().length() == 0)
                    adapterHospital.clearData();
                else
                    presenter.searchHospital(editable.toString());
            }
        };

        edtHospital.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtHospital.removeTextChangedListener(textWatcher);

                hospital = adapterHospital.getItem(i);

                edtHospital.addTextChangedListener(textWatcher);
            }
        });

        edtHospital.addTextChangedListener(textWatcher);
    }

    /**
     * Khởi tạo danh sách ảnh
     */
    private void initImages() {
        recyclerviewImage.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        adapterImage = new AddImageAdapter();
        recyclerviewImage.setAdapter(adapterImage);

        adapterImage.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (position == -1 && object == null) {
                    takePicture();
                } else {
                    RESP_Image respImage = new RESP_Image(adapterImage.getListData());

                    Intent intent = new Intent(HrCreateActivity.this, ViewImageActivity.class);
                    intent.putExtra(Constants.OBJECT, respImage);
                    intent.putExtra(Constants.OBJECT_2, position);

                    startActivity(intent);
                }
            }
        });
    }

    /**
     * Khởi tạo sự kiện lắng nghe người dùng tương tác
     */
    private void initActionListerner() {
        edtDisease.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        edtStartDate.setOnClickListener(this);
        layoutMore.setOnClickListener(this);
    }

    /**
     * Chọn ngày bắt đầu
     */
    private void setStartDate() {
        TimeUtils.datePicker(HrCreateActivity.this, edtStartDate.getText().toString(), false, new DatePickerListener() {
            @Override
            public void onTimeSet(String date, long miliseconds) {
                edtStartDate.setText(date);
            }
        });
    }

    /**
     * Mở rộng hoặc thu gọn layout thêm ảnh
     */
    private void showAndHideExpanlayout() {
        if (expandableLayout.isExpanded()) {
            imgExpandState.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
            txtExpandState.setText(getString(R.string.layout_hr_create_expand));
            expandableLayout.collapse();
        } else {
            imgExpandState.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
            txtExpandState.setText(getString(R.string.layout_hr_create_collapse));
            expandableLayout.expand();
        }
    }

    /**
     * Kiểm tra dữ liệu để chuyển sang màn thêm ảnh
     */
    private void createHr() {
        String hr_name = edtHrName.getText().toString().trim();
        String startDate = edtStartDate.getText().toString();
        String note = edtNote.getText().toString();
        String doctorName = edtDoctor.getText().toString();

        if (hospital == null)
            hospital = new Hospital();

        hospital.setName(edtHospital.getText().toString());

        presenter.continueHr(mbrID, hr_name, startDate, note, doctorName, hospital);
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
     * Lấy danh sách bệnh đã chọn
     */
    private void getSelectDisease(Intent data) {
        RESP_User_Diseases respUserDiseases = null;

        try {
            respUserDiseases = (RESP_User_Diseases) data.getSerializableExtra(Constants.OBJECT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (respUserDiseases != null) {
            HashMap<String, Boolean> hashMap = new HashMap<>();

            for (Object item : tagViewAdapter.getListData()) {
                hashMap.put(((UserDiseases) item).getName(), true);
            }

            for (UserDiseases userDiseases : respUserDiseases.getData()) {
                Boolean isExists = hashMap.get(userDiseases.getName());

                if (isExists == null || !isExists)
                    tagViewAdapter.addItem(userDiseases);
            }

            if (tagViewAdapter.getItemCount() > 0) {
                edtDisease.setText(" ");
            }
        }
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

    private void goToAddDrug() {
        startActivityForResult(AddDrugActivity.class, Constants.OBJECT, presenter.getHealthRecords().getId(), REQUEST_ADD_DRUG);
    }

    private void showErrorDisease() {
        showMaterialDialog(false, false, null, getString(R.string.message_hr_create_error_diseae), getString(R.string.layout_disagree), getString(R.string.layout_agree), new DialogListener() {
            @Override
            public void negativeClicked() {
                goToAddDrug();
            }

            @Override
            public void positiveClicked() {
                showProgressBar(3);
                presenter.checkAddDisease();
            }
        });
    }

    private void showErrorImage() {
        showMaterialDialog(false, false, null, getString(R.string.message_hr_create_error_image), getString(R.string.layout_disagree), getString(R.string.layout_agree), new DialogListener() {
            @Override
            public void negativeClicked() {
                goToAddDrug();
            }

            @Override
            public void positiveClicked() {
                showProgressBar(4);
                presenter.checkAddImage();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_disease:
                startActivityForResult(SearchDiseaseActivity.class, REQUEST_DISEASE);
                break;
            case R.id.img_add:
                startActivityForResult(SearchDiseaseActivity.class, REQUEST_DISEASE);
                break;
            case R.id.edt_start_date:
                setStartDate();
                break;
            case R.id.layout_morre:
                showAndHideExpanlayout();
                break;
        }
    }

    @Override
    public void onUploadSuccess(HealthRecordImages healthRecordImages) {
        closeProgressBar();

        adapterImage.addItem(healthRecordImages);
    }

    @Override
    public void onSearchHospitalSuccess(List<Hospital> hospitals, String key) {
        String oldKey = TextUtils.unicodeToKoDau(edtHospital.getText().toString());
        if (oldKey.replaceAll(" ", "%20").equals(key)) {
            List<Hospital> list = new ArrayList<>();

            if (hospitals.size() > 5) {
                for (int i = 0; i < 5; i++) {
                    list.add(hospitals.get(i));
                }
            } else
                list.addAll(hospitals);

            adapterHospital.setListData(list);
            adapterHospital.getFilter().filter(edtHospital.getText().toString());
        }
    }

    @Override
    public void onCreateHrSuccess() {
        closeProgressBar();
        goToAddDrug();
    }

    @Override
    public void showProgressBar(int type) {
        String message;

        switch (type) {
            case 1:
                message = getString(R.string.doing_upload_image);
                break;
            case 2:
                message = getString(R.string.doing_hr_create);
                break;
            case 3:
                message = getString(R.string.doing_hr_create_disease);
                break;
            case 4:
                message = getString(R.string.doing_hr_create_image);
                break;
            default:
                message = getString(R.string.doing_do);
                break;
        }

        showProgressBar(false, false, message);
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 301:
                edtHrName.requestFocus();
                edtHrName.setError(getString(R.string.error_sr_search_name));
                break;
            case 302:
                edtHrName.requestFocus();
                edtHrName.setError("Tên sổ khám không được chứa ký tự đặc biệt");
//                showLongToast("Tên sổ khám không được chứa ký tự đặc biệt");
                break;
            case 303:
                edtStartDate.requestFocus();
                edtStartDate.setError(getString(R.string.error_sr_search_start_date));
//                showLongToast(getString(R.string.error_sr_search_start_date));
                break;
            case 304:
                showErrorDisease();
                break;
            case 305:
                showErrorImage();
                break;
        }
    }

    @Override
    public List<Object> getDiseaseList() {
        return tagViewAdapter.getListData();
    }

    @Override
    public List<Object> getImageList() {
        return adapterImage.getListData();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_hr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final MenuItem menuItem = menu.findItem(R.id.action_continue);
        FrameLayout rootView1 = (FrameLayout) menuItem.getActionView();
        TextView textView1 = rootView1.findViewById(R.id.txt_title);
        textView1.setText(getString(R.string.menu_schedule_continue));

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
            case R.id.action_continue:
                createHr();
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
            case REQUEST_DISEASE:
                if (resultCode == RESULT_OK) {
                    getSelectDisease(data);
                }
                break;
            case REQUEST_IMAGE:
                if (resultCode == RESULT_OK) {
                    File file = ImagePicker.getFileFromResult(this, data);

                    if (file != null) {
                        resizeImage(file);
                    } else {
                        onError(Constants.ERROR_UNKOW);
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

                    if (fileObject != null) {
                        presenter.updateImage(fileObject.getFile());
                    } else {
                        onError(Constants.ERROR_UNKOW);
                    }
                }
                break;
            case REQUEST_ADD_DRUG:
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    finish();
                }
                break;
        }
    }
}