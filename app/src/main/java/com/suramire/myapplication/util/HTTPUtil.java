package com.suramire.myapplication.util;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.io.File;

/**
 * Created by Suramire on 2017/6/26.
 */

public class HTTPUtil {
    /**
     * Post方式
     * @param url
     * @return
     */
    public static Call getCall(String url,Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request build = builder.get().url(url).build();
        Call call = okHttpClient.newCall(build);
        call.enqueue(callback);
        return  call;
    }

    public static Call getPost(String url,File file,Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file );
        Request build = builder.post(requestBody).url(url).build();
        Call call = okHttpClient.newCall(build);
        call.enqueue(callback);
        return  call;

    }
}
