package com.xtelsolution.xmec.view.activity.hr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xtelsolution.sdk.callback.ConfirmListener;
import com.xtelsolution.sdk.callback.HealthRecordOptionListener;
import com.xtelsolution.sdk.callback.ItemClickListener;
import com.xtelsolution.sdk.callback.NumberChangeListener;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.DialogUtils;
import com.xtelsolution.sdk.utils.SizeUtils;
import com.xtelsolution.sdk.utils.WidgetUtils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.adapter.hr.DrugAdapter;
import com.xtelsolution.xmec.model.entity.UserDrugs;
import com.xtelsolution.xmec.model.req.REQ_UserDrugs;
import com.xtelsolution.xmec.presenter.hr.AddDrugPresenter;
import com.xtelsolution.xmec.view.activity.BasicActivity;
import com.xtelsolution.xmec.view.activity.inf.hr.IAddDrugView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDrugActivity extends BasicActivity implements IAddDrugView {
    private AddDrugPresenter presenter;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private DrugAdapter adapter;

    private int hrID;

    private MenuItem menuDone;
    private MenuItem menuSkip;

    private final int REQUEST_ADD = 1;
    private final int REQUEST_UPDATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_drug);
        ButterKnife.bind(this);

        presenter = new AddDrugPresenter(this);

        initToolbar(R.id.toolbar, getString(R.string.title_activity_hr_add_drug));

        getData();
        initDrugs();
        initActionListener();
    }

    private void getData() {
        hrID = getIntExtra(Constants.OBJECT);

        if (hrID == -1) {
            showErrorGetData(null);
        }
    }

    /**
     * Khởi tạo view hiển thị danh sách thuốc
     */
    private void initDrugs() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new DrugAdapter(0);
        recyclerview.setAdapter(adapter);

        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int position, Object object) {
                if (position == -1) {
                    showUserDrugOption((UserDrugs) object);
                } else {
                    UserDrugs userDrugs = (UserDrugs) object;
                    startActivity(DetailDrugActivity.class, Constants.OBJECT, userDrugs.getId());
                }
            }
        });

        adapter.setNumberChangeListener(new NumberChangeListener() {
            @Override
            public void onChange(int itemCount) {
                boolean isDone = itemCount > 0;

                if (menuDone != null)
                    menuDone.setVisible(isDone);

                if (menuSkip != null)
                    menuSkip.setVisible(!isDone);
            }
        });
    }

    private void showUserDrugOption(final UserDrugs userDrugs) {
        DialogUtils.showUserDrugOption(AddDrugActivity.this, new HealthRecordOptionListener() {
            @Override
            public void onClicked(int type) {
                switch (type) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:
                        REQ_UserDrugs req_userDrugs = new REQ_UserDrugs(adapter.getListData());
                        req_userDrugs.getData().remove(userDrugs);

                        Intent intent = new Intent(AddDrugActivity.this, UpdateDrugActivity.class);
                        intent.putExtra(Constants.OBJECT, userDrugs.getId());

                        startActivityForResult(intent, REQUEST_UPDATE);
                        break;
                    case 4:
                        showConfirmDelete(userDrugs);
                        break;
                }
            }
        });
    }

    private void showConfirmDelete(final UserDrugs userDrugs) {
        DialogUtils.showConfirmDeleteDrug(AddDrugActivity.this, userDrugs.getDrugName(), new ConfirmListener() {
            @Override
            public void onConfirmSuccess() {
                presenter.deleteDrug(userDrugs);
            }
        });
    }

    private void addCreatedDrug(Intent data) {
        int id = getIntData(Constants.OBJECT, data);

        if (id != -1) {
            presenter.getUserDrug(id, 1);
        }
    }

    private void updateCreatedDrug(Intent data) {
        int id = getIntData(Constants.OBJECT, data);

        if (id != -1) {
            presenter.getUserDrug(id, 2);
        }
    }

    /**
     * Lắng nghe sự kiện khi người dùng tương tác
     */
    private void initActionListener() {
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    WidgetUtils.slideView(fabAdd, fabAdd.getHeight() + SizeUtils.dpToPx(getApplicationContext(), 16));
                } else if (dy < 0) {
                    WidgetUtils.slideView(fabAdd, 0);
                }
            }
        });

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddDrugActivity.this, CreateDrugActivity.class);
                intent.putExtra(Constants.OBJECT, hrID);

                startActivityForResult(intent, REQUEST_ADD);
            }
        });
    }

    private void goBack() {
        Intent intent = new Intent();
        intent.putExtra(Constants.OBJECT, hrID);

        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onDeleteSuccess(UserDrugs userDrugs) {
        closeProgressBar();
        showLongToast(getString(R.string.success_add_drug_delete_drug));
        adapter.deleteItem(userDrugs);
    }

    @Override
    public void onGetDrugScheduleSuccess(UserDrugs userDrugs, int type) {
        if (type == 1)
            adapter.addItem(userDrugs);
        else
            adapter.updateItem(userDrugs);
    }

    @Override
    public void onAddSuccess() {
        goBack();
    }

    @Override
    public void showProgressBar(int type) {
        String message;

        switch (type) {
            case 1:
                message = getString(R.string.doing_add_drug_delete_drug);
                break;
            default:
                message = getString(R.string.doing_do);
                break;
        }

        showProgressBar(false, false, message);
    }

    @Override
    public List<UserDrugs> getListUserDrugs() {
        return adapter.getListData();
    }

    @Override
    public void onError(int code) {
        super.onError(code);

        switch (code) {
            case 301:
                showLongToast(getString(R.string.error_add_drug_select_drug));
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hr_add_drug, menu);

        menuDone = menu.findItem(R.id.action_done);
        menuSkip = menu.findItem(R.id.action_skip);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        final MenuItem menuDone = menu.findItem(R.id.action_done);
        FrameLayout rootView1 = (FrameLayout) menuDone.getActionView();
        TextView textView1 = rootView1.findViewById(R.id.txt_title);
        textView1.setText(getString(R.string.menu_hr_add_drug_done));

        rootView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuDone);
            }
        });

        final MenuItem menuSkip = menu.findItem(R.id.action_skip);
        FrameLayout rootView2 = (FrameLayout) menuSkip.getActionView();
        TextView textView2 = rootView2.findViewById(R.id.txt_title);
        textView2.setText(getString(R.string.menu_hr_add_drug_skip));

        rootView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuSkip);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                break;
            case R.id.action_done:
                goBack();
                break;
            case R.id.action_skip:
                goBack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_ADD:
                if (resultCode == RESULT_OK) {
                    addCreatedDrug(data);
                }
                break;
            case REQUEST_UPDATE:
                if (resultCode == RESULT_OK) {
                    updateCreatedDrug(data);
                }
                break;
        }
    }
}