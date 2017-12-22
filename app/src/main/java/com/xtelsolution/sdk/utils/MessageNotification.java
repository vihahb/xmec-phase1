package com.xtelsolution.sdk.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.xtelsolution.xmec.R;

/**
 * Helper class for showing and canceling message
 * notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class MessageNotification {
    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "Message";

    /**
     * Shows the notification, or updates a previously shown notification of
     * this type, with the given parameters.
     * <p>
     * TODO: Customize this method's arguments to present relevant content in
     * the notification.
     * <p>
     * TODO: Customize the contents of this method to tweak the behavior and
     * presentation of message notifications. Make
     * sure to follow the
     * <a href="https://developer.android.com/design/patterns/notifications.html">
     * Notification design guidelines</a> when doing so.
     *
     * @see #cancel(Context)
     */
    public static void notify(final Context context,
                              String dumpmy_text,
                              final String titles,
                              final String message,
                              final int number,
                              PendingIntent peding_accept, int type) {
        final Resources res = context.getResources();
        Bitmap picture = null;
        switch (type){
            case 1:
                picture = BitmapFactory.decodeResource(res, R.mipmap.ic_circle_share);
                break;
            case 2:
                picture = BitmapFactory.decodeResource(res, R.mipmap.ic_link);
                break;
            case 3:
                picture = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher_v1);
                break;
        }


        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_v1)
                .setContentTitle(titles)
                .setContentText(Html.fromHtml(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(picture)
                .setTicker(titles)
                .setNumber(number)
                .setContentIntent(
                        peding_accept)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(Html.fromHtml(message))
                        .setBigContentTitle(titles)
                        .setSummaryText(dumpmy_text))
                // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        notify(context, builder.build());
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        }
    }

    /**
     * Cancels any notifications of this type previously shown using
     * {@link #notify(Context, String, String, String, int, PendingIntent, Drawable)}.
     */
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null) {
            nm.cancel(NOTIFICATION_TAG, 0);
        }
    }
}
