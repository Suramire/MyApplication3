package com.zjw.web;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Operaton
{

    public String login(String url, String username, String password)
    {
        String result = null;
        ConnNet connNet=new ConnNet();
        List<NameValuePair> params=new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("flag", "login33"));//传给服务器的标杆
        try {
            HttpEntity entity=new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost httpPost=connNet.gethttPost(url);
            System.out.println(httpPost.toString());
            httpPost.setEntity(entity);
            HttpClient client=new DefaultHttpClient();
            HttpResponse httpResponse=client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
                result=EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            }
            else
            {
                result="登录失败";
            }
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (ParseException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return result;
    }
    public String adminlogin(String url, String adminname, String password)
    {
        String result = null;
        ConnNet connNet=new ConnNet();
        List<NameValuePair> params=new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("adminname", adminname));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("flag", "adminlogin33"));//传给服务器的标杆
        try {
            HttpEntity entity=new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost httpPost=connNet.gethttPost(url);
            System.out.println(httpPost.toString());
            httpPost.setEntity(entity);
            HttpClient client=new DefaultHttpClient();
            HttpResponse httpResponse=client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
                result=EntityUtils.toString(httpResponse.getEntity(), "utf-8");
            }
            else
            {
                result="登录失败";
            }
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (ParseException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return result;
    }

    public String checkusername(String url, String username)
    {
        String result=null;
        ConnNet connNet=new ConnNet();
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        try {
            HttpEntity entity=new UrlEncodedFormEntity(params, HTTP.UTF_8);
            HttpPost httpPost=connNet.gethttPost(url);
            System.out.println(httpPost.toString());
            httpPost.setEntity(entity);
            HttpClient client=new DefaultHttpClient();
            HttpResponse httpResponse=client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
            {
                result=EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                System.out.println("resu"+result);
            }
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (ClientProtocolException e) {

            e.printStackTrace();
        } catch (ParseException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return result;
    }

    public String UpData(String uripath, String jsonString)
    {
        String result = null;
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        NameValuePair nvp=new BasicNameValuePair("jsonstring", jsonString);
        list.add(new BasicNameValuePair("flag", "register33"));//传给服务器的标杆
        list.add(nvp);
        ConnNet connNet=new ConnNet();
        HttpPost httpPost=connNet.gethttPost(uripath);
        try {
            HttpEntity entity = new UrlEncodedFormEntity(list, HTTP.UTF_8);
            //此句必须加上否则传到客户端的中文将是乱码
            httpPost.setEntity(entity);
            HttpClient client=new DefaultHttpClient();
            HttpResponse httpResponse=client.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode()==200)
            {
                result=EntityUtils.toString(httpResponse.getEntity(), "utf-8");
                System.out.println("resu"+result);
            }
            else {
                result="注册失败";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }




}
