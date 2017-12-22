package com.xtelsolution.sdk.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;

import com.xtelsolution.xmec.R;
import com.xtelsolution.xmec.model.entity.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Author: Lê Công Long Vũ
 * Date: 11/6/2017
 * Email: leconglongvu@gmail.com
 */
public class NotificationUtils {
    private Context mContext;


    public NotificationUtils(Context context) {
        this.mContext = context;
    }

//    public void showNotificationNomal(NotificationCompat.Builder notificationBuilder, String TitleNotification,
//                                                     String messageNotification,
//                                                     int iconNotification,
//                                                     int iconNegative, String TitleNegative, PendingIntent pendingNegative,
//                                                     int iconPositive, String TitlePositive, PendingIntent pendingPositive) {
//
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//
//        inboxStyle.addLine(messageNotification);
//        Notification notification = null;
//
//        if (iconNegative != -1 && !TextUtils.isEmpty(TitleNegative)) {
//            notificationBuilder.addAction(iconNegative, TitleNegative, pendingNegative);
//        }
//
//        if (iconPositive != -1){
//            notificationBuilder.addAction(iconPositive, TitlePositive, pendingPositive);
//        }
//               notificationBuilder.build();
//
//        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(Config.NOTIFICATION_ID, notification);
//
//
//
////        Notification notification;
////        notification = notificationBuilder
////                .setSmallIcon(iconNotification)
////                .setContentText(messageNotification)
////                .setContentTitle(TitleNotification);
//
//        @SuppressLint("ServiceCast") NotificationManagerCompat notificationManagerCompat = (NotificationManagerCompat) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManagerCompat.notify(1, notificationBuilder.build());
//    }

    public void showSmallNotification(int id_notification, int icon,
                                      String title,
                                      String message,
                                      int iconNegative,
                                      int iconPositive, PendingIntent pendingPositive,
                                      Uri alarmSound) {

        int numberNotify = 0;
        numberNotify++;

        NotificationCompat inboxStyle = new NotificationCompat();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        notificationBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(mContext, R.color.toolbar_end))
                .setContentTitle(title)
//                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingPositive)
//                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(alarmSound)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .setShowWhen(true)
                .setNumber(numberNotify);

        if (iconPositive != -1) {
            notificationBuilder.addAction(iconPositive, "Tới thông báo", pendingPositive);
        }

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(id_notification, notificationBuilder.build());
        }
    }


    public void showBigNotification(int icon,
                                    String banner,
                                    String title,
                                    String message,
                                    int iconNegative, String TitleNegative, PendingIntent pendingNegative,
                                    int iconPositive, String TitlePositive, PendingIntent pendingPositive,
                                    Uri alarmSound) {
        int numberNotify = 0;
        numberNotify++;

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle(title);
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(banner));


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext);
        notificationBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
//                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setStyle(bigPictureStyle)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingPositive)
//                .setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(alarmSound)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                .setContentText(message)
                .setNumber(numberNotify);
        if (iconNegative != -1 && !TextUtils.isEmpty(TitleNegative)) {
            notificationBuilder.addAction(iconNegative, TitleNegative, pendingNegative);
        }

        if (iconPositive != -1) {
            notificationBuilder.addAction(iconPositive, TitlePositive, pendingPositive);
        }

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notificationBuilder.build());
        }
    }

    /**
     * Downloading push notification image before displaying it in
     * the notification tray
     */
    private Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}