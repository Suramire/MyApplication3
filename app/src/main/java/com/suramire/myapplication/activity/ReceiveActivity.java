package com.suramire.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.HTTPUtil;
import com.suramire.myapplication.util.JsonUtil;
import com.suramire.myapplication.util.L;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Receive;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.suramire.myapplication.util.Constant.URL;

/**
 * Created by Suramire on 2017/6/25.
 */

public class ReceiveActivity extends BaseActivity {

    @Bind(R.id.receive_listview)
    ListView listView;
    @Bind(R.id.editText2)
    EditText receivecontent;
    @Bind(R.id.receive_send)
    Button receiveSend;
    @Bind(R.id.receive_empty)
    TextView receiveEmpty;
    private List<Receive> receiveList;
    private int nid;
    private int uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receice);
        ButterKnife.bind(this);
        listView.setEmptyView(receiveEmpty);
        nid = getIntent().getIntExtra("nid", 0);
        uid = (int) SPUtils.get(this, "uid", 0);
        //根据nid查询该帖子的评论
        getReceive();


    }


    @OnClick(R.id.receive_send)
    public void onViewClicked() {
        if(uid>0){
            String content = receivecontent.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                //提交评论
                //1 在本地创建一个Receive对象
                final Receive receive = new Receive();
                receive.setContent(content);
                receive.setReceivetime(new Date());
                receive.setUid(uid);
                receive.setNid(nid);
                //2 将此对象转成JSON字符串 传送给服务器
                String jsonString = JsonUtil.objectToJson(receive);
                HTTPUtil.getCall(Constant.URL + "addreceive&nid=" + nid + "&json=" + jsonString, new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        L.e("发送评论失败");
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String result = response.body().string();
                        String show = "";
                        if (!TextUtils.isEmpty(result)) {
                            if (result.equals("success")) {
                                show = "评论成功";
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //根据nid查询该帖子的评论
                                        getReceive();
                                    }
                                });
                            } else {
                                show = "评论失败";
                            }
                            final String finalShow = show;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ReceiveActivity.this, finalShow, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });

            }else {
                Toast.makeText(this, "评论内容不能为空哦", Toast.LENGTH_SHORT).show();
            }
        }else {
            // TODO: 2017/6/27 这里用弹窗的方式显示
            Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    private void getReceive(){
        HTTPUtil.getCall(URL + "getReceive&nid=" + nid, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("ReceiveActivity", "onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("ReceiveActivity", "onResponse");

                String string = response.body().string();
                if (!TextUtils.isEmpty(string)) {
                    //这边接收评论并显示
                    receiveList = JsonUtil.jsonToList(string, Receive.class);

                    final int size = receiveList.size();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(size>0){
                                getSupportActionBar().setTitle("共("+size+")条评论");
                            }else{
                                getSupportActionBar().setTitle("暂无评论");
                            }
                        }
                    });

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(new CommonAdapter<Receive>(ReceiveActivity.this, R.layout.item_receive, receiveList) {
                                @Override
                                public void onUpdate(BaseAdapterHelper helper, Receive item, int position) {
                                    // FIXME: 2017/6/26 分隔符待改进
                                    String[] split = item.getContent().split(Constant.SPLIT);
                                    String name = split[0];
                                    String content = split[1];
                                    helper.setText(R.id.receive_time, item.getReceivetime() + "")
                                            .setText(R.id.receive_content, content)
                                            .setText(R.id.receice_name, name);
//
                                }
                            });
                        }
                    });
                }
            }
        });
    }

}
