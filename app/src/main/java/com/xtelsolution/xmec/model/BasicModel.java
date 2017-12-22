package com.xtelsolution.xmec.model;

import com.xtelsolution.sdk.callback.RequestOkHttp;
import com.xtelsolution.sdk.callback.RequestServer;

/**
 * Created by Lê Công Long Vũ on 1/3/2017
 */

public class BasicModel {
    public static final String HOSPITAL_GET_RATE = "hospital/detail/rate?id=";
    public static final String VALIDATE_PHONE = "/validate/phone?msisdn=";
    /*
    * Nip
    * */
    public final String BASE_NIP = "http://authen.xtel.vn/nipum/v1.0/";
    public final String LOGIN = "m/user/login/";
    public final String REGISTER_ACCOUNT = "g/v2/user";
    public final String ACTIVE_ACCOUNT = "g/user/active";
    public final String RE_ACTIVE_ACCOUNT = "g/user/re-active";
    public final String RESET_PASSWORD = "g/user/reset-pwd";
    public final String CHANGE_PASSWORD = "g/user/change-pwd";
    public final String AUTHENTICATE = "m/user/authenticate";
    /*
    * Api
    * */
    public final String BASE_API = "http://x-mec.com/api/";
    /*
    * End Nip
    * */
    public final String USER_INFO = "user/info";
    // Y bạ
    public final String MBR = "medical-report-book";
    public final String SHARED = "/shared";
    public final String SHARE = "/share";
    public final String ME = "/me";
    public final String MBR_SHARE = "/share";
    public final String MBR_USER = "/users";
    public final String MBR_TYPE = "?type=";
    public final String MBR_ID = "&mrb_id=";
    public final String MBR_DELETE = "?mrb_id=";
    public final String MBR_BY_ID = "/detail?mrb_id=";
    public final String HR = "health-record";
    public final String HR_ID = "?hr_id=";
    public final String ID = "?id=";
    public final String SYSTEM = "system";
    public final String DISEASE_ENTITY = "system";
    public final String IMAGE_TYPE = "?image_type=";
    public final String SCHEDULE = "/schedule";
    public final String TIME_PER_DAY = "/time-per-day";
    public final String DONE = "/done";
    public final String CONTINUE = "/continute";
    public final String USER = "/user";
    public final String INFO = "/info";
    public final String FCM = "/fcm";
    public final String DEVICE = "/device";
    public final String DISEASE = "/disease";
    public final String UD_ID = "?ud_id=";
    public final String DRUG = "/drug";
    public final String DRUG_DETAIL = "/detail?id=";
    public final String DRUG_SEARCH = "/search?key=";
    public final String IMAGE = "/image";
    public final String LIST = "/list";
    public final String SEARCH = "/search";
    public final String TYPE = "?type=";
    public final String KEY = "?key=";
    public final String SD_ID = "?sd_id=";
    public final String SHARE_TYPE = "/type";
    public final String HR_DRUG_SEARCH_KEY = "/search?key=";
    public final String HR_DRUG_PAGE = "&page=";
    public final String HR_DRUG_PAGE_SIZE = "&pagesize=";
    public final String HR_ADD_DRUG = "/drug/image?image_type=";
    public final String HR_D_SEARCH_KEY = "/disease/search?key=";
    public final String DISEASE_DETAIL = "/detail?id=";
    public final String HR_D_SEARCH_PAGE = "&page=";
    public final String HR_D_SEARCH_SIZE = "&pagesize=";
    public final String HOSPITAL_SEARCH_KEY = "hospital/search?key=";
    public final String HOSPITAL_AROUND = "hospital/around?latitude=%1$s&longitude=%2$s&radius=5&type=%3$s";
    public final String HOSPITAL_SEARCH_PAGE = "&page=";
    public final String HOSPITAL_SEARCH_SIZE = "&pagesize=";
    public final String HOSPITAL_DETAIL = "hospital/detail?id=";
    public final String HOSPITAL_RATE = "hospital/rate";
    public final String SEARCH_PHONE = "user/search?phone=";
    // Image
    public final String UPLOAD_AVATAR = "upload/image/mrb/avatar";
    public final String UPLOAD_USER_AVATAR = "upload/image/user/avatar";
    public final String UPLOAD_DRUG_IMAGE = "upload/image/drug";
    public final String UPLOAD_HR_IMAGE = "upload/image/health-record";
    //Notification
    public final String USER_NOTIFICAION = "user/notification";
    public final String SYSTEM_NOTIFICAION = "system/notification";

    // new
    public String POST_LIST_CATEGORY ="/post/category/list";
    public String CATEGORY_ID ="?category_id=";
    public static final String POST_LIST_INFO = "/post/info/list";

    //Image URL
    public static final String IMAGE_URL = "/image/url";



    public RequestServer requestServer = new RequestServer();
    public RequestOkHttp requestOkHttp = new RequestOkHttp();


    /*
    * End Api
    * */
}