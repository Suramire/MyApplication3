package com.suramire.myapplication.util;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Suramire on 2017/6/20.
 * JSon 工具类
 * getJSONObject
 * getJSONArray
 */



public class JsonUtil {

    /**
     * 根据url地址获取JSONObject
     *
     * @param url 提交的URL IP:端口/项目名/Servlet名?参数
     * @return 目标JSONObject
     */
    public static JSONObject getJSONObject(final String url) {
        JSONObject mJSONObject = null;
        try {
            URL mURL = new URL(url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) mURL.openConnection();
            if (true) {
                InputStream mInputStream = mHttpURLConnection.getInputStream();
                byte[] bytes = new byte[mInputStream.available()];
                mInputStream.read(bytes);
                String mJSONString = new String(bytes);
                mJSONObject = new JSONObject(mJSONString);
            } else {
                Log.e("JsonUtil", "连接失败");
            }
            mHttpURLConnection.disconnect();
        }catch (MalformedURLException e) {
            Log.e("JsonUtil", "MalformedURLException:" + e);
        } catch (IOException e) {
            Log.e("JsonUtil", "IOException:" + e);
        } catch (JSONException e) {
            Log.e("JsonUtil", "JSONException:" + e);
        }catch (Exception e){
            Log.e("JsonUtil", "Exception:" + e);
        }
        return mJSONObject;
    }

    /**
     * 根据url获取JSONArray
     *
     * @param url 请求的地址
     * @return 目标JSONArray
     */
    public static JSONArray getJSONArray(String url) {
        JSONArray mJSONArray = null;
        try {
            URL mURL = new URL(url);
            HttpURLConnection mHttpURLConnection = (HttpURLConnection) mURL.openConnection();
            if (true) {
                InputStream mInputStream = mHttpURLConnection.getInputStream();
                byte[] bytes = new byte[mInputStream.available()];
                mInputStream.read(bytes);
                String mJSONString = new String(bytes);
                mJSONArray = new JSONArray(mJSONString);
            }else {
                Log.e("JsonUtil", "连接失败");
            }
        } catch (MalformedURLException e) {
            Log.e("JsonUtil", "MalformedURLException:" + e);
        } catch (IOException e) {
            Log.e("JsonUtil", "IOException:" + e);
        } catch (JSONException e) {
            Log.e("JsonUtil", "JSONException:" + e);
        }catch (Exception e){
            Log.e("JsonUtil", "Exception:" + e);
        }
        return mJSONArray;

    }

}