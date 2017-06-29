package com.zxf.scode;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


import com.suramire.myapplication.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;



public class MyInformation extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinformation);
        Button login=(Button)findViewById(R.id.login);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent();
                intent.setClass(MyInformation.this,Login.class);
                startActivity(intent);
            }
        });

        Button information=(Button)findViewById(R.id.information);
        information.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //todo 登录判断

                //todo 實現查詢 未實現
                queryinformation();
            }

            private void queryinformation() {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        String usernameString="vxv";

//						String urlString = "http://10.0.2.2:8090/android_service/Update?username="+usernameString+"&uid="+1;
                        String urlString = "http://10.0.2.2:8090/android_service/Query?do=update?username="+usernameString;
                        Log.e("url", urlString);
                        URL url;
                        try {
                            url = new URL(urlString);
                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                            Log.e("ResponseCode", urlConnection.getResponseCode()+"");
                            InputStream is =urlConnection.getInputStream();
                            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            while(-1 != (len = is.read(buffer))){
                                baos.write(buffer,0,len);
                                baos.flush();
                            }
                            Log.e("result",baos.toString("utf-8") );
                            runOnUiThread( new Runnable() {
                                public void run() {
                                    try {

                                        if(baos.toString("utf-8").equals("user")){
                                            //todo 查詢后輸出
                                        }

                                        else{
                                            Toast.makeText(MyInformation.this, "查詢失败", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
//							urlConnection.connect();
                        } catch (Exception e){
                            Log.e("error",e+"");
                        }
                    }
                }).start();
            };

        });
    }
}


	

