package com.suramire.myapplication.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseFullScreenActivity;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoselect);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.photo_img)
    public void onViewClicked() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setGridColumnCount(4)
                .start(PhotoSelectActicity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                Bitmap bitmap = BitmapFactory.decodeFile(photos.get(0));
                photoImg.setImageBitmap(bitmap);
            }



//            if (photos != null) {
//
//                selectedPhotos.addAll(photos);
//            }
//            photoAdapter.notifyDataSetChanged();
        }
    }
}
