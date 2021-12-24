package com.flipthenumber.beta.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.flipthenumber.beta.R;
import com.flipthenumber.beta.ui.activities.FriendListActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

import static com.facebook.login.widget.ProfilePictureView.TAG;

/**
 * Created by Belal on 12/8/2017.
 */
//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    private final static String default_notification_channel_id = "default";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        Log.d(TAG, "Received message; id: " + remoteMessage.getMessageId());
        Map<String, String> data = remoteMessage.getData();
        Log.d(TAG, "Data: " + data);

        String title = data.get("title");
        String message = data.get("body");
         sendNotification(title,message);

        if (remoteMessage.getNotification() != null) {
            String title1 = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            sendNotification(title1,body);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String title, String content) {
        Intent notificationIntent = new Intent(MyFirebaseMessagingService.this, FriendListActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(MyFirebaseMessagingService.this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this,
                default_notification_channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(content);

      //  mBuilder.setContentIntent(intent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new
                    NotificationChannel(NOTIFICATION_CHANNEL_ID, "goefire", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }
}
