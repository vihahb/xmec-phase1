package com.xtelsolution.xmec.view.activity.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.sdk.utils.Utils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.CategoryListEntity;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailNewsActivity extends AppCompatActivity {


    @BindView(R.id.wvContent)
    WebView wvContent;
    @BindView(R.id.loadding)
    LinearLayout loadding;

    private static final String TAG = "DetailNewsActivity";

    public static void start(Context context, CategoryListEntity newsPortsModel/*, View expansionView*/) {
        Intent intent = new Intent(context, DetailNewsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.OBJECT, newsPortsModel);
        intent.putExtra(Constants.BUNDLE, bundle);
        context.startActivity(intent);
    }
//
//    public static void start(Context context, NewsPortsModel newsPortsModel/*, View expansionView*/) {
//        Intent intent = new Intent(context, DetailNewsActivity.class);
//
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(Constants.OBJECT, newsPortsModel);
//        intent.putExtra(Constants.BUNDLE, bundle);
//        context.startActivity(intent);
//    }

    //    private NewsPortsModel model;
    private CategoryListEntity model;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_detail_news);
        ButterKnife.bind(this);

//        model = (NewsPortsModel) getIntent().getBundleExtra(Constants.BUNDLE).getSerializable(Constants.OBJECT);
        model = (CategoryListEntity) getIntent().getBundleExtra(Constants.BUNDLE).getSerializable(Constants.OBJECT);

        Log.e(TAG, "onCreate: " + model.toString());
        if (NetworkUtils.isConnected(getApplicationContext())) {

            getData(model);
        } else {

        }

    }

    private void getData(/*NewsPortsModel*/CategoryListEntity model) {
        Log.e(TAG, "getData: " + model);
        Ion.with(this).load(model.getLink()).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> result) {
                if (e == null && result.getResult() != null) {
                    try {


                        JSONObject object = new JSONObject(result.getResult());

                        Log.e(TAG, "onCompleted: " + result.getResult());
                        StringBuilder sb = new StringBuilder();
                        sb.append("<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
                        sb.append(object.getString("content").replaceAll("<[aA].*?>", "<u>").replaceAll("</[aA]>", "</u>"));
                        sb.append("</body></HTML>");
                        wvContent.loadDataWithBaseURL("file:///android_asset/", sb.toString(), "text/html", "utf-8", null);
//                    wvContent.loadData(element.outerHtml(), "text/html", "utf-8");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadding.setVisibility(View.GONE);
                            }
                        }, 2000);

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                } else {
                    if (e != null)
                        e.printStackTrace();
                    Utils.showToast("Rất tiếc. Trang bạn yêu cầu ko tồn tại.");
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
