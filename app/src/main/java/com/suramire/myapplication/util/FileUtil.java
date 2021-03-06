package com.suramire.myapplication.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suramire on 2017/6/22.
 * 实现对文件（图片）的相关操作
 */

public class FileUtil {
    /**
     * 将bitmap图片写入SD卡
     *
     * @param mBitmap
     * @return 写入的文件路径 为空则表示写入失败
     */
    public static String writeToSDCard(Bitmap mBitmap, String bitmapName) {
        String path = Constant.PICTUREPATH;
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return "";
        }
        FileOutputStream b = null;
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();// 创建文件夹
        String fileName = path+ bitmapName;// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            Log.e("TestActivity", "FileNotFoundException@writeToSDCard:" + e);
        } finally {
            try {// 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                Log.e("TestActivity", "IOException@writeToSDCard" + e);
            }
        }
        return fileName;
    }

    /**
     * 获取模拟数据
     * @param number 数量
     * @return
     */
    public static List<String> getStrings(int number){
        List<String> list = new ArrayList<>();
        if(number>=1){
            for (int i = 0; i < number; i++) {
                list.add("new data "+i);
            }
        }
        return  list;
    }

    public static void WriteObjectToFile(Object object){
        try {
            FileOutputStream fos = new FileOutputStream(Constant.PICTUREPATH+"temp.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (IOException e) {
            Log.e("FileUtil", "e:" + e);
        }
    }
    public static Object ReadObjectFromFile(){
        Object o = null;
        try {
            FileInputStream fis = new FileInputStream(Constant.PICTUREPATH+"temp.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
             o = ois.readObject();
            ois.close();
            fis.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }
}
