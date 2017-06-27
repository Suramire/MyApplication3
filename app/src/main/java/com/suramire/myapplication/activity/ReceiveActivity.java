package com.suramire.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Receive;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.suramire.myapplication.util.Constant.URL;

/**
 * Created by Suramire on 2017/6/25.
 */

public class ReceiveActivity extends BaseActivity {

    private List<Receive> receiveList;
    private ListView listView;
    private int nid;
    private int uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receice);
        listView = (ListView) findViewById(R.id.receive_listview);
        listView.setEmptyView(findViewById(R.id.receive_empty));
        nid = getIntent().getIntExtra("nid", 0);
        uid = (int) SPUtils.get(this, "uid", 0);
        //根据nid查询该帖子的评论
        HTTPUtil.getCall(URL + "getReceive&nid=" + nid, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d("ReceiveActivity", "onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d("ReceiveActivity", "onResponse");

                String string = response.body().string();
                if(!TextUtils.isEmpty(string)) {
                    //这边接收评论并显示
                    receiveList = JsonUtil.jsonToList(string, Receive.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(new CommonAdapter<Receive>(ReceiveActivity.this,R.layout.item_receive, receiveList) {
                                @Override
                                public void onUpdate(BaseAdapterHelper helper, Receive item, int position) {
                                    // FIXME: 2017/6/26 分隔符待改进
                                    String[] split = item.getContent().split(Constant.SPLIT);
                                    String name = split[0];
                                    String content = split[1];
                                    helper.setText(R.id.receive_time,item.getReceivetime()+"")
                                            .setText(R.id.receive_content,item.getContent())
                                            .setText(R.id.receice_name,name);
//
                                }
                            });
                        }
                    });
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_receive_add,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==R.id.receive_add){
            if(uid>0){
                //            Toast.makeText(this, "弹出写评论的对话框", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder  = new AlertDialog.Builder(this);
                builder.setTitle("留下你的评论").setIcon(R.mipmap.ic_launcher);
                View view = View.inflate(this, R.layout.dialog_receive, null);
                final EditText editText = (EditText) view.findViewById(R.id.receive_content);
                Spinner spinner = view.findViewById(R.id.spinner);
                builder.setView(view);

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 2017/6/25 判断用户输入,不为空则提交到服务器
                        // TODO: 2017/6/25 提交后刷新评论列表
//                    Toast.makeText(ReceiveActivity.this,"你输入了"+editText.getText(), Toast.LENGTH_SHORT).show();
                        String content = editText.getText().toString().trim();
                        if(!TextUtils.isEmpty(content)){
                            //提交评论
                            //1 在本地创建一个Receive对象
                            final Receive receive = new Receive();
                            receive.setContent(content);
                            receive.setReceivetime(new Date());
                            receive.setUid(uid);
                            receive.setNid(nid);
                            //2 将此对象转成JSON字符串 传送给服务器
                            String jsonString = JsonUtil.objectToJson(receive);
                            HTTPUtil.getCall(Constant.URL+ "addreceive&nid="+nid+"&json=" + jsonString,new Callback() {
                                @Override
                                public void onFailure(Request request, IOException e) {
                                    Log.d("ReceiveActivity", "onFailure");
                                }

                                @Override
                                public void onResponse(Response response) throws IOException {
                                    String result = response.body().string();
                                    String show = "";
                                    if(!TextUtils.isEmpty(result)){
                                        if(result.equals("success")){
                                            show = "评论成功";
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //根据nid查询该帖子的评论
                                                    HTTPUtil.getCall(URL + "getReceive&nid=" + nid, new Callback() {
                                                        @Override
                                                        public void onFailure(Request request, IOException e) {
                                                            Log.d("ReceiveActivity", "onFailure");
                                                        }

                                                        @Override
                                                        public void onResponse(Response response) throws IOException {
                                                            Log.d("ReceiveActivity", "onResponse");

                                                            String string = response.body().string();
                                                            if(!TextUtils.isEmpty(string)) {
                                                                //这边接收评论并显示
                                                                receiveList = JsonUtil.jsonToList(string, Receive.class);
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        listView.setAdapter(new CommonAdapter<Receive>(ReceiveActivity.this,R.layout.item_receive, receiveList) {
                                                                            @Override
                                                                            public void onUpdate(BaseAdapterHelper helper, Receive item, int position) {
                                                                                // FIXME: 2017/6/26 分隔符待改进
                                                                                String[] split = item.getContent().split(Constant.SPLIT);
                                                                                String name = split[0];
                                                                                String content = split[1];
                                                                                helper.setText(R.id.receive_time,item.getReceivetime()+"")
                                                                                        .setText(R.id.receive_content,item.getContent())
                                                                                        .setText(R.id.receice_name,name);
//
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }else{
                                            show = "评论失败";
                                        }
                                    }
                                    final String finalShow = show;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ReceiveActivity.this, finalShow, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                            //3 接收服务器结果

                            //4 根据结果是否刷新评论

                        }else{
                            Toast.makeText(ReceiveActivity.this, "内容不能为空哦", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                builder.setCancelable(false).show();
            }else {
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
            }



        }
        return super.onOptionsItemSelected(item);
    }
}
