package com.suramire.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.DateUtil;
import com.suramire.myapplication.util.GsonUtil;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Receive;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Suramire on 2017/6/29.
 */

public class UserReceive extends BaseActivity {
    @Bind(R.id.listview_userreceive)
    ListView listviewUserreceive;
    @Bind(R.id.receive_empty)
    TextView receiveEmpty;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_userreceive;
    }

    @Override
    protected void initView() {
        listviewUserreceive.setEmptyView(receiveEmpty);
        L.e("查看回复记录 url"+Constant.URL + "getReceiveByUid&uid=" + SPUtils.get("uid", 0));
        HTTPUtil.getCall(Constant.URL + "getReceiveByUid&uid=" + SPUtils.get("uid", 0), new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String string = response.body().string();
                if(!TextUtils.isEmpty(string)){
                    final List<Receive> receives = GsonUtil.jsonToList(string,Receive.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(receives.size()>0){
                                listviewUserreceive.setAdapter(new CommonAdapter<Receive>(UserReceive.this,R.layout.item_receive,receives) {
                                    @Override
                                    public void onUpdate(BaseAdapterHelper helper, Receive item, int position) {
                                        helper.setText(R.id.receive_time, DateUtil.dateToString(item.getReceivetime()) + "")
                                                .setText(R.id.receive_content, item.getContent());
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
    }
}
