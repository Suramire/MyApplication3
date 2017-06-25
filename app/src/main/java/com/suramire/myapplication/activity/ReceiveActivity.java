package com.suramire.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.FileUtil;
import com.suramire.myapplication.util.JsonUtil;
import com.xmut.sc.entity.Receive;

import java.io.IOException;
import java.util.Date;

/**
 * Created by Suramire on 2017/6/25.
 */

public class ReceiveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receice);
        ListView listView = (ListView) findViewById(R.id.receive_listview);
        listView.setAdapter(new CommonAdapter<String>(this,R.layout.item_receive, FileUtil.getStrings(10)) {
            @Override
            public void onUpdate(BaseAdapterHelper helper, String item, int position) {
                helper.setText(R.id.receice_name, "评论者:" + item);
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
                    if(!content.isEmpty()){
                        //提交评论
                        //1 在本地创建一个Receive对象
                        final Receive receive = new Receive();
                        receive.setContent(content);
                        receive.setReceivetime(new Date());
                        receive.setUid(1);
                        receive.setNid(2);
                        //2 将此对象转成JSON字符串 传送给服务器
                        String jsonString = JsonUtil.getJsonString(receive);
                        OkHttpClient okHttpClient = new OkHttpClient();
                        Request.Builder builder1 = new Request.Builder();
                        Request request = builder1.url(Constant.URLRECEIVE + "?json=" + jsonString).get().build();
                        Call call = okHttpClient.newCall(request);
                        //3 接收服务器结果
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Request request, IOException e) {
                                Log.d("ReceiveActivity", "onFailure");
                            }

                            @Override
                            public void onResponse(Response response) throws IOException {
                                String result = response.body().string();
                                String show = "";
                                if(!result.isEmpty()){
                                    if(result.equals("success")){
                                        show = "评论成功";
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

                        //4 根据结果是否刷新评论

                    }else{

                    }

                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {


                }
            });
            builder.setCancelable(false).show();


        }
        return super.onOptionsItemSelected(item);
    }
}
