package com.xtelsolution.xmec.view.activity.hr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.xtelsolution.sdk.callback.DialogListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.ImagePicker;
import com.xtelsolution.sdk.utils.KeyboardUtils;
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.AddImageAdapter;
import com.xtelsolution.xmec.adapter.hr.SearchDrugAdapter;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.FileObject;
import com.xtelsolution.xmec.model.entity.RESP_Image;
import com.xtelsolution.xmec.model.entity.UserDrugImages;
import com.xtelsolution.xmec.presenter.hr.CreateDrugPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.ResizeImageActivity;
import com.xtelsolution.xmec.view.activity.detailhr.ViewImageActivity;
import com.xtelsolution.xmec.view.activity.inf.hr.ICreateDrugView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateDrugActivity extends BasicActivity implements ICreateDrugView {
    private CreateDrugPresenter presenter;

    @BindView(R.id.edt_name)
    AppCompatAutoCompleteTextView edtName;
    @BindView(R.id.edt_note)
    TextInputEditText edtNote;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private AddImageAdapter imageAdapter;
    private SearchDrugAdapter drugAdapter;

    private int hrId = -1;
    private Drug drug;

    private final int PERMISSION_CAMERA = 1;
    private final int REQUEST_IMAGE = 2;
    private final int REQUEST_RESIZE_IMAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_drug);
        ButterKnife.bind(this);

        presenter = new CreateDrugPresenter(this);

        getData();
        initDrugName();
        initRecyclerview();
        initActionListener();
        KeyboardUtils.autoHideKeboard(this, findViewById(R.id.rootView));
    }

    private void getData() {
        try {
            hrId = getIntent().getIntExtra(Constants.OBJECT, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (hrId == -1) {
            showErrorGetData(null);
        }

        presenter.getUserDrugs(hrId);
    }

    private void initDrugName() {
        drugAdapter = new SearchDrugAdapter(getApplicationContext(), 0);
        edtName.setAdapter(drugAdapter);

        final TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (drug != null) {
                    if (!drug.getTenThuoc().equals(editable.toString())) {
                        drug.setId(-1);
                    }
                }

                if (editable.toString().length() == 0)
                    drugAdapter.clearData();
                else
                    presenter.searchDrug(editable.toString());
            }
        };

        edtName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edtName.removeTextChangedListener(textWatcher);

                drug = drugAdapter.getItem(i);

                edtName.addTextChangedListener(textWatcher);
            }
        });

        edtName.addTextChangedListener(textWatcher);
    }

    /**
     * Khởi tạo view hiển thị danh sách ảnh
     */
    private void initRecyclerview() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new AddImageAdapter();
        recyclerview.setAdapter(imageAdapter);

        imageAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (position == -1) {
                    if (imageAdapter.getItemCount() == 4) {
                        showLongToast(getString(R.string.error_create_drug_max_image));
                        return;
                    }

                    ImagePicker.takePicture(CreateDrugActivity.this, PERMISSION_CAMERA, REQUEST_IMAGE);
                } else {
                    RESP_Image respImage = new RESP_Image(imageAdapter.getListData());

                    Intent intent = new Intent(CreateDrugActivity.this, ViewImageActivity.class);
                    intent.putExtra(Constants.OBJECT, respImage);
                    intent.putExtra(Constants.OBJECT_2, position);

                    startActivity(intent);
                }
            }
        });
    }

    private void initActionListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDrug();
            }
        });
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

    private void addDrug() {
        if (drug == null)
            drug = new Drug();

        drug.setTenThuoc(edtName.getText().toString().trim());
        String note = edtNote.getText().toString().trim();

        presenter.createDrug(hrId, drug, note);
    }

    private void showConfirmAddImage(final int code) {
        showMaterialDialog(false, false, null, getString(R.string.message_create_drug_error_image),
                getString(R.string.layout_disagree), getString(R.string.layout_agree), new DialogListener() {
                    @Override
                    public void negativeClicked() {
                        goBack();
                    }

                    @Override
                    public void positiveClicked() {
                        showProgressBar(1);

                        if (code == 302)
                            presenter.checkUploadImage();
                        else
                            presenter.checkAddImage();
                    }
                });
    }

    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, presenter.getUserDrugsID());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSearchSuccess(List<Drug> drugs, String key) {
        String currentKey = TextUtils.unicodeToKoDauLowerCase(edtName.getText().toString());
        currentKey = currentKey.replace(" ", "%20");

        if (currentKey.equals(key)) {
            List<Drug> list = new ArrayList<>();

            if (drugs.size() > 5) {
                for (int i = 0; i < 5; i++) {
                    list.add(drugs.get(i));
                }
            } else
                list.addAll(drugs);

            drugAdapter.setListData(list);
            drugAdapter.getFilter().filter(edtName.getText().toString());
        }
    }

    @Override
    public void onUploadSuccess(UserDrugImages userDrugImages) {
        closeProgressBar();
        imageAdapter.addItem(userDrugImages);
    }

    @Override
    public void onAddSuccess() {
        showLongToast(getString(R.string.success_create_drug));
        goBack();
    }

    @Override
    public void showProgressBar(int type) {
        String message;

        switch (type) {
            case 1:
                message = getString(R.string.doing_upload_image);
                break;
            case 2:
                message = getString(R.string.doing_create_drug);
                break;
            default:
                message = getString(R.string.doing_do);
                break;
        }

        showProgressBar(false, false, message);
    }

    @Override
    public int getHrID() {
        return hrId;
    }

//    @Override
//    public List<UserDrugs> getUserDrugs() {
//        if (reqUserDrugs == null)
//            return new ArrayList<>();
//        else
//            return reqUserDrugs.getInt();
//    }

    @Override
    public List<Object> getListImage() {
        return imageAdapter.getListData();
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 301:
                edtName.requestFocus();
                edtName.setError(getString(R.string.error_create_drug_name));
                break;
            case 302:
                showConfirmAddImage(302);
                break;
            case 303:
                showConfirmAddImage(303);
                break;
            case 304:
                drugAdapter.clearData();
                break;
            case 305:
                edtName.requestFocus();
                edtName.setError("Thuốc đã được thêm");
                break;
        }
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
                ImagePicker.takePicture(CreateDrugActivity.this, PERMISSION_CAMERA, REQUEST_IMAGE);
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
                        UserDrugImages userDrugImages = new UserDrugImages();
                        userDrugImages.setImagePath(fileObject.getFile().getPath());
                        imageAdapter.addItem(userDrugImages);
//                        presenter.uploadImage(fileObject.getFile());
                    } else {
                        onError(Constants.ERROR_UNKOW);
                    }
                }
                break;
        }
    }
}