package com.suramire.myapplication.mvp;

import android.os.SystemClock;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.List;

import static com.suramire.myapplication.util.Constant.currentCount;
import static com.suramire.myapplication.util.Constant.notes;

/**
 * Created by Suramire on 2017/7/20.
 */

public class BaseModel implements IBaseModel {
    @Override
    public void getData(String what, final OnGetDataListener onGetDataListener) {
        HTTPUtil.getCall(Constant.URL +what,new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                SystemClock.sleep(3000);
                onGetDataListener.getFail();
            }
            @Override
            public void onResponse(Response response) throws IOException {
                String string = response.body().string();
                List<Note> mNotes = GsonUtil.jsonToList(string, Note.class);
                final int mcount = mNotes.size();//本次刷新的数量
                currentCount = mcount;
                if(mcount>0){
                    if (notes==null) {
                        notes = mNotes;
                    } else {
                        for(Note note:notes){
                            mNotes.add(note);
                        }
                        notes = mNotes;
                    }
                    SystemClock.sleep(3000);
                    onGetDataListener.getSuccessful(notes);
                    Constant.indexCount += mcount;//记录帖子数
                }else{
                    onGetDataListener.getFail();
                }
            }
        });
    }
}
