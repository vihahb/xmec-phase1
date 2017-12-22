package com.xtelsolution.xmec.model.server;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.xtelsolution.sdk.callback.AbsICmd;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.xmec.model.entity.NewsPortsModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanhChung on 04/11/2017.
 */

public abstract class NewsPostCategoryModel extends AbsICmd {
    private static final String TAG = "NewsPostCategoryModel";
    private String url = "";

    public void setUrl(String url) {
        this.url = url;
    }

    private Context context;
    private ParcelHTML parcelHTML;

    public NewsPostCategoryModel(Context context) {
        this.context = context;
        parcelHTML = new ParcelHTML();
    }

    @Override
    protected void invoke() {
        if (url == null) return;
        Log.e(TAG, "invoke: " + url);
        parcelHTML.execute(url);

    }

    @Override
    protected void exception(String message) {

    }

    protected abstract void onSuccess(List<NewsPortsModel> newsList);

    protected abstract void onError(int errorCode);

    private class ParcelHTML {

        void execute(String url) {

            Ion.with(context).load(url).asString().withResponse().setCallback(new FutureCallback<Response<String>>() {
                @Override
                public void onCompleted(Exception e, Response<String> result) {
                    if (e == null && result.getResult() != null) {

                        List<NewsPortsModel> list = new ArrayList<>();
                        Document doc = Jsoup.parse(result.getResult());
                        Elements elements = doc.select(".post-list ul li");
                        for (Element element : elements) {
                            NewsPortsModel model = new NewsPortsModel();
                            model.setTitle(element.select(".post-content h4").text());
                            model.setUrl(element.select(".post-content h4 a").attr("href"));
                            model.setDescription(element.select(".post-content").text());
                            model.setImage(element.select("img").attr("src"));
                            list.add(model);
                            Log.e(TAG, "onCompleted: " + model.toString());

                        }
                        onSuccess(list);
                    } else {
                        onError(Constants.ERROR_UNKOW);
                        assert e != null;
                        e.printStackTrace();
                    }

                }
            });
        }

    }
}
