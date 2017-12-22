package com.xtelsolution.xmec.presenter.drug;

import android.os.AsyncTask;
import android.util.Log;

import com.xtelsolution.sdk.utils.NetworkUtils;
import com.xtelsolution.xmec.model.entity.InfoDrug;
import com.xtelsolution.xmec.model.resp.RESP_DrugInfo;
import com.xtelsolution.xmec.model.server.GetDrugById;
import com.xtelsolution.xmec.presenter.BasicPresenter;
import com.xtelsolution.xmec.view.activity.drug.IDrugInfoView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xtel on 11/17/17.
 */

public class DrugDetailPresenter extends BasicPresenter {

    private IDrugInfoView view;

    public DrugDetailPresenter(IDrugInfoView basicView) {
        super(basicView);
        view = basicView;
    }

    private void iCmd(Object... params) {
        switch ((int) params[0]) {
            case 1:
                int id = (int) params[1];
                new GetDrugById(id) {
                    @Override
                    protected void onSuccess(RESP_DrugInfo drugInfo) {
                        List<InfoDrug> infoDrugs = new ArrayList<>();
                        if (drugInfo.getDrugSummary()!=null) {
                            infoDrugs.add(new InfoDrug("Tóm tắt thuốc", drugInfo.getDrugSummary()));
                        }
                        if (drugInfo.getDrugUsage()!=null) {
                            infoDrugs.add(new InfoDrug("Hướng dẫn sử dụng", drugInfo.getDrugUsage()));
                        }
                        if (drugInfo.getDrugWarn()!=null) {
                            infoDrugs.add(new InfoDrug("Cảnh báo", drugInfo.getDrugWarn()));
                        }
                        if (drugInfo.getDrugPoint()!=null) {
                            infoDrugs.add(new InfoDrug("Chỉ định", drugInfo.getDrugPoint()));
                        }
                        if (drugInfo.getDrugUnpoint()!=null) {
                            infoDrugs.add(new InfoDrug("Chống chỉ định", drugInfo.getDrugUnpoint().replace("<li>", "").replace("</li>", "")));
                        }
                        if (drugInfo.getDrugSideEffect()!=null) {
                            infoDrugs.add(new InfoDrug("Tác dụng phụ", drugInfo.getDrugSideEffect().replaceAll("\t", "").replaceAll("<li>", "").replaceAll("</li>", "")));
                        }
                        if (drugInfo.getDrugNote()!=null) {
                            infoDrugs.add(new InfoDrug("Lưu ý", drugInfo.getDrugNote()));
                        }
                        if (drugInfo.getDrugOverdose()!=null) {
                            infoDrugs.add(new InfoDrug("Quá liều", drugInfo.getDrugOverdose()));
                        }
                        if (drugInfo.getDrugForget()!=null) {
                            infoDrugs.add(new InfoDrug("Nếu quên uống thuốc", drugInfo.getDrugForget()));
                        }
                        if (drugInfo.getDrugDiet()!=null) {
                            infoDrugs.add(new InfoDrug("Chế độ ăn uống", drugInfo.getDrugDiet()));
                        }
                        if (drugInfo.getDrugInteractive()!=null) {
                            infoDrugs.add(new InfoDrug("Tương tác", drugInfo.getDrugInteractive()));
                        }
                        if (drugInfo.getDrugMechanism()!=null) {
                            infoDrugs.add(new InfoDrug("Dược lý cơ chế", drugInfo.getDrugMechanism()));
                        }
                        if (drugInfo.getDrugKinetics()!=null) {
                            infoDrugs.add(new InfoDrug("Dược động học", drugInfo.getDrugKinetics()));
                        }
                        Log.e("getDrugSuccess", "onSuccess: " + infoDrugs.toString());
                        view.getDrugSuccess(infoDrugs);
                    }
                    @Override
                    protected void onErrror(int code) {
                        view.showWarning("Có lỗi xảy ra.\nVui lòng thử lại!");
                        showError(code);
                    }
                };
                break;
        }
    }

    public void getContent(String url) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.showWarning("Vui lòng kiểm tra kết nối mạng!\n Chạm để thử lại?");
            return;
        }
//        iCmd(1, url);
    }

    public void getDrugById(int drug_id) {
        if (!NetworkUtils.isConnected(view.getActivity())) {
            view.showWarning("Vui lòng kiểm tra kết nối mạng!\n Chạm để thử lại?");
            return;
        }
        view.showLoading();
        iCmd(1, drug_id);
    }

    @Override
    protected void getSessionSuccess(Object... params) {

    }

    private class ParseUrl extends AsyncTask<String, Void, List<InfoDrug>> {


        private static final String TAG = "ParseUrl";

        @Override
        protected List<InfoDrug> doInBackground(String... strings) {
            List<InfoDrug> list = new ArrayList<>();
            try {
                //Get document Page HTML
                Document document = Jsoup.connect(strings[0]).get();

                //Get Document Secsion
                Elements summary = document.select(".drug-body section");
                for (Element element : summary) {
                    InfoDrug infoDrug = new InfoDrug();
                    infoDrug.setTitle(element.select("h2 span").html().replaceAll("h2>", "h3>"));
                    infoDrug.setContent(element.select(".body").html());
                    Log.e(TAG, "doInBackground: " + element.select(".body").html());
                    if (element.getElementById("hinh-anh-thuoc") != null) continue;
                    list.add(infoDrug);

                }

//                String summary_detail = "";
//                if (summary != null && summary.size() > 0){
//                    for (Element elements : summary){
//                        Element title_drug_info = elements.getElementsByTag("h4").first();
//
//                        if (summary_detail.equals("")){
//                            summary_detail = title_drug_info + " " + drug_info_content;
//                        } else {
//                            summary_detail = summary_detail + "\n\n" + title_drug_info + " " + drug_info_content;
//                        }
//                        Log.e(TAG, "summary_detail in for: " + summary_detail);
//                    }
//                }
                Log.e(TAG, "summary: " + summary);
//                Log.e(TAG, "summary_detail: " + summary_detail);
                Log.e(TAG, "doInBackground: " + list.toString());

            } catch (Exception e) {
                Log.e(TAG, "doInBackground exception: " + e.toString());
            }

            return list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show dialog
        }

        @Override
        protected void onPostExecute(List<InfoDrug> jsoupDrugDetail) {
            super.onPostExecute(jsoupDrugDetail);
            //Hide progress dialog
//            tv_content_description.setText(Html.fromHtml(jsoupDrugDetail.getSummary()));
            view.getCollectionSucces(jsoupDrugDetail);
        }
    }
}
