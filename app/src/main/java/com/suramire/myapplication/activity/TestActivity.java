package com.suramire.myapplication.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.MainActivity;
import com.suramire.myapplication.R;
import com.suramire.myapplication.util.FileUtil;
import com.suramire.myapplication.util.JsonUtil;
import com.suramire.myapplication.util.Constant;
import com.xmut.sc.entity.Note;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by Suramire on 2017/6/21.
 */

public class TestActivity extends AppCompatActivity {

    @Bind(R.id.imageView3)
    ImageView imageView3;
    @Bind(R.id.button6)
    Button button6;
    @Bind(R.id.button4)
    Button button4;
    @Bind(R.id.test_textview)
    TextView testTextview;
    @Bind(R.id.button5)
    Button button5;
    @Bind(R.id.button9)
    Button button9;
    @Bind(R.id.button8)
    Button button8;
    private TextView textView;
    private OkHttpClient okHttpClient;
    private Request.Builder builder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        okHttpClient = new OkHttpClient();
        builder = new Request.Builder();
        textView = (TextView) findViewById(R.id.test_textview);
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/myHead/head.jpg");
        imageView3.setImageBitmap(bitmap);
    }

    //接收JSON数据（JSONArray）并显示
    //使用OKHTTP实现
    public void click1(View view) {
        Log.d("ip:", getIp());
        final Request request = builder.get().url(Constant.URL0+"?hello=233").build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("TestActivity", "onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //请求成功时
                final String string = response.body().string();
                if(!TextUtils.isEmpty(string)){
                    List<Note> jsonList = JsonUtil.jsonToList(string,Note.class);
                    Log.d("TestActivity", jsonList.size()+"");
                    String mString = "";
                    for (Note note :
                            jsonList) {
                        mString +=note.getContent()+"\n";
                    }
                    //在主线程更新UI
                    final String finalMString = mString;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            testTextview.setText(finalMString);
                        }
                    });
                }else{
                    Log.e("TestActivity", "response is empty");
                }
            }
        });
    }

    //向服务器发送对象并接收服务器返回的结果
    public void click3(View view) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            date = simpleDateFormat.parse("2017-1-1");
            Note note = new Note(10,"","这是标题3","这是内容3",date ,10,date,"1","标签",date,true);
            Note note2 = new Note(11,"","这是标题4","这是内容4",date ,10,date,"1","标签",date,true);
            List<Note> notes = new ArrayList<>();
            notes.add(note);
            notes.add(note2);
            String s = new GsonBuilder().setPrettyPrinting().create().toJson(notes);
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain;chaset=utf-8"), s);
            Request request = builder.url(Constant.URL0).post(requestBody).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d("TestActivity", "onFailure");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    //请求成功时
                    final String string = response.body().string();
                    if(!TextUtils.isEmpty(string)){
                        List<Note> jsonList = JsonUtil.jsonToList(string,Note.class);
                        Log.d("TestActivity", jsonList.size()+"");
                        String mString = "";
                        for (Note note :
                                jsonList) {
                            mString +=note.getContent()+"\n";
                        }
                        //在主线程更新UI
                        final String finalMString = mString;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                testTextview.setText(finalMString);
                            }
                        });
                    }else{
                        Log.e("TestActivity", "response is empty");
                    }
                }
            });
            Log.d("TestActivity", s);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    //上传头像至服务器
    public void click4(View view) {
        Log.d("ip:", getIp());
        Drawable drawable = imageView3.getDrawable();//从imageview中获取drawable图片
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();//将drawable图片转成bitmap
        String s = FileUtil.writeToSDCard(bitmap,"download.png");//将图片存入sd卡获取图片保存的路径
        // TODO: 2017/6/26 改成用户实际的名字`
        File file = new File(s);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file );
        //post方式上传头像 参数放在url
        // TODO: 2017/6/27 用户名改为uid

        Request request = builder.url(Constant.URL0+"?username=username.png").post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("TestActivity", "click3onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //请求成功时
                final String string = response.body().string();
                if(!TextUtils.isEmpty(string)){
                    List<Note> jsonList = JsonUtil.jsonToList(string,Note.class);
                    Log.d("TestActivity", jsonList.size()+"");
                    String mString = "";
                    for (Note note :
                            jsonList) {
                        mString +=note.getContent()+"\n";
                    }
                    //在主线程更新UI
                    final String finalMString = mString;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            testTextview.setText(finalMString);
                        }
                    });
                }else{
                    Log.e("TestActivity", "response is empty");
                }
            }
        });

    }

    //从服务器读取一张图片并显示
    @OnClick(R.id.button9)
    public void onViewClicked() {
        Log.d("ip:", getIp());
        //post方式上传头像 参数放在url
        Request request = builder.url(Constant.BASEURL+"bbs/upload/user.png").get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("TestActivity", "click3onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //请求成功时
                InputStream inputStream = response.body().byteStream();
                File file = new File(Constant.PICTUREPATH,"download.png");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len =0;
                while ((len=inputStream.read(buffer))!=-1){
                    fileOutputStream.write(buffer,0,len);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeFile(Constant.PICTUREPATH +"download.png");//todo 正确命名图片
                        imageView3.setImageBitmap(bitmap);
                    }
                });
            }
        });

    }





    public String getIp() {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = intToIp(ipAddress);
        return ip;
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    public void click8(View view){
        NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(TestActivity.this);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        builder.setSmallIcon(R.drawable.ic_menu_camera)
                .setTicker("收到一条新通知")
                .setContentTitle("标题")
                .setContentText("这是正文内容")
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent);
        notificationManager.notify(0x233,builder.build());

    }
    public void click0(View view){
        FileUtil.WriteObjectToFile(Constant.notes);
    }
    public void click11(View view){
        List<Note> o = (List<Note>) FileUtil.ReadObjectFromFile();
        Log.d("TestActivity", "o.size():" + o.size());
        for (Note n :
                o) {
            Log.d("TestActivity", n.getContent());
        }
    }

}
