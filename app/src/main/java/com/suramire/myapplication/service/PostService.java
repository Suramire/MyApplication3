package com.suramire.myapplication.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.MainActivity;
import com.suramire.myapplication.R;
import com.suramire.myapplication.activity.NoteDetailActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.L;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

/**
 * Created by Suramire on 2017/6/23.
 */

public class PostService extends Service {
    private boolean flag = true;
    private Note note;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int space = intent.getIntExtra("space", 345);
        Log.d("PostService", "space:" + space);
        try {
            random((int) (Math.random() * 100));
//            random(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(note!=null){
            showNotification();
        }

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
        Intent intent2 = new Intent(this, NoteDetailActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent2.putExtra("note",note);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent2,FLAG_UPDATE_CURRENT);
        builder.setSmallIcon(R.drawable.ic_menu_camera)
                .setTicker("收到一条新通知")
                .setContentTitle(note.getTitle())
                .setContentText(note.getContent())
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        notificationManager.notify(0x233,builder.build());
    }

    /**
     * Created by Weeks Wei on 2017/6/26.
     * 通知随机显示一条帖子数据
     * random()向服务器请求查询一条数据
     *
     * @param random 帖子的ID
     */
    private void random(int random) throws IOException {
        //1.拿到okHttpClient对象
        final OkHttpClient okHttpClient = new OkHttpClient();
        //2.构造Request
        Request.Builder builder = new Request.Builder();
//        Request request = builder.get().url(Constant.URLNOTIFICATION + "?nid=" + random + "&NewNote=randomQuery").build();
        Request request = builder.get().url(Constant.URL + "randomQuery&nid=" + random).build();
        //3.将Request封装为Call
        Call call = okHttpClient.newCall(request);
        L.e(Constant.URLNOTIFICATION + "?nid=" + random + "&NewNote=randomQuery");
        //4.执行call
//        Response response = call.execute();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
//                Toast.makeText(getApplicationContext(),"连接服务器失败，请重试",Toast.LENGTH_LONG).show();
                L.e("onFailure:" + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                Toast.makeText(getApplicationContext(),"获得数据："+response,Toast.LENGTH_LONG).show();
                L.e("onResponse:");
                String res = response.body().string();
//                L.e(res);


                if (res != null) {
                    //截取
                    String part1 = res.substring(0, res.indexOf("?"));
                    String part2 = res.substring(res.indexOf("?") + 1);
                    L.e("part1:" + part1);
                    L.e("part2:" + part2);
                    if (!part2.equals("{}")) {

                        // android 自带json
                        note = new Note();
                        Gson gson = new Gson();
                        note = gson.fromJson(part2, Note.class);
                    }
                }

            }
        });

    }
}
