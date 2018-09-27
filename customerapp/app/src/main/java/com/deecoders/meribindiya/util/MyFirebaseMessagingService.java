package com.deecoders.meribindiya.util;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.deecoders.meribindiya.R;
import com.deecoders.meribindiya.activity.Splash;
import com.deecoders.meribindiya.receiver.MyReceiver;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by Kunwar's on 27-Jan-17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("message", "new message came");
        if(remoteMessage.getData() != null){
            String title = remoteMessage.getData().get("title");
            String msg = remoteMessage.getData().get("alert_message");
            String image = remoteMessage.getData().get("bitmap");
            Bitmap bmp = getBitmapfromUrl(image);
            if(msg != null)
                showSimpleNotification(title, msg, bmp);
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        if(imageUrl == null)
            return null;
        if(imageUrl.isEmpty())
            return null;

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public void showSimpleNotification(String title, String msg, Bitmap bmp){
        Log.e("message", "notification came");
        // on click open activities screen
        Intent intent = new Intent(this, Splash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Random r = new Random();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, r.nextInt(10000), intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(msg);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        notificationBuilder.setVibrate(new long[] {1000, 1000});

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "channel-1";
        String channelName = "MeriBindiya";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
            notificationBuilder.setChannelId(channelId);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.white));
            notificationBuilder.setSmallIcon(R.drawable.login_logo);
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.login_logo));
            if(bmp != null){
                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bmp));
            }
        }
        else {
            notificationBuilder.setSmallIcon(R.drawable.logo);
        }
        notificationBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(r.nextInt(10000), notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("tag", "onTaskRemoved");
        Intent myIntent = new Intent(getBaseContext(), MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        long interval = 6 * 1000; //
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), interval, pendingIntent);
        super.onTaskRemoved(rootIntent);
    }
}
