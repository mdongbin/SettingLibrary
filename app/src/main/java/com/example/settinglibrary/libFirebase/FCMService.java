package com.example.settinglibrary.libFirebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.settinglibrary.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMService extends FirebaseMessagingService {
    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if(remoteMessage.getNotification() != null){
            Log.e(TAG, "알림 메시지 : " + remoteMessage.getNotification());
            String msgBody = remoteMessage.getNotification().getBody();
            String msgTitle = remoteMessage.getNotification().getTitle();

            Intent intent = new Intent(this, FirebaseActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            Notification.Builder notiBuilder = new Notification.Builder(this, "test")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(msgTitle)
                    .setContentText(msgBody)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel("test", "CH_NAME", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);

            manager.notify(5555, notiBuilder.build());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.e(TAG, "Refreshed token : " + s);
    }
}
