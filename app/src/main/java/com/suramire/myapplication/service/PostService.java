package com.suramire.myapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.suramire.myapplication.MainActivity;
import com.suramire.myapplication.R;
import com.suramire.myapplication.activity.TestActivity;

/**
 * Created by Suramire on 2017/6/23.
 */

public class PostService extends Service {
    private boolean flag = true;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        int space = intent.getIntExtra("space", 30);
//        Log.d("PostService", "space:@onBind" + space);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (flag) {
//                    Log.d("PostService", "i:");
//                    SystemClock.sleep(1000);
//                }
//            }
//        }).start();

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int space = intent.getIntExtra("space", 345);
        Log.d("PostService", "space:" + space);
        showNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        flag = false;
        super.onDestroy();
    }

    /**
     * 显示通知
     */
    private void showNotification(){
        NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        Intent intent2 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent2,0);
        builder.setSmallIcon(R.drawable.ic_menu_camera)
                .setTicker("收到一条新通知")
                .setContentTitle("标题")
                .setContentText("这是正文内容")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        notificationManager.notify(0x233,builder.build());
    }
}
