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
import com.xtelsolution.sdk.utils.PermissionHelper;
import com.xtelsolution.sdk.utils.TextUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.AddImageAdapter;
import com.xtelsolution.xmec.adapter.hr.SearchDrugAdapter;
import com.xtelsolution.xmec.model.entity.Drug;
import com.xtelsolution.xmec.model.entity.FileObject;
import com.xtelsolution.xmec.model.entity.RESP_Image;
import com.xtelsolution.xmec.model.entity.UserDrugImages;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.presenter.hr.UpdateDrugPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.ResizeImageActivity;
import com.xtelsolution.xmec.view.activity.detailhr.ViewImageActivity;
import com.xtelsolution.xmec.view.activity.inf.hr.IUpdateDrugView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateDrugActivity extends BasicActivity implements IUpdateDrugView {
    private UpdateDrugPresenter presenter;

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

    private UserDrugs userDrugs;
//    private REQ_UserDrugs reqUserDrugs;
    private Drug drug;

    private final int PERMISSION_CAMERA = 1;
    private final int REQUEST_IMAGE = 2;
    private final int REQUEST_RESIZE_IMAGE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_drug);
        ButterKnife.bind(this);

        presenter = new UpdateDrugPresenter(this);
        drug = new Drug();

        initDrugName();
        initRecyclerview();
        initActionListener();
        getData();
    }

    private void getData() {
        int id = getIntExtra(Constants.OBJECT);

        if (id != -1) {
            presenter.getData(id);
        } else {
            showErrorGetData(null);
        }
    }

    private void setData() {
        drug.setId(userDrugs.getDrugId());
        drug.setTenThuoc(userDrugs.getDrugName());

        edtName.setText(userDrugs.getDrugName());
        edtNote.setText(userDrugs.getNote());

        if (userDrugs.getUserDrugImages() != null)
            imageAdapter.setListDrugData(userDrugs.getUserDrugImages());
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
                if (!drug.getTenThuoc().equals(editable.toString())) {
                    drug.setId(-1);
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

                    ImagePicker.takePicture(UpdateDrugActivity.this, PERMISSION_CAMERA, REQUEST_IMAGE);
                } else {
                    RESP_Image respImage = new RESP_Image(imageAdapter.getListData());

                    Intent intent = new Intent(UpdateDrugActivity.this, ViewImageActivity.class);
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
        userDrugs.setDrugName(edtName.getText().toString().trim());
        userDrugs.setNote(edtNote.getText().toString().trim());
        userDrugs.setDrugId(drug.getId());

        presenter.updateDrug();
    }

    private void showConfirmAddImage(final boolean isDelete) {
        showMaterialDialog(false, false, null, getString(R.string.message_create_drug_error_image),
                getString(R.string.layout_disagree), getString(R.string.layout_agree), new DialogListener() {
                    @Override
                    public void negativeClicked() {
                        goBack();
                    }

                    @Override
                    public void positiveClicked() {
                        showProgressBar(1);

                        if (isDelete)
                            presenter.checkDeleteImage();
                        else
                            presenter.checkAddImage();
                    }
                });
    }

    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, userDrugs.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onGetDataSuccess(UserDrugs userDrugs) {
        if (userDrugs == null)
            showErrorGetData(null);
        else {
            this.userDrugs = userDrugs;
            setData();
        }
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
    public void onUpdateSuccess() {
        closeProgressBar();
        showLongToast(getString(R.string.success_update_drug));
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
                message = getString(R.string.doing_update_drug);
                break;
            default:
                message = getString(R.string.doing_do);
                break;
        }

        showProgressBar(false, false, message);
    }

    @Override
    public UserDrugs getUserDrugs() {
        return userDrugs;
    }

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
                showConfirmAddImage(true);
                break;
            case 303:
                showConfirmAddImage(false);
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
                ImagePicker.takePicture(UpdateDrugActivity.this, PERMISSION_CAMERA, REQUEST_IMAGE);
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
                        presenter.uploadImage(fileObject.getFile());
                    } else {
                        onError(Constants.ERROR_UNKOW);
                    }
                }
                break;
        }
    }
}