package com.zxf.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.zxf.web.ModifyConnNet;
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



public class ModifyOperaton<ConnNet> {



    public String UpData(String uripath, String jsonString) {
        String result = null;
        List<NameValuePair> list=new ArrayList<NameValuePair>();
        NameValuePair nvp=new BasicNameValuePair("jsonstring", jsonString);
        list.add(new BasicNameValuePair("do", "modify33"));//传给服务器的标杆
        list.add(nvp);
        ModifyConnNet connNet=new ModifyConnNet();
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
                result="保存失败";
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

    /**
     *
     * public String checkusername(String url,String username)
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
     */
    public String Readinformaion(String url,String username){
        String result = null;
        ModifyConnNet modifyConnNet=new ModifyConnNet();
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username",username ));

        return username;


    }

}
