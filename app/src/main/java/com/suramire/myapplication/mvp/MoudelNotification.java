package com.suramire.myapplication.mvp;

import android.text.TextUtils;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suramire on 2017/7/21.
 */

public class MoudelNotification implements IMoudleNotification {
    @Override
    public void getData(final OnGetDataListener onGetDataListener) {
        double r = Math.random() * 100;
        int random = (int) r;
        final List<Note> notes = new ArrayList<>();
        HTTPUtil.getCall(Constant.URL +"randomQuery&nid=" + random, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String res = response.body().string();
                L.e("res:" + res);
                if (!TextUtils.isEmpty(res)) {
                    //截取
                    String part1 = res.substring(0, res.indexOf("?"));
                    String part2 = res.substring(res.indexOf("?") + 1);
                    if (!part2.equals("{}")) {
                        Note note = (Note) GsonUtil.jsonToObject(part2, Note.class);
                        notes.add(note);
                        if(notes.size()>0){
                            onGetDataListener.getSuccessful(notes);
                        }else{
                            onGetDataListener.getFail();
                        }
                    }
                }
            }
        });

    }


}
