package com.xtelsolution.sdk.utils;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.HospitalTypeModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThanhChung on 10/11/2017.
 */

public class MapUtil {

    private static final String TAG = "MapUtil";
    private static LatLng latLngOld;
    private static ValueAnimator vAnimator;
    private static Circle circle;

    public static Bitmap resizeMapIcons(String iconName, int width, int height, Context context) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(iconName, "mipmap", context.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public static Bitmap resizeMapIconsDrawable(String iconName, int width, int height, Context context) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    public static void addAnimationCricle(GoogleMap map, LatLng latLng) {
        if (latLngOld != null && latLngOld.longitude == latLng.longitude && latLng.latitude == latLngOld.latitude) {
            return;
        }
        latLngOld = latLng;
        if (vAnimator != null) vAnimator.cancel();
        if (circle != null) {
            circle.remove();
            circle = null;
        }

        circle = map.addCircle(new CircleOptions().center(latLng)
                .strokeColor(Color.RED).radius(100));

        vAnimator = new ValueAnimator();
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.RESTART);  /* PULSE */
        vAnimator.setIntValues(0, 100);
        vAnimator.setDuration(1000);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                // Log.e("", "" + animatedFraction);
                circle.setRadius(animatedFraction * 100);
            }
        });
        vAnimator.start();
    }

    public static void removerAmination() {
        if (vAnimator != null) vAnimator.cancel();
        if (circle != null) {
            circle.remove();
            circle = null;
        }

    }

    public static BitmapDescriptor getIconMarkerHospital(String type) {
        return BitmapDescriptorFactory.fromResource(getIconMipmapHospital(type));

    }

    public static int getIconMipmapHospital(String type) {
        if (list == null) getListHospitalType();
        for (int i = 0; i < list.size(); i++) {
            if (type.equals(list.get(i).getKey().replaceAll(" ", ""))) {
                return (list.get(i).getMarker());
            }

        }
        return (R.mipmap.ic_benh_vien);

    }

    public static int getIconMipmapCircleHospital(String type) {
        if (list == null) getListHospitalType();
        for (int i = 0; i < list.size(); i++) {
            if (type.equals(list.get(i).getKey().replaceAll(" ", ""))) {
                return (list.get(i).getIcon());
            }

        }
        return (R.drawable.ic_c_benh_vien);

    }

    public static double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }

    public static String calculationByDistanceKm(LatLng StartP, LatLng EndP) {
        double valueResult = calculationByDistance(StartP, EndP);
        DecimalFormat newFormat = new DecimalFormat("####.##");
        double km = valueResult / 1;
        return newFormat.format(km);
    }

    public static double calculationByDistanceM(LatLng StartP, LatLng EndP) {
        return calculationByDistance(StartP, EndP) * 1000;
    }


    public static void getNamePlaceByLatlng(Context context, final LatLng latLng, final OnNamePlaceBuyLatlng namePlaceBuyLatlng) {
        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latLng.latitude + "," + latLng.longitude + "&sensor=true";
        Log.e(TAG, "getNamePlaceByLatlng: " + url);
        Ion.with(context)
                .load(url)
                .asJsonObject()
                .withResponse()
                .setCallback(new FutureCallback<Response<JsonObject>>() {
                    @Override
                    public void onCompleted(Exception e, final Response<JsonObject> result) {
                        if (e == null) {
                            try {
                                JsonArray results = result.getResult().getAsJsonArray("results");
                                JsonObject object = results.get(0).getAsJsonObject();
                                String long_name = object.get("formatted_address").getAsString();
                                namePlaceBuyLatlng.onName(long_name.split("\\,")[0], long_name, latLng);
                            } catch (Exception e1) {
                                Log.e(TAG, "onCompleted: ", new Throwable(e1));
                                namePlaceBuyLatlng.error();
                            }
                        } else {
                            namePlaceBuyLatlng.error();
                        }
                    }
                });
    }

    private static List<HospitalTypeModel> list;

    public static List<HospitalTypeModel> getListHospitalType() {
        if (list == null)
            list = new ArrayList<>();
        if (list.size() == 0) {
            list.add(new HospitalTypeModel(1, "Phòng khám", "phong-kham", R.mipmap.ic_phong_kham, R.drawable.ic_c_phong_kham));
            list.add(new HospitalTypeModel(2, "Bệnh viện", "benh-vien", R.mipmap.ic_benh_vien, R.drawable.ic_c_benh_vien));
            list.add(new HospitalTypeModel(3, "Trung tâm tư vấn và chăm sóc sức khỏe", "trung-tam-tu-van-va-cham-soc-suc-khoe", R.mipmap.ic_tt_tv_cs_sk, R.drawable.ic_c_tttv_va_cham_soc_sk));
            list.add(new HospitalTypeModel(4, "Trung tâm y tế", "trung-tam-y-te", R.mipmap.ic_tt_y_te, R.drawable.ic_c_tt_y_te));
            list.add(new HospitalTypeModel(5, "Spa", "spa", R.mipmap.ic_spa, R.drawable.ic_c_spa));
            list.add(new HospitalTypeModel(6, "Khoa điều trị", "khoa-dieu-tri", R.mipmap.ic_khoa_dieu_tri, R.drawable.ic_c_khoa_dieu_tri));
            list.add(new HospitalTypeModel(7, "Trạm y tế", "tram-y-te", R.mipmap.ic_tram_y_te, R.drawable.ic_c_ttye));
            list.add(new HospitalTypeModel(8, "Trung tâm cấp cứu", "trung-tam-cap-cuu", R.mipmap.ic_trung_tam_cap_cuu, R.drawable.ic_c_tt_cap_cuu));
            list.add(new HospitalTypeModel(9, "Viện dưỡng lão", " vien-duong-lao", R.mipmap.ic_vien_duon_lao, R.drawable.ic_c_vien_duon_lao));
            list.add(new HospitalTypeModel(10, "Phòng khám thú y", "phong-kham-thu-y", R.mipmap.ic_thu_y, R.drawable.ic_c_thu_y));
            list.add(new HospitalTypeModel(11, "Trung tâm tư vấn tâm lý", "trung-tam-ty-van-tam-ly", R.mipmap.ic_tt_tu_van_y_te, R.drawable.ic_c_nha_thuoc));
            list.add(new HospitalTypeModel(12, "Thẩm mỹ viện", "tham-my-vien", R.mipmap.ic_tham_my_vien, R.drawable.ic_c_spa));
            list.add(new HospitalTypeModel(13, "Nhà thuốc", "nha-thuoc", R.mipmap.ic_nha_thuoc, R.drawable.ic_c_nha_thuoc));
            list.add(new HospitalTypeModel(14, "Cửa hàng thiết bị y tế", "cua-hang-thiet-bi-y-te", R.mipmap.ic_cua_hang_tbyt, R.drawable.ic_c_cua_hang_thiet_bi));
            list.add(new HospitalTypeModel(15, "Salon", "salon", R.mipmap.ic_salon, R.drawable.ic_c_salon));
            list.add(new HospitalTypeModel(16, "Tầm quất &Massage", "tam-quat-massage", R.mipmap.ic_massage, R.drawable.ic_c_massage));
            list.add(new HospitalTypeModel(17, "Phòng khám y học cổ truyền", "phong-kham-y-hoc-co-truyen", R.mipmap.ic_pk_yhct, R.drawable.ic_c_pk_yh_co_truyen));
            list.add(new HospitalTypeModel(18, "Trung tâm nghiên cứu sinh học và y dược", "trung-tam-nghien-cuu-sinh-hoc-va-y-duoc", R.mipmap.ic_tt_nc_sinh_hoc_va_y_duoc, R.drawable.ic_c_tttn_nc_sh_vs_yd));
            list.add(new HospitalTypeModel(19, "Gym, yoga, thể thao", "gym-yoga-the-thao", R.mipmap.ic_gym, R.drawable.ic_c_gym));
            list.add(new HospitalTypeModel(20, "Trung tâm xét nghiệm", "xet-nghiem", R.mipmap.ic_tt_xet_nghiem, R.drawable.ic_c_tt_set_nghiem));
        }

        return list;
    }

    public static String getNameTypeHospitalByID(int id) {
        List<HospitalTypeModel> list = getListHospitalType();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == id) {
                return "<b>" + list.get(i).getName() + "</b>";
            }
        }
        return "<b>loại cơ sở y tế này</b>";
    }

    public static String getNameTypeHospitalByType(String type) {
        if (type == null) {
            return "Cơ sở y tế";
        }
        for (HospitalTypeModel typeModel : getListHospitalType()) {
            if (type.equals(typeModel.getKey())) {
                return typeModel.getName();
            }
        }
        return "Cơ sở y tế";
    }

    public static int getIdByTypeName(String type) {
        for (HospitalTypeModel typeModel : getListHospitalType()) {
            if (type.equals(typeModel.getKey())) {
                return typeModel.getId();
            }
        }
        return -1;
    }


    public interface OnNamePlaceBuyLatlng {
        void onName(String name, String address, LatLng latLng);

        void error();
    }
}
