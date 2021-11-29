package com.example.todo;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AlarmRecevier extends BroadcastReceiver {

    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel";
    private static String CHANNEL_NAME = "Channel";


    @Override
    public void onReceive(Context context, Intent intent) {

        manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(context);
        //알림창 클릭 시 activity 화면 부름
        Intent intent2 = new Intent(context, TodoManagementActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,intent2, PendingIntent.FLAG_UPDATE_CURRENT);

        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("TODO")
                .setContentText("할 일이 있어요!")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        manager.notify(1,builder.build());
    }

    public void createNotificationChannel(Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
    }

}
