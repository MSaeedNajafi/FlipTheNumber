package com.flipthenumber.beta.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.flipthenumber.beta.MainActivity;
import com.flipthenumber.beta.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    SharedPreferences sharedPreferences;
    Context context;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = getApplicationContext();

        Log.i("onMessageReceived","here");

//        String sented = remoteMessage.getData().get("sented");
//        String user = remoteMessage.getData().get("user");
//
//        SharedPreferences preferences = getSharedPreferences("PREFS", MODE_PRIVATE);
//        String currentUser = preferences.getString("currentuser", "none");

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            sendOreoNotification(remoteMessage);
//        } else {
//            sendNotification(remoteMessage);
//        }


   //     sendNotification(remoteMessage.getNotification().getBody());




        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched


            Log.i("TitleAndBody",remoteMessage.getNotification().getTitle()+"\n"
            +remoteMessage.getNotification().getBody());

            // directly as below.
            showNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }




    }

    private void sendOreoNotification(RemoteMessage remoteMessage){

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
       // String messageType = remoteMessage.getData().get("messageType");
        int j = (int) System.currentTimeMillis();
        Intent intent = null;


//        if (messageType.equals("Booking Accept"))
//        {
//            intent = new Intent(context, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "Booking Accept");
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//
//        else if (messageType.equals("Booking Cancel"))
//        {
//            intent = new Intent(context, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "Booking Cancel");
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//
//        else if (messageType.equals("Driver Arrived"))
//        {
//            intent = new Intent(context, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "Driver Arrived");
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//
//        else if (messageType.equals("Driver Reached"))
//        {
//
//            intent = new Intent(context, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "Driver Reached");
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }


        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder;

        builder = oreoNotification.getOreoNotification(title, body, pendingIntent,
                defaultSound);

        int i = 0;
        if (j > 0) {
            i = j;
        }

        oreoNotification.getManager().notify(i, builder.build());



    }

    private void sendNotification(RemoteMessage remoteMessage) {


        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
      //  String messageType = remoteMessage.getData().get("messageType");

        int j = (int) System.currentTimeMillis();
        Intent intent = null;


//        if (messageType.equals("Booking Accept"))
//        {
//            intent = new Intent(context, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "Booking Accept");
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//
//        else if (messageType.equals("Booking Cancel"))
//        {
//            intent = new Intent(context, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "Booking Cancel");
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//
//
//        else if (messageType.equals("Driver Arrived"))
//        {
//            intent = new Intent(context, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "Driver Arrived");
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }
//
//
//        else if (messageType.equals("Driver Reached"))
//        {
//            intent = new Intent(context, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "Driver Reached");
//            intent.putExtras(bundle);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);
//        }



        PendingIntent pendingIntent = PendingIntent.getActivity(this, j, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder;

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0;
        if (j > 0) {
            i = j;
        }

        noti.notify(i, builder.build());



    }





//    private void sendNotification(String messageBody) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("FCM Message")
//                .setContentText(messageBody)
//                .setAutoCancel(true)
//                .setSound(defaultSoundUri)
//                .setContentIntent(pendingIntent);
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
//    }







    public void showNotification(String title,
                                 String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, MainActivity.class);
        // Assign channel ID
        String channel_id = "notification_channel";
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(),
                channel_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(
                    getCustomDesign(title, message));
        } // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        else {
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.mipmap.ic_launcher);
        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "web_app",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }




    // Method to get the custom Design for the display of
    // notification.
    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.mipmap.ic_launcher);
        return remoteViews;
    }



}
