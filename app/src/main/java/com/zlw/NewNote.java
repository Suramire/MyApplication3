package com.zlw;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.suramire.myapplication.R;
import com.suramire.myapplication.base.BaseActivity;
import com.suramire.myapplication.util.Constant;
import com.suramire.myapplication.util.SPUtils;
import com.xmut.sc.entity.Note;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zlw on 2017/6/25.
 */

public class NewNote extends BaseActivity {
//    private String path= Constant.BASEURL+"android_service/GetData";
    private EditText ed_title,ed_content,ed_type;
    private CheckBox cb;
    private Spinner spinner;
    private Note note=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    protected int getContentViewId() {
        return R.layout.layout_newnote;
    }

    @Override
    protected  void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        note=new Note();
        ed_content = (EditText) findViewById(R.id.id_content);
        ed_title = (EditText) findViewById(R.id.id_title);
        ed_type = (EditText) findViewById(R.id.ed_type);
        cb= (CheckBox) findViewById(R.id.checkBox);
        spinner= (Spinner) findViewById(R.id.id_spinner);
        //判断共享是否被选中
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    note.setIsshare(true);
                    Log.i("check","true");

                }else{
                    note.setIsshare(false);
                    Log.i("check","false");

                }
            }
        });

        final List<String> list=new ArrayList<>();
        String[] stringArray = getResources().getStringArray(R.array.types);
        for (int i = 0; i < stringArray.length; i++) {
            list.add(stringArray[i]);
        }
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String data = list.get(i);

                note.setType((i+1)+"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setAdapter(adapter);

    }



    /**
     * 点击发布按钮
     *
     */
    public void release(){

        if (init(note)) {
            Gson gson=new Gson();
            String toJson = gson.toJson(note);
            Log.i("flag",toJson);
            OkHttpClient okHttpClient=new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            final Request request = builder.get().url(Constant.URL+"add&note="+toJson).addHeader("content-type","application/json:charset:utf-8").build();

            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                }

                @Override
                public void onResponse(Response response) throws IOException {


                    final String result = response.body().string();
                    if(!TextUtils.isEmpty(result)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(result.equals("true")){
                                    Toast.makeText(NewNote.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(NewNote.this, "发布失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }

                }
            });
        }else{

        }

    }

    /**
     * 初始化Note
     * @param note
     * 添加帖子完整性判断 2017年7月4日 20:24:25
     */
    private boolean init(Note note) {
        String title = ed_title.getText().toString().trim();
        String content = ed_content.getText().toString().trim();
        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content)){
            Toast.makeText(this, "标题和内容都不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            note.setTitle(title);
            note.setContent(content);
            Date date = new Date();
            note.setPublishtime(date);
            note.setEdittime(date);
            note.setViewtime(date);
            note.setCount(0);
            note.setUid((int)SPUtils.get("uid",0));
            note.setTag(ed_type.getText().toString().trim());
            note.setIsshare(cb.isChecked());
            return true;
        }

    }

    /**
     * 格式化当前时间
     */
    public static String formatDate(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = sDateFormat.format(new Date(System.currentTimeMillis()));

        Log.i("time",format);
        return  format;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done){
            release();
        }
        return super.onOptionsItemSelected(item);
    }
}
