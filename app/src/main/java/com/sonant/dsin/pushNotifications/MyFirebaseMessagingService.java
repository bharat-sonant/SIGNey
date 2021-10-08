package com.sonant.dsin.pushNotifications;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sonant.dsin.R;
import com.sonant.dsin.SubjectInternalActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        getFirebaseMessage(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getTitle());
    }

    public void getFirebaseMessage(String topic, String msg) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "myfirebasechannel")
                .setSmallIcon(R.drawable.notifciationvector)
                .setContentTitle(topic)
                .setContentText(msg)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(101, builder.build());

    }
}
