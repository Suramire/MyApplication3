package com.suramire.myapplication.util;

import com.xmut.sc.entity.Note;
import com.xmut.sc.entity.User;

import java.util.List;

/**
 * Created by Suramire on 2017/6/21.
 * 存放常量
 */

public class Constant {
    /*常量区*/

    public  static String BASEURL = "http://10.0.2.2:8080/";//进入系统时读取
//    public final static String BASEURL = "http://192.168.1.101:8080/";
    public  static String URL0 = BASEURL+"bbs/Login";
    public  static String URL = BASEURL+"bbs/GetResult?do=";
    public final static String SDCARDDIR =  SDCardUtils.getSDCardPath();
    public final static String PICTUREPATH = SDCARDDIR + "/bbs_head/";
    public final  static  String DBNAME = "localuser.db";//本地数据库名
    public final  static  String TABLENAME = "user";//本地数据表名
    public final  static  String SPLIT = "splitzero";
    public final static  int DELAY = 300;//动画延迟
    public final static int NUM = 100;//随机数范围
    public final static String URLNOTIFICATION = BASEURL+"bbs/Notification";//链接服务器
    public final static int UPDATESUCCESS = 0x9;//成功修改用户信息
    public final static int LOGINSUCCESS = 0x10;//成功修改用户信息
    public final static int CHANGEUCCESS = 0x11;//成功修改系统设置
    /*全局变量区*/
    public static String userName = "";//保存当前登录的用户名
    public static User user;//保存已登录的用户
    public static Note note;//当前查看的帖子
    public static int indexCount;//首页已加载帖子数量
    public static List<Note> notes;//已缓冲的List<Note>
    public static boolean isDestory;//首页是否被销毁过
    public static boolean isPostRunning;//推送服务是否在运行
}
