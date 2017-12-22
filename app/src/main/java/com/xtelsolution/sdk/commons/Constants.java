package com.xtelsolution.sdk.commons;

import com.xtelsolution.MyApplication;
import com.xtelsolution.xmec.R;

/**
 * Created by Lê Công Long Vũ on 12/27/2016
 * <p>
 * 12323213
 */

public class Constants {
    public static final String SHARED_NAME = "share_business";

    public static final String PHONE_TYPE = "PHONE-NUMBER";
    public static final String SERVICE_CODE = "XTL-XMEC";

    public static final int DEFAULT_SIZE = 20;
    public static final String IS_UPDATE_FCM = "is_update_fcm";

    public static final String BADGE_COUNT = "badge_count";
    public static final String MRB_ID = "mrb_id";
    public static final String MAP_INFO = "map_info";
    public static final String ACTION_RELOAD = "action_reload";
    public static final String ID_LIST = "ID_LIST";
    public static final String STATE = "state";
    public static final String TYPE_LINK = "type_link";
    public static final String TYPE_SHARE = "type_share";
    public static final String TYPE_MBR = "type_mbr";
    public static final String ACTION_UPDATE = "action_update";
    public static final String ACTION_UPDATE_NOTIFY = "action_update_notify";

    public static final String HOSPITAL_TYPE = "hospital_type";
    /*
    * Error Code
    * */
    public static final int ERROR_NO_INTERNET = -3;
    public static final int ERROR_UNKOW = -1;
    public static final int ERROR_SESSION = 2;
    public static final int ERROR_EMPTY = 404;
    public static final int ERROR_VIEW_ONLY = 11;


    public static final String FLAG_NOTIFICATION = "flag_notification";
    public static final String LAST_DISEASE = "last_disease";

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    /*
    * Account
    * */
    public static final String AUTHENTICATION_ID = "authentication_id";
    public static final String SESSION = "session";

    /*
    * End Error Code
    * */
    public static final String LOGIN_TIME = "login_time";
    public static final String EXPIRED_TIME = "expired_time";
    public static final String TIME_ALIVE = "time_alive";
    public static final String DEV_INFO_STATUS = "dev_info_status";
    /*
    * User Info
    * */
    public static final String USER_UID = "user_uid";
    public static final String USER_FULLNAME = "user_fullname";
    /*
    * End Account
    * */
    public static final String USER_GENDER = "user_gender";
    public static final String USER_BIRTHDAYLONG = "user_birthday";
    public static final String USER_PHONENUMBER = "user_phonenumber";
    public static final String USER_ADDRESS = "user_address";
    public static final String USER_WEIGHT = "user_weight";
    public static final String USER_HEIGHT = "user_height";
    public static final String USER_AVATAR = "user_avatar";
    public static final String OBJECT = "object";
    public static final String OBJECT_2 = "object_2";
    /*
    * End User Info
    * */
    public static final String OBJECT_3 = "object_3";
    public static final String DRUG_LIST = "drug_list";
    public static final String BUNDLE = "bundle";
    /*
    * Field*/
    public static final String URL_INTENT = "url_intent";
    /**
     * Action
     */
    public static final String ACTION_SHARE = "action_share";
    public static final String POSITION = "position";
    public static final String ACTION = "ACTION";
    public static final String ACTION_DELETE_NOTIFY = "ACTION_DELETE_NOTIFY";
    public static final String SHARE_ID = "share_id";
    public static final String CATEGORY_SAVE = "category_save";
    public static final String BASE_IMAGE_URL = "base_image_url";
    public static final String ENABLE = "enable";
    public static final String GO_TO_ITEM = "go_to_item";
    public static final String FIRST_SHOW = "first_show";
    public static String LAST_DRUG = "last_drug";
    public static final int SEARCH_HOSPITALBY_KEY = 1;
    public static final int SEARCH_HOSPITALBY_LATLNG = 2;

    public final static String ACTION_ADD = "ACTION_ADD";
    public final static String ACTION_DELETE = "ACTION_DELETE";
    public final static int ERROR_PERMISSION_LOCATION = 1;
    public final static int ERROR_OFF_LOCATION = 2;
    public static int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    public static String SEARCH_KEY = "search_key";
    public static final int REQUEST_CHECK_SETTINGS = 999;

    public static String getMessageErrorByCode(int error) {
        switch (error) {
            case -3:
                return MyApplication.context.getString(R.string.error_no_internet_try_again);

            default:
                return MyApplication.context.getString(R.string.error_unknow);
        }
    }

    //    LOCATION
    public static final String REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY";
    public static final String LOCATION_KEY = "LOCATION_KEY";
    public static final String LAST_UPDATED_TIME_STRING_KEY = "LAST_UPDATED_TIME_STRING_KEY";
}