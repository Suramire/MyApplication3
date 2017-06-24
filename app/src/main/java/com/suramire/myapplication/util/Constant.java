package com.suramire.myapplication.util;

import android.os.Environment;

/**
 * Created by Suramire on 2017/6/21.
 * 存放常量
 */

public class Constant {
    public final static int SHOWJSONARRAY = 0x234;
    public final static int SHOWIMAGE = 0x235;
    public final static int SHOWRESULT = 0x236;
    public final static int SHOWNOTHING = 0x237;
    public final static int GETLIST = 0x238;
    public final static String BASEURL = "http://10.0.2.2:8080/";
//    public final static String BASEURL = "http://192.168.1.102:8080/";
    public final static String URL0 = BASEURL+"bbs/Login";
    public final static String URLGUESS = BASEURL+"bbs/Guess";

    public final static String PICTUREPATH = Environment.getExternalStorageDirectory() + "/bbs_head/";
    public final  static  String DBNAME = "localuser.db";//本地数据库名
    public final  static  String TABLENAME = "user";//本地数据表名
    public  static boolean isLogin = false;
    public static String userName = "";
}
