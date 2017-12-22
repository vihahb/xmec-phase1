package com.xtelsolution.xmec.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.xtelsolution.MyApplication;
import com.xtelsolution.sdk.commons.Constants;
import com.xtelsolution.sdk.utils.Base64Helper;
import com.xtelsolution.sdk.utils.JsonHelper;
import com.xtelsolution.sdk.utils.MessageNotification;
import com.xtelsolution.sdk.utils.SharedUtils;
import com.xtelsolution.sdk.utils.Utils;
import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.NotificationAction;
import com.xtelsolution.xmec.model.entity.NotificationData;
import com.xtelsolution.xmec.model.entity.NotifyConfig;
import com.xtelsolution.xmec.model.resp.RESP_Notification;
import com.xtelsolution.xmec.model.server.NotificationModel;
import com.xtelsolution.xmec.view.activity.HomeActivity;
import com.xtelsolution.xmec.view.activity.login.SlideViewActivity;
import com.xtelsolution.xmec.view.activity.notification.NotificationActivity;

import java.util.Map;

/**
 * Author: Lê Công Long Vũ
 * Date: 10/23/2017
 * Email: leconglongvu@gmail.com
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static int count;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData());

            Map<String, String> map = remoteMessage.getData();
            NotificationAction action = JsonHelper.getObject(map.get("action"), NotificationAction.class);

            String mrb_name = action.getMrb_name();
            action.setMrb_name(Base64Helper.getDecode(mrb_name));

            NotificationData payloadEntity = new NotificationData();
            payloadEntity.setName(Base64Helper.getDecode(map.get("name")));
            payloadEntity.setType(Integer.parseInt(map.get("type")));
            payloadEntity.setUid(Integer.parseInt(map.get("uid")));
            payloadEntity.setTitle(map.get("title"));
            payloadEntity.setBody(map.get("body"));
            payloadEntity.setAction(action);

            Log.e("Dât object", JsonHelper.toJson(payloadEntity));
            sendBroadcast(new Intent("ACTION_GET"));
            initNotify(payloadEntity, action);
            getBadgeCount();
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void initNotify(NotificationData payloadEntity, NotificationAction action) {
        boolean getNotifyUser = SharedUtils.getInstance().getBooleanValue(Constants.FLAG_NOTIFICATION);
        String user = SharedUtils.getInstance().getStringValue(Constants.USER_FULLNAME);
        String title;
        String message = "";

        Intent backIntent = new Intent(this, HomeActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent intent = new Intent(this, NotificationActivity.class);
        final PendingIntent pendingIntentPositive = PendingIntent.getActivities(this, 11,
                new Intent[] {backIntent, intent}, PendingIntent.FLAG_ONE_SHOT);




//        Intent notifyIntentPositive = new Intent(this, NotificationActivity.class);
//        notifyIntentPositive.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntentPositive = PendingIntent.getActivity(this, 11, notifyIntentPositive, PendingIntent.FLAG_UPDATE_CURRENT);

        int id_notification = 0;
        int number = 0;
        switch (payloadEntity.getType()) {
            case 2:
                if (getNotifyUser){
                    number++;
                    title = "Thông báo liên kết";
                    switch (payloadEntity.getAction().getState()){
                        case 0:
                            message = "<b>" + payloadEntity.getName() + "</b> đã gửi cho bạn một yêu cầu liên kết y bạ <b>" + payloadEntity.getAction().getMrb_name() + "</b>";
                            break;
                        case 1:
                            message = "<b>" + payloadEntity.getName() + "</b> đã đồng ý yêu cầu liên kết y bạ <b>" + payloadEntity.getAction().getMrb_name() + "</b> của bạn";
                            break;
                        case 2:
                            Intent delete_intent = new Intent(Constants.ACTION_DELETE_NOTIFY);
                            delete_intent.putExtra(Constants.ACTION_DELETE, action);
                            delete_intent.putExtra(Constants.TYPE, Constants.TYPE_LINK);
                            sendBroadcast(delete_intent);
                            message = "<b>" + payloadEntity.getName() + "</b> đã từ chối yêu cầu liên kết y bạ <b>" + payloadEntity.getAction().getMrb_name() + "</b> của bạn";
                            break;
                    }
                    MessageNotification.notify(MyApplication.context,
                            user,
                            title,
                            message,
                            number,
                            pendingIntentPositive, 2);
                }
                break;
            case 1:
                if (getNotifyUser){
                    id_notification = 2;
                    title = "Thông báo chia sẻ";
                    switch (payloadEntity.getAction().getState()){
                        case 0:
                            message = "<b>" + payloadEntity.getName() + "</b> đã gửi cho bạn một yêu cầu chia sẻ y bạ <b>" + payloadEntity.getAction().getMrb_name() + "</b>";
                            break;
                        case 1:
                            message = "<b>" + payloadEntity.getName() + "</b> đã đồng ý yêu cầu chia sẻ y bạ <b>" + payloadEntity.getAction().getMrb_name() + "</b> của bạn";
                            break;
                        case 2:
                            message = "<b>" + payloadEntity.getName() + "</b> đã từ chối yêu cầu chia sẻ y bạ <b>" + payloadEntity.getAction().getMrb_name() + "</b> của bạn";
                            Intent delete_intent = new Intent(Constants.ACTION_DELETE_NOTIFY);
                            delete_intent.putExtra(Constants.ACTION_DELETE, action);
                            delete_intent.putExtra(Constants.TYPE, Constants.TYPE_SHARE);
                            sendBroadcast(delete_intent);
                            break;
                    }

                    MessageNotification.notify(MyApplication.context,
                            user,
                            title,
                            message,
                            number,
                            pendingIntentPositive, 1);
                }
                break;

            case 3:
                id_notification = 2;
                title = "Thông báo từ hệ thống";
                message = payloadEntity.getBody();
                MessageNotification.notify(MyApplication.context,
                        user,
                        title,
                        message,
                        number,
                        pendingIntentPositive, 3);
                break;
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, SlideViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }
    }

    private void getBadgeCount(){
        count = 0;
        //Get Count notify
        int type;
        if (SharedUtils.getInstance().getStringValue(Constants.SESSION) != null){
            type = 1;
        } else {
            type = 0;
        }
        new NotificationModel(type) {
            @Override
            protected void onSuccess(RESP_Notification obj) {
                for (int i = 0; i < obj.getData().size(); i++) {
                    if (obj.getData().get(i).getNotifyState() == NotifyConfig.STATE_PENDING){ //0
                        count++;
                    }
                }
                SharedUtils.getInstance().putIntValue(Constants.BADGE_COUNT, count);
                Log.e(TAG, "Count: " +count);
                if (Utils.isAppRunning(MyApplication.context, MyApplication.context.getPackageName())){
                    Intent intent = new Intent("ACTION_NOTIFY");
                    intent.putExtra(Constants.BADGE_COUNT, count);
                    sendBroadcast(intent);
                }
            }

            @Override
            protected void onError(int error) {
                MyApplication.log("Err get Count", "Code: " + error);
            }
        };
    }
}