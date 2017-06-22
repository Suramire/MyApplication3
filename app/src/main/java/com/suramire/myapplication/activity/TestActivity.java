package com.suramire.myapplication.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.suramire.myapplication.R;
import com.suramire.myapplication.test.Student;
import com.suramire.myapplication.util.FileUtil;
import com.suramire.myapplication.util.JsonUtil;
import com.suramire.myapplication.util.Number;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        textView = (TextView) findViewById(R.id.test_textview);

        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/myHead/head.jpg");
        imageView3.setImageBitmap(bitmap);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TestActivity", "imageView3");
                // TODO: 2017/6/22  显示图片选择框，将选中的图片显示在imageview中
            }
        });
    }

    //接收JSON数据（JSONArray）并显示
    public void click1(View view) {
        final MyHandler myHandler = new MyHandler();
        Log.d("TestActivity", getIp());
        final Student student = new Student("王五", 30);
        Log.d("TestActivity", "student:" + student);
        //介绍服务器响应结果
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Number.URL0;
                JSONArray jsonArray = JsonUtil.getJSONArray(url);
                Message message = Message.obtain();
                message.what = Number.SHOWJSONARRAY;
                message.obj = jsonArray;
                myHandler.sendMessage(message);
            }
        }).start();
    }

    //向服务器发送对象并接收服务器返回的结果
    public void click3(View view) {
        new Thread(new Runnable() {

            private HttpURLConnection urlConnection;
            private ObjectOutputStream objectOutputStream;
            private BufferedReader bufferedReader;

            @Override
            public void run() {
                //发送对象并接收结果
                Log.e("TestActivity", "running");
                String url = Number.URL0;
                StringBuffer sb = new StringBuffer();
                try {
                    URL url1 = new URL(url);//将URL字符串转成
                    urlConnection = (HttpURLConnection) url1.openConnection();//发送http请求
                    urlConnection.setDoOutput(true);
                    urlConnection.setConnectTimeout(5 * 1000);
                    urlConnection.setReadTimeout(10 * 1000);
                    urlConnection.setRequestMethod("POST");//请求的方法为POST
                    objectOutputStream = new ObjectOutputStream(urlConnection.getOutputStream());
                    Student student = new Student("赵", 12);
                    objectOutputStream.writeObject(student);//发送studeny对象给服务器
                    InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
                    bufferedReader = new BufferedReader(reader);
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);//从缓冲中读取服务器返回的结果
                    }

                } catch (MalformedURLException e) {
                    Log.e("TestActivity", "MalformedURLException:" + e);
                } catch (IOException e) {
                    Log.e("TestActivity", "IOException:" + e);
                } catch (Exception e) {
                    Log.e("TestActivity", "Exception:" + e);

                } finally {
                    Log.e("TestActivity", "sb:" + sb.toString());
                    Log.e("TestActivity", "finish");
                }

            }
        }).start();
    }

    //上传头像至服务器
    public void click4(View view) {
        Drawable drawable = imageView3.getDrawable();//从imageview中获取drawable图片
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();//将drawable图片转成bitmap
        String s = writeToSDCard(bitmap);//将图片存入sd卡获取图片保存的路径
        imageView3.setImageBitmap(bitmap);
        final byte[] bytes = FileUtil.getBytes(s);//将根据图片路径将图片转成字节数组，方便存入服务器
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s1 = FileUtil.sendSomething(Number.URL0, bytes);//发送字节数组给服务器
                Log.d("TestActivity", s1);
            }
        }).start();


    }
    //从服务器读取一张图片并显示
    @OnClick(R.id.button9)
    public void onViewClicked() {
        final MyHandler myHandler = new MyHandler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = Number.BASEURL+"bbs/upload/test.bmp";
                Log.d("TestActivity", url);
                try {
                    URL url1 = new URL(url);

                    HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();
                    if (true) {
                        InputStream mInputStream = urlConnection.getInputStream();
                        byte[] bytes = new byte[mInputStream.available()];
                        mInputStream.read(bytes);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Message message = Message.obtain();
                        message.what = Number.SHOWIMAGE;
                        message.obj = bitmap;

                        myHandler.sendMessage(message);

                    }else {
                        Log.e("JsonUtil", "连接失败");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Number.SHOWJSONARRAY: {
                    if (msg.obj != null) {
                        JSONArray obj = (JSONArray) msg.obj;
                        if (obj.length() > 0) {
                            String s = "";
                            for (int i = 0; i < obj.length(); i++) {
                                try {
                                    JSONObject jsonObject = obj.getJSONObject(i);
                                    s += "name:" + jsonObject.getString("name") + "age:" + jsonObject.getInt("age") + "\n";
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            textView.setText(s);
                        } else {
                            textView.setText("没有找到");
                        }
                    } else {
                        textView.setText("没有找到");
                    }

                }
                break;
                case Number.SHOWIMAGE:{
                    Bitmap bitmap = (Bitmap) msg.obj;
                    imageView3.setImageBitmap(bitmap);
                    writeToSDCard(bitmap);
                }break;
            }
        }
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

    /**
     * 将bitmap图片写入SD卡
     *
     * @param mBitmap
     * @return 写入的文件路径 为空则表示写入失败
     */
    private String writeToSDCard(Bitmap mBitmap) {
        String path = "/sdcard/myHead/";// sd路径
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return "";
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            Log.d("TestActivity", "e:" + e);
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                Log.d("TestActivity", "e:" + e);
            }
        }
        return fileName;
    }

}
