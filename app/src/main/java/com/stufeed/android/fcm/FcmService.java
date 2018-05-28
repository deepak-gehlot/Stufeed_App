package com.stufeed.android.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.stufeed.android.R;
import com.stufeed.android.api.response.GetSettingResponse;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.HomeActivity;

import java.util.Map;

public class FcmService extends FirebaseMessagingService {
    public FcmService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();

        if (data.size() > 0) {
            String type = data.get("type");
            switch (type) {
                case "like":
                    break;
                case "comment":
                    break;
                case "follow":
                    break;
                case "add_board":
                    break;
                case "board":
                    break;

            }
            GetSettingResponse.Setting setting = Utility.getUserSetting(this);
            if (setting != null && setting.getIsNotification().equals("1")) {
                Intent intent = new Intent(this, HomeActivity.class);
                showNotification(setting, data, intent);
            }
        }
    }


    private void showNotification(GetSettingResponse.Setting setting, Map<String, String> data, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            notificationManager.createNotificationChannel(channel);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "default")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(data.get("title"))
                .setContentIntent(pendingIntent)
                .setContentText(data.get("body"))
                .setAutoCancel(true)
                .setLights(Color.parseColor("#ffb400"), 50, 10)
                .setVibrate(new long[]{500, 500, 500, 500})
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (setting.getIsSound().equals("1")) {
            mBuilder.setSound(soundUri);
        }
        notificationManager.notify(10, mBuilder.build());
    }
}
