package com.suramire.myapplication.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Suramire on 2017/6/22.
 * 实现对文件（图片）的相关操作
 */

public class FileUtil {
    /**
     * 将file文件转化为byte数组
     *
     * @param filePath
     * @return byte数组
     */
    public static byte[] getBytes(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 向服务器发送数据
     * @param object 要发送的数据
     * @param url 目标地址
     * @return 服务器响应的结果
     * @since 2017-6-22 11:25:19
     * @author Suramire
     */
    public static String sendSomething(String url,Object object){
        ObjectOutputStream objectOutputStream = null;
        BufferedReader bufferedReader = null;
        HttpURLConnection urlConnection = null;
        StringBuffer sb = new StringBuffer();
        try {
            URL url1 = new URL(url);
            urlConnection = (HttpURLConnection) url1.openConnection();//发送http请求
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(5 * 1000);
            urlConnection.setReadTimeout(10 * 1000);
            urlConnection.setRequestMethod("POST");//请求的方法为POST
            objectOutputStream = new ObjectOutputStream(urlConnection.getOutputStream());
            objectOutputStream.writeObject(object);//发送对象给服务器
            InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
            bufferedReader = new BufferedReader(reader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);//从缓冲中读取服务器返回的结果
            }
        } catch (MalformedURLException e) {
            Log.e("FileUtil", "MalformedURLException:" + e);
        } catch (IOException e) {
            Log.e("FileUtil", "IOException:" + e);
        }catch (Exception e){
            Log.e("FileUtil", "Exception:" + e);
        }

        finally {

                try {
                    if(bufferedReader!=null)
                        bufferedReader.close();
                    if(objectOutputStream!=null)
                        objectOutputStream.close();
                    if(urlConnection!=null)
                        urlConnection.disconnect();
                } catch (IOException e) {
                    Log.d("FileUtil", "IOException@Close:" + e);
                }
            return sb.toString();
        }
    }
}
