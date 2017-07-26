package com.suramire.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.App;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.FileUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by Suramire on 2017/6/23.
 */

public class PhotoSelectActicity extends AppCompatActivity {
    @Bind(R.id.photo_img)
    ImageView photoImg;
    @Bind(R.id.button10)
    Button button10;
    private boolean hasImg = false;
    private Bitmap bitmap;
    public static final int CODESUCCESS = 0x1;
    public static final int CODEFIAL = 0x2;
    private App mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoselect);
        ButterKnife.bind(this);
        mContext = App.getContext();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                hasImg = true;
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                bitmap = BitmapFactory.decodeFile(photos.get(0));
                photoImg.setImageBitmap(bitmap);
            }


//            if (photos != null) {
//
//                selectedPhotos.addAll(photos);
//            }
//            photoAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.photo_img, R.id.button10})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.photo_img:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setGridColumnCount(4)
                        .start(PhotoSelectActicity.this);
                break;
            case R.id.button10:
                // TODO: 2017/6/27 如果用户选择了头像,则将头像发送给服务器
                try {
                    if(hasImg){
                        String s = FileUtil.writeToSDCard(bitmap,Constant.userName+".png");//将图片存入sd卡获取图片保存的路径
                        File file = new File(s);
                        HTTPUtil.getPost(Constant.URL+"upload&uid="+ SPUtils.get("uid",0), file, new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {

                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                String string = response.body().string();
                                L.e("这里显示上传结果:" + string);
                                if(string.equals("success")){
                                    //这里更新用户的头像
                                    // TODO: 2017/6/27 如果有本地图片则先读取本地图片
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                            setResult(CODESUCCESS);
                                            finish();
                                        }
                                    });

                                }else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
                                            setResult(CODEFIAL);
                                            finish();
                                        }
                                    });

                                }

                            }
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, "发生错误:" + e, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
