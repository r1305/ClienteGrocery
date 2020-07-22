package com.freshfastfood.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.freshfastfood.MainActivity;
import com.freshfastfood.R;
import com.freshfastfood.activity.FirstActivity;
import com.freshfastfood.utils.MyWorker;
import com.freshfastfood.utils.Utiles;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String token = "123";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("TAG", "From: " + remoteMessage.getFrom());


        //getting the title and the body
        //String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getData().get("body");
        String title= remoteMessage.getData().get("title");

        new Utiles().showNotification(getApplicationContext(),body,title);

    }

}